package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.platform.ServicesClient;
import com.coolerpromc.craftulator.platform.services.client.IRegistryHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeCraftulatorClient {
    @SubscribeEvent
    public static void onEndClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            CraftulatorClient.onEndClientTick(Minecraft.getInstance());
        }
    }

    @Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            // This event fires while the client is still starting up, ahead of the
            // setup events, so the mappings are created here rather than earlier.
            CraftulatorClient.init();
            ServicesClient.REGISTRY.applyKeyMappingRegistrations(new ForgeKeyMappingRegistrar(event));
        }
    }

    private record ForgeKeyMappingRegistrar(RegisterKeyMappingsEvent event) implements IRegistryHelper.KeyMappingRegistrar {
        @Override
        public void registerCategory(String category) {
            // Categories are plain strings on the mapping itself, nothing to register.
        }

        @Override
        public void register(KeyMapping keyMapping) {
            event.register(keyMapping);
        }
    }
}
