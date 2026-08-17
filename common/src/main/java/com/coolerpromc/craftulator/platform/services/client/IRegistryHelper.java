package com.coolerpromc.craftulator.platform.services.client;

import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface IRegistryHelper {
    <T extends HandledCustomPacketPayload> void registerClientPayloadReceiver(CustomPacketPayload.Type<T> type);
    void applyClientPayloadReceiverRegistrations(ClientPayloadReceiverRegistrar registrar);

    interface ClientPayloadReceiverRegistrar{
        <T extends HandledCustomPacketPayload> void register(CustomPacketPayload.Type<T> type);
    }

    void registerKeyMappingCategory(KeyMapping.Category category);
    void registerKeyMapping(KeyMapping keyMapping);
    void applyKeyMappingRegistrations(KeyMappingRegistrar registrar);

    interface KeyMappingRegistrar{
        void registerCategory(KeyMapping.Category category);
        void register(KeyMapping keyMapping);
    }
}
