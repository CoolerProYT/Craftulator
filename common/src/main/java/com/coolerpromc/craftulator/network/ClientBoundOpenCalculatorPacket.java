package com.coolerpromc.craftulator.network;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.platform.util.PayloadContext;
import com.coolerpromc.craftulator.screen.CalculatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundOpenCalculatorPacket() implements CustomPacket {
    public static final ResourceLocation TYPE = Constants.id("open_calculator");

    public static ClientBoundOpenCalculatorPacket decode(FriendlyByteBuf buf) {
        return new ClientBoundOpenCalculatorPacket();
    }

    @Override
    public FriendlyByteBuf encode(FriendlyByteBuf buf) {
        return buf;
    }

    @Override
    public ResourceLocation id() {
        return TYPE;
    }

    @Override
    public void handle(PayloadContext context) {
        context.execute(() -> Minecraft.getInstance().setScreen(new CalculatorScreen()));
    }
}
