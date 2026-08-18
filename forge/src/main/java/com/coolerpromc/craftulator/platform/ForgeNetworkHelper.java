package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.ForgeCraftulator;
import com.coolerpromc.craftulator.network.CustomPacket;
import com.coolerpromc.craftulator.platform.services.INetworkHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class ForgeNetworkHelper implements INetworkHelper {
    @Override
    public <T extends CustomPacket> void sendToPlayer(ServerPlayer player, T packet) {
        ForgeCraftulator.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @Override
    public <T extends CustomPacket> void sendToServer(T packet) {
        ForgeCraftulator.CHANNEL.sendToServer(packet);
    }
}
