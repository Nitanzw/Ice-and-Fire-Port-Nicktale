package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Remembers every dragon cave / roost that spawned a dragon so the site (structure and dragon) can be generated again
 * twelve real-time hours after the dragon died or was tamed.
 */
public class DragonRespawnData extends SavedData {
    /** Real time (not game time) after which an emptied site regenerates. */
    public static final long RESPAWN_MILLIS = Long.getLong("iaf.dragonRespawnMillis", 12L * 60L * 60L * 1000L);

    public record Site(String featureId, String dimension, BlockPos origin, UUID dragon, long freedAt) {
        private static final Codec<Site> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("feature").forGetter(Site::featureId),
                Codec.STRING.fieldOf("dimension").forGetter(Site::dimension),
                BlockPos.CODEC.fieldOf("origin").forGetter(Site::origin),
                UUIDUtil.CODEC.optionalFieldOf("dragon").forGetter(site -> Optional.ofNullable(site.dragon())),
                Codec.LONG.optionalFieldOf("freed_at", 0L).forGetter(Site::freedAt)
        ).apply(instance, (feature, dim, origin, dragon, freed) -> new Site(feature, dim, origin, dragon.orElse(null), freed)));

        boolean sameSite(String feature, String dim, BlockPos pos) {
            return featureId.equals(feature) && dimension.equals(dim) && origin.equals(pos);
        }
    }

    /** Sites registered from worldgen threads; merged into the saved data on the server thread. */
    private static final ConcurrentLinkedQueue<Site> PENDING = new ConcurrentLinkedQueue<>();

    private static final Codec<DragonRespawnData> CODEC = Site.CODEC.listOf().fieldOf("sites").codec()
            .xmap(DragonRespawnData::new, data -> data.sites);
    private static final SavedDataType<DragonRespawnData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(IceAndFire.MODID, "dragon_respawn"),
            DragonRespawnData::new,
            CODEC
    );

    private final List<Site> sites;

    public DragonRespawnData() {
        this.sites = new ArrayList<>();
    }

    private DragonRespawnData(List<Site> sites) {
        this.sites = new ArrayList<>(sites);
    }

    public static DragonRespawnData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    /** Safe to call from any thread. */
    public static void register(String featureId, String dimension, BlockPos origin, UUID dragon) {
        PENDING.add(new Site(featureId, dimension, origin.immutable(), dragon, 0L));
    }

    public void drainPending() {
        Site polled;
        while ((polled = PENDING.poll()) != null) {
            final Site site = polled;
            sites.removeIf(existing -> existing.sameSite(site.featureId(), site.dimension(), site.origin()));
            sites.add(site);
            setDirty();
        }
    }

    /** The dragon of a site died or was tamed: start the respawn timer. */
    public void markFreed(UUID dragon) {
        for (int i = 0; i < sites.size(); i++) {
            Site site = sites.get(i);
            if (dragon.equals(site.dragon()) && site.freedAt() == 0L) {
                sites.set(i, new Site(site.featureId(), site.dimension(), site.origin(), site.dragon(), System.currentTimeMillis()));
                IceAndFire.LOGGER.info("Dragon site {} at {} freed, respawning in {} ms", site.featureId(), site.origin(), RESPAWN_MILLIS);
                setDirty();
            }
        }
    }

    /** Sites whose timer ran out; they are removed and re-registered by the feature that regenerates them. */
    public List<Site> takeDue(long now, long delay) {
        List<Site> due = new ArrayList<>();
        for (Site site : sites) {
            if (site.freedAt() > 0L && now - site.freedAt() >= delay) {
                due.add(site);
            }
        }
        if (!due.isEmpty()) {
            sites.removeAll(due);
            setDirty();
        }
        return due;
    }
}
