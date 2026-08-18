package com.coolerpromc.craftulator.platform.client;

import com.coolerpromc.craftulator.platform.services.client.IRegistryHelper;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class ForgeRegistryHelper implements IRegistryHelper {
    private final List<KeyMapping> keyMappings = new ArrayList<>();

    @Override
    public void registerKeyMappingCategory(String category) {

    }

    @Override
    public void registerKeyMapping(KeyMapping keyMapping) {
        this.keyMappings.add(keyMapping);
    }

    @Override
    public void applyKeyMappingRegistrations(KeyMappingRegistrar registrar) {
        for (KeyMapping keyMapping : keyMappings) {
            registrar.register(keyMapping);
        }
    }
}
