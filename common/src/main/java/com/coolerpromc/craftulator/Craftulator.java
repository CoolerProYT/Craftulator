package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.item.ModItems;
import com.coolerpromc.craftulator.network.ClientBoundOpenCalculatorPayload;
import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.Services;
import com.coolerpromc.craftulator.sound.ModSounds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class Craftulator {
    public static void init() {
        ModItems.init();
        ModSounds.init();
    }

    public static void initPayloadType(){
        registerClientboundPayload(ClientBoundOpenCalculatorPayload.TYPE, ClientBoundOpenCalculatorPayload.STREAM_CODEC);
    }

    private static <T extends HandledCustomPacketPayload> void registerClientboundPayload(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec){
        Services.REGISTRY.registerClientboundPayload(type, streamCodec);
    }
}