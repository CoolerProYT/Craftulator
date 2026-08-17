package com.coolerpromc.craftulator.network;

import com.coolerpromc.craftulator.platform.util.PayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface HandledCustomPacketPayload extends CustomPacketPayload {
    void handle(PayloadContext context);
}