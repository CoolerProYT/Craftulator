package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.event.CreativeTabEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class FabricCraftulator implements ModInitializer {
    @Override
    public void onInitialize() {
        Craftulator.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((output) -> CreativeTabEvents.onModify(output::accept, output.getContext()));
    }
}
