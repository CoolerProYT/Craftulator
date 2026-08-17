package com.coolerpromc.craftulator.platform.services;

import com.coolerpromc.craftulator.compat.AccessoriesCompat;
import com.coolerpromc.craftulator.platform.Services;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;

public interface ICompatHelper {
    boolean isWearing(Player player, ItemLike item);

    String ACCESSORIES_MODID = "accessories";

    default boolean isWearingCommon(Player player, ItemLike item) {
        return isAccessoriesLoaded() && AccessoriesCompat.isWearing(player, item.asItem());
    }

    private boolean isAccessoriesLoaded() {
        return Services.PLATFORM.isModLoaded(ACCESSORIES_MODID);
    }
}
