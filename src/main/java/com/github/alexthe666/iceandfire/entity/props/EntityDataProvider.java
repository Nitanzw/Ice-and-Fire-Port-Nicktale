/*
 * Ice and Fire NeoForge port
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;

/** Persistent living-entity state, backed by NeoForge data attachments. */
public final class EntityDataProvider {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, IceAndFire.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<EntityData>> ENTITY_DATA =
        ATTACHMENTS.register("entity_data", () -> AttachmentType.serializable(EntityData::new).build());

    private EntityDataProvider() {
    }

    /** Retains the Optional-based call site API while creating the attachment lazily for living entities. */
    public static Optional<EntityData> getCapability(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return Optional.of(livingEntity.getData(ENTITY_DATA));
        }
        return Optional.empty();
    }
}
