package com.coolerpromc.craftulator.compat;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public final class AccessoriesCompat {
    public static boolean isWearing(Player player, Item item) {
        return AccessoriesCapability.getOptionally(player).map(capability -> capability.isEquipped(stack -> stack.is(item))).orElse(false);
    }
}
