package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.ServicesClient;
import com.coolerpromc.craftulator.platform.services.client.IRegistryHelper;
import com.coolerpromc.craftulator.platform.util.NeoForgePayloadContext;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@Mod(value = Constants.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class NeoForgeCraftulatorClient {
    public NeoForgeCraftulatorClient(IEventBus eventBus) {
        CraftulatorClient.init();
    }

    @SubscribeEvent
    public static void onRegisterClientPayloadHandlers(RegisterClientPayloadHandlersEvent event) {
        CraftulatorClient.initClientPayloadHandler();
        ServicesClient.REGISTRY.applyClientPayloadReceiverRegistrations(new NeoForgePayloadRegistrar(event)::register);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ServicesClient.REGISTRY.applyKeyMappingRegistrations(new NeoForgeKeyMappingRegistrar(event));
    }

    @SubscribeEvent
    public static void onEndClientTick(ClientTickEvent.Post event) {
        CraftulatorClient.onEndClientTick(Minecraft.getInstance());
    }

    private record NeoForgePayloadRegistrar(RegisterClientPayloadHandlersEvent event) {
        private <T extends HandledCustomPacketPayload> void register(CustomPacketPayload.Type<T> type) {
            event.register(type, (payload, context) -> payload.handle(new NeoForgePayloadContext(context)));
        }
    }

    private record NeoForgeKeyMappingRegistrar(RegisterKeyMappingsEvent event) implements IRegistryHelper.KeyMappingRegistrar {
        @Override
        public void registerCategory(KeyMapping.Category category) {
            event.registerCategory(category);
        }

        @Override
        public void register(KeyMapping keyMapping) {
            event.register(keyMapping);
        }
    }
}
