package com.coolerpromc.craftulator.platform.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record FabricClientPayloadContext(Minecraft client, ClientPacketListener listener) implements PayloadContext {
    @Override
    public Player player() {
        return client.player;
    }

    @Override
    public Level level() {
        return client.level;
    }

    @Override
    public void execute(Runnable runnable) {
        client.execute(runnable);
    }

    @Override
    public void disconnect(Component reason) {
        listener.getConnection().disconnect(reason);
    }
}
