package com.coolerpromc.craftulator.platform.services;

import com.coolerpromc.craftulator.compat.TrinketsCompat;
import com.coolerpromc.craftulator.platform.Services;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;

public interface ICompatHelper {
    boolean isWearing(Player player, ItemLike item);

    String TRINKETS_MODID = "trinkets_updated";

    default boolean isWearingCommon(Player player, ItemLike item) {
        return isTrinketsLoaded() && TrinketsCompat.isWearing(player, item.asItem());
    }

    private boolean isTrinketsLoaded() {
        return Services.PLATFORM.isModLoaded(TRINKETS_MODID);
    }
}
