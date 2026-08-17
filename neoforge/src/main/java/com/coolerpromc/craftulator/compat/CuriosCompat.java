package com.coolerpromc.craftulator.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosCompat {
    private CuriosCompat() {
    }

    public static boolean isWearing(Player player, Item item) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(inventory -> inventory.findFirstCurio(item))
                .isPresent();
    }
}
