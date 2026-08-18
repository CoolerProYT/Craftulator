package com.coolerpromc.craftulator.platform.services.client;

import net.minecraft.client.KeyMapping;

public interface IRegistryHelper {
    void registerKeyMappingCategory(String category);
    void registerKeyMapping(KeyMapping keyMapping);
    void applyKeyMappingRegistrations(KeyMappingRegistrar registrar);

    interface KeyMappingRegistrar{
        void registerCategory(String category);
        void register(KeyMapping keyMapping);
    }
}
