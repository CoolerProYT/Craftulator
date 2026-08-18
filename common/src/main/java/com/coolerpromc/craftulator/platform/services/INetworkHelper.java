package com.coolerpromc.craftulator.platform.services;

import com.coolerpromc.craftulator.network.CustomPacket;
import net.minecraft.server.level.ServerPlayer;

public interface INetworkHelper {
    <T extends CustomPacket> void sendToPlayer(ServerPlayer player, T packet);
    <T extends CustomPacket> void sendToServer(T packet);
}
