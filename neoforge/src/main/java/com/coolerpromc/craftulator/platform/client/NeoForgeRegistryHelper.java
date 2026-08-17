package com.coolerpromc.craftulator.platform.client;

import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.services.client.IRegistryHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    private final List<ClientPayloadReceiverEntry<?>> clientPayloadReceivers = new ArrayList<>();
    private final List<KeyMapping.Category> keyMappingCategories = new ArrayList<>();
    private final List<KeyMapping> keyMappings = new ArrayList<>();

    @Override
    public <T extends HandledCustomPacketPayload> void registerClientPayloadReceiver(CustomPacketPayload.Type<T> type) {
        this.clientPayloadReceivers.add(new ClientPayloadReceiverEntry<>(type));
    }

    @Override
    public void applyClientPayloadReceiverRegistrations(ClientPayloadReceiverRegistrar registrar) {
        for (ClientPayloadReceiverEntry<?> entry : clientPayloadReceivers) {
            entry.register(registrar);
        }
    }

    @Override
    public void registerKeyMappingCategory(KeyMapping.Category category) {
        this.keyMappingCategories.add(category);
    }

    @Override
    public void registerKeyMapping(KeyMapping keyMapping) {
        this.keyMappings.add(keyMapping);
    }

    @Override
    public void applyKeyMappingRegistrations(KeyMappingRegistrar registrar) {
        for (KeyMapping.Category category : keyMappingCategories) {
            registrar.registerCategory(category);
        }

        for (KeyMapping keyMapping : keyMappings) {
            registrar.register(keyMapping);
        }
    }

    private record ClientPayloadReceiverEntry<T extends HandledCustomPacketPayload>(CustomPacketPayload.Type<T> type){
        private void register(ClientPayloadReceiverRegistrar registrar){
            registrar.register(this.type);
        }
    }
}
