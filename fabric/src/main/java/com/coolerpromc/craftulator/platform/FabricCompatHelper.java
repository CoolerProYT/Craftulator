package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.platform.services.ICompatHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;

public class FabricCompatHelper implements ICompatHelper {
    @Override
    public boolean isWearing(Player player, ItemLike item) {
        return isWearingCommon(player, item);
    }
}
