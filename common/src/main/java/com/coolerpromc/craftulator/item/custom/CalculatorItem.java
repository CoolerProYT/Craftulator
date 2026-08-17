package com.coolerpromc.craftulator.item.custom;

import com.coolerpromc.craftulator.network.ClientBoundOpenCalculatorPayload;
import com.coolerpromc.craftulator.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class CalculatorItem extends Item {
    public CalculatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer){
            Services.NETWORK.sendToPlayer(serverPlayer, new ClientBoundOpenCalculatorPayload());
        }

        return InteractionResult.SUCCESS;
    }
}
