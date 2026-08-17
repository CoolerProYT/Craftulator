package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.keybind.ModKeyMappings;
import com.coolerpromc.craftulator.network.ClientBoundOpenCalculatorPayload;
import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.ServicesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class CraftulatorClient {
    public static void init(){
        ModKeyMappings.init();
    }

    public static void initClientPayloadHandler(){
        registerClientPayloadReceiver(ClientBoundOpenCalculatorPayload.TYPE);
    }

    public static void onEndClientTick(Minecraft minecraft){
        ModKeyMappings.handleInput(minecraft);
    }

    private static <T extends HandledCustomPacketPayload> void registerClientPayloadReceiver(CustomPacketPayload.Type<T> type){
        ServicesClient.REGISTRY.registerClientPayloadReceiver(type);
    }
}
