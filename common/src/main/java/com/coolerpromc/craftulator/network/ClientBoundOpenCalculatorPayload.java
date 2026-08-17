package com.coolerpromc.craftulator.network;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.platform.util.PayloadContext;
import com.coolerpromc.craftulator.screen.CalculatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundOpenCalculatorPayload() implements HandledCustomPacketPayload{
    public static final Type<ClientBoundOpenCalculatorPayload> TYPE = new Type<>(Constants.id("open_calculator"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundOpenCalculatorPayload> STREAM_CODEC = StreamCodec.unit(new ClientBoundOpenCalculatorPayload());

    @Override
    public void handle(PayloadContext context) {
        context.execute(() -> Minecraft.getInstance().setScreen(new CalculatorScreen()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
