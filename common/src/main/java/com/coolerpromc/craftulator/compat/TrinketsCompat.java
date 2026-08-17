package com.coolerpromc.craftulator.compat;

import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public final class TrinketsCompat {
    private TrinketsCompat() {
    }

    public static boolean isWearing(Player player, Item item) {
        return TrinketsApi.getAttachment(player)
                .findFirst(stack -> stack.is(item))
                .isPresent();
    }
}
