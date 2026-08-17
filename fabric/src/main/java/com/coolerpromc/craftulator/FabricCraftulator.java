package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.event.CreativeTabEvents;
import com.coolerpromc.craftulator.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.world.item.CreativeModeTabs;

public class FabricCraftulator implements ModInitializer {
    @Override
    public void onInitialize() {
        Craftulator.init();
        Craftulator.initPayloadType();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((output) -> CreativeTabEvents.onModify(output::accept, output.getContext()));

        Services.REGISTRY.applyClientboundPayloadRegistrations(PayloadTypeRegistry.playS2C()::register);
    }
}
