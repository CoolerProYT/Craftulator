package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.network.CustomPacket;
import com.coolerpromc.craftulator.platform.services.INetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHelper implements INetworkHelper {
    @Override
    public <T extends CustomPacket> void sendToPlayer(ServerPlayer player, T packet) {
        ServerPlayNetworking.send(player, packet.id(), packet.encode(PacketByteBufs.create()));
    }

    @Override
    public <T extends CustomPacket> void sendToServer(T packet) {
        ClientPlayNetworking.send(packet.id(), packet.encode(PacketByteBufs.create()));
    }
}
