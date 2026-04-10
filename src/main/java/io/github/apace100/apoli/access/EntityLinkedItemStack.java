package io.github.apace100.apoli.access;

import net.minecraft.world.entity.Entity;

public interface EntityLinkedItemStack {
    default Entity apoli$getEntity() {
        throw new AssertionError("Implemented via mixin");
    }

    default void apoli$setEntity(Entity entity) {
        throw new AssertionError("Implemented via mixin");
    }
}
