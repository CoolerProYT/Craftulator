package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.compat.TrinketsCompat;
import com.coolerpromc.craftulator.platform.services.ICompatHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;

public class FabricCompatHelper implements ICompatHelper {
    String TRINKETS_MODID = "trinkets";

    @Override
    public boolean isWearing(Player player, ItemLike item) {
        return (isTrinketsLoaded() && TrinketsCompat.isWearing(player, item.asItem())) || isWearingCommon(player, item);
    }

    private boolean isTrinketsLoaded() {
        return Services.PLATFORM.isModLoaded(TRINKETS_MODID);
    }
}
