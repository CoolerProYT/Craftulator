package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.network.ClientBoundOpenCalculatorPacket;
import com.coolerpromc.craftulator.platform.ServicesClient;
import com.coolerpromc.craftulator.platform.services.client.IRegistryHelper;
import com.coolerpromc.craftulator.platform.util.FabricClientPayloadContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;

public class FabricCraftulatorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CraftulatorClient.init();

        ClientPlayNetworking.registerGlobalReceiver(ClientBoundOpenCalculatorPacket.TYPE, (client, handler, buf, sender) ->
                ClientBoundOpenCalculatorPacket.decode(buf).handle(new FabricClientPayloadContext(client, handler)));

        ServicesClient.REGISTRY.applyKeyMappingRegistrations(new FabricKeyMappingRegistrar());

        ClientTickEvents.END_CLIENT_TICK.register(CraftulatorClient::onEndClientTick);
    }

    private static final class FabricKeyMappingRegistrar implements IRegistryHelper.KeyMappingRegistrar {
        @Override
        public void registerCategory(String category) {
            // Categories are plain strings on the mapping itself, nothing to register.
        }

        @Override
        public void register(KeyMapping keyMapping) {
            KeyBindingHelper.registerKeyBinding(keyMapping);
        }
    }
}
