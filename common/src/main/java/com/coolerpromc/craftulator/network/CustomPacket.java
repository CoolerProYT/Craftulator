package com.coolerpromc.craftulator.network;

import com.coolerpromc.craftulator.platform.util.PayloadContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * This version has no CustomPacketPayload, so packets carry their own id and
 * buffer handling and each loader wires them into its own network layer.
 */
public interface CustomPacket {
    FriendlyByteBuf encode(FriendlyByteBuf buf);
    ResourceLocation id();
    void handle(PayloadContext context);
}
