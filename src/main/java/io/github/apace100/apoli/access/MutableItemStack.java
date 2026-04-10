package io.github.apace100.apoli.access;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface MutableItemStack {

    default void apoli$setItem(Item item) {
        throw new AssertionError("Implemented via mixin");
    }

    default void apoli$setFrom(ItemStack stack) {
        throw new AssertionError("Implemented via mixin");
    }
}
