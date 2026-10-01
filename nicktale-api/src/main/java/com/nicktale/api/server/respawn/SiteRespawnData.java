package com.nicktale.api.server.respawn;

import com.nicktale.api.NicktaleApi;
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
 * Saved record of world-generated sites (a configured feature placed at an origin, guarding one entity) that
 * regenerate a fixed real-time delay after their entity is gone.
 */
public class SiteRespawnData extends SavedData {
    /** A site: the configured feature that builds it, where, the entity it spawned, when that entity was lost (0 = alive). */
    public record Site(String featureId, String dimension, BlockPos origin, UUID entity, long freedAt, long delayMillis) {
        private static final Codec<Site> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("feature").forGetter(Site::featureId),
                Codec.STRING.fieldOf("dimension").forGetter(Site::dimension),
                BlockPos.CODEC.fieldOf("origin").forGetter(Site::origin),
                UUIDUtil.CODEC.optionalFieldOf("entity").forGetter(site -> Optional.ofNullable(site.entity())),
                Codec.LONG.optionalFieldOf("freed_at", 0L).forGetter(Site::freedAt),
                Codec.LONG.fieldOf("delay_millis").forGetter(Site::delayMillis)
        ).apply(instance, (feature, dim, origin, entity, freed, delay) -> new Site(feature, dim, origin, entity.orElse(null), freed, delay)));

        boolean sameSite(String feature, String dim, BlockPos pos) {
            return featureId.equals(feature) && dimension.equals(dim) && origin.equals(pos);
        }
    }

    /** Sites registered from worldgen threads; merged into the saved data on the server thread. */
    private static final ConcurrentLinkedQueue<Site> PENDING = new ConcurrentLinkedQueue<>();

    private static final Codec<SiteRespawnData> CODEC = Site.CODEC.listOf().fieldOf("sites").codec()
            .xmap(SiteRespawnData::new, data -> data.sites);
    private static final SavedDataType<SiteRespawnData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(NicktaleApi.MOD_ID, "site_respawn"),
            SiteRespawnData::new,
            CODEC
    );

    private final List<Site> sites;

    public SiteRespawnData() {
        this.sites = new ArrayList<>();
    }

    private SiteRespawnData(List<Site> sites) {
        this.sites = new ArrayList<>(sites);
    }

    public static SiteRespawnData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    /** Safe to call from any thread. */
    static void queue(String featureId, String dimension, BlockPos origin, UUID entity, long delayMillis) {
        PENDING.add(new Site(featureId, dimension, origin.immutable(), entity, 0L, delayMillis));
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

    /** The entity of a site is gone: start the respawn timer. Returns the site it belonged to, if any. */
    public Optional<Site> markFreed(UUID entity) {
        for (int i = 0; i < sites.size(); i++) {
            Site site = sites.get(i);
            if (entity.equals(site.entity()) && site.freedAt() == 0L) {
                Site freed = new Site(site.featureId(), site.dimension(), site.origin(), site.entity(), System.currentTimeMillis(), site.delayMillis());
                sites.set(i, freed);
                setDirty();
                return Optional.of(freed);
            }
        }
        return Optional.empty();
    }

    /** Sites whose timer ran out; they are removed and re-registered by the feature that regenerates them. */
    public List<Site> takeDue(long now) {
        List<Site> due = new ArrayList<>();
        for (Site site : sites) {
            if (site.freedAt() > 0L && now - site.freedAt() >= site.delayMillis()) {
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
