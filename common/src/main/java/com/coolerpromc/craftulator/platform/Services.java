package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.platform.services.ICompatHelper;
import com.coolerpromc.craftulator.platform.services.INetworkHelper;
import com.coolerpromc.craftulator.platform.services.IPlatformHelper;
import com.coolerpromc.craftulator.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    public static final INetworkHelper NETWORK = load(INetworkHelper.class);
    public static final ICompatHelper COMPAT = load(ICompatHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}