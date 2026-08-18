package com.coolerpromc.craftulator.item.custom;

import com.coolerpromc.craftulator.network.ClientBoundOpenCalculatorPacket;
import com.coolerpromc.craftulator.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CalculatorItem extends Item {
    public CalculatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer){
            Services.NETWORK.sendToPlayer(serverPlayer, new ClientBoundOpenCalculatorPacket());
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
