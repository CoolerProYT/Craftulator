package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.event.CreativeTabEvents;
import com.coolerpromc.craftulator.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.world.item.CreativeModeTabs;

public class FabricCraftulator implements ModInitializer {
    @Override
    public void onInitialize() {
        Craftulator.init();
        Craftulator.initPayloadType();

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((output) -> CreativeTabEvents.onModify(output::accept, output.getContext()));

        Services.REGISTRY.applyClientboundPayloadRegistrations(PayloadTypeRegistry.clientboundPlay()::register);
    }
}
