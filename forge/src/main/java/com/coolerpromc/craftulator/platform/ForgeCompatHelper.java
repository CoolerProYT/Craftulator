package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.compat.CuriosCompat;
import com.coolerpromc.craftulator.platform.services.ICompatHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModList;

public class ForgeCompatHelper implements ICompatHelper {
    private static final String CURIOS_MODID = "curios";

    private Boolean curiosLoaded;

    @Override
    public boolean isWearing(Player player, ItemLike item) {
        return (isCuriosLoaded() && CuriosCompat.isWearing(player, item.asItem())) || isWearingCommon(player, item);
    }

    private boolean isCuriosLoaded() {
        if (this.curiosLoaded == null) {
            this.curiosLoaded = ModList.get() != null && ModList.get().isLoaded(CURIOS_MODID);
        }

        return this.curiosLoaded;
    }
}
