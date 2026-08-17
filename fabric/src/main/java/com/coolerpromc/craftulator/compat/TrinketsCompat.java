package com.coolerpromc.craftulator.compat;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public final class TrinketsCompat {
    private TrinketsCompat() {
    }

    public static boolean isWearing(Player player, Item item) {
        return TrinketsApi.getTrinketComponent(player).map(trinketComponent -> trinketComponent.isEquipped(stack -> stack.is(item))).orElse(false);
    }
}
