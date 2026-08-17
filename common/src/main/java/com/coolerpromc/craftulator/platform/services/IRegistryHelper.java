package com.coolerpromc.craftulator.platform.services;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.util.RegistryHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public interface IRegistryHelper {
    default <T extends Block> RegistryHandler.Blocks<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func) {
        return registerBlock(name, func, BlockBehaviour.Properties.of());
    }
    <T extends Block> RegistryHandler.Blocks<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties p);

    default <T extends Item> RegistryHandler.Items<T> registerItem(String name, Function<Item.Properties, T> func) {
        return registerItem(name, func, new Item.Properties());
    }
    <T extends Item> RegistryHandler.Items<T> registerItem(String name, Function<Item.Properties, T> func, Item.Properties p);

    RegistryHandler.Sounds registerSoundEvent(String name);

    static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Constants.id(name));
    }
    static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
    }
    static ResourceKey<SoundEvent> soundEventKey(String name) {
        return ResourceKey.create(Registries.SOUND_EVENT, Constants.id(name));
    }

    <T extends HandledCustomPacketPayload> void registerClientboundPayload(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec);
    void applyClientboundPayloadRegistrations(ClientboundPayloadRegistrar registrar);

    interface ClientboundPayloadRegistrar {
        <T extends HandledCustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec);
    }
}