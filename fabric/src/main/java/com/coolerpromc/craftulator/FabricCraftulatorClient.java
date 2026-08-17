package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.ServicesClient;
import com.coolerpromc.craftulator.platform.services.client.IRegistryHelper;
import com.coolerpromc.craftulator.platform.util.FabricClientPayloadContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class FabricCraftulatorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CraftulatorClient.init();
        CraftulatorClient.initClientPayloadHandler();

        ServicesClient.REGISTRY.applyClientPayloadReceiverRegistrations(FabricCraftulatorClient::registerPayloadReceiver);
        ServicesClient.REGISTRY.applyKeyMappingRegistrations(new FabricKeyMappingRegistrar());

        ClientTickEvents.END_CLIENT_TICK.register(CraftulatorClient::onEndClientTick);
    }

    private static <T extends HandledCustomPacketPayload> void registerPayloadReceiver(CustomPacketPayload.Type<T> type) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.handle(new FabricClientPayloadContext(context)));
    }

    private static final class FabricKeyMappingRegistrar implements IRegistryHelper.KeyMappingRegistrar {
        @Override
        public void registerCategory(String category) {

        }

        @Override
        public void register(KeyMapping keyMapping) {
            KeyBindingHelper.registerKeyBinding(keyMapping);
        }
    }
}
