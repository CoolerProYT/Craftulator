package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.event.CreativeTabEvents;
import com.coolerpromc.craftulator.platform.NeoForgeRegistryHelper;
import com.coolerpromc.craftulator.platform.Services;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(Constants.MODID)
@EventBusSubscriber(modid = Constants.MODID)
public class NeoForgeCraftulator {
    public NeoForgeCraftulator(IEventBus eventBus) {
        NeoForgeRegistryHelper.register(eventBus);

        Craftulator.init();
    }

    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)){
            CreativeTabEvents.onModify(event::accept, event.getParameters());
        }
    }

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        Craftulator.initPayloadType();
        PayloadRegistrar registrar = event.registrar("1");
        Services.REGISTRY.applyClientboundPayloadRegistrations(registrar::playToClient);
    }

}