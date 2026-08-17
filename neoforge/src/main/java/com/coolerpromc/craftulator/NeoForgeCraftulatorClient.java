package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.platform.ServicesClient;
import com.coolerpromc.craftulator.platform.services.client.IRegistryHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(value = Constants.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class NeoForgeCraftulatorClient {
    public NeoForgeCraftulatorClient(IEventBus eventBus) {
        CraftulatorClient.init();
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ServicesClient.REGISTRY.applyKeyMappingRegistrations(new NeoForgeKeyMappingRegistrar(event));
    }

    @SubscribeEvent
    public static void onEndClientTick(ClientTickEvent.Post event) {
        CraftulatorClient.onEndClientTick(Minecraft.getInstance());
    }

    private record NeoForgeKeyMappingRegistrar(RegisterKeyMappingsEvent event) implements IRegistryHelper.KeyMappingRegistrar {
        @Override
        public void registerCategory(String category) {
            // 1.21.1 categories are plain strings on the mapping itself, nothing to register.
        }

        @Override
        public void register(KeyMapping keyMapping) {
            event.register(keyMapping);
        }
    }
}
