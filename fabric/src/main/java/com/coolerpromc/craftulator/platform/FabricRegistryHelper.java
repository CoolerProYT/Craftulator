package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.services.IRegistryHelper;
import com.coolerpromc.craftulator.platform.util.RegistryHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FabricRegistryHelper implements IRegistryHelper {
    private final List<ClientboundPayloadEntry<?>> clientboundPayloads = new ArrayList<>();

    @Override
    public <T extends Block> RegistryHandler.Blocks<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties p) {
        ResourceKey<Block> key = IRegistryHelper.blockKey(name);
        Holder<Block> holder = Registry.registerForHolder(BuiltInRegistries.BLOCK, key, func.apply(p.setId(key)));

        return () -> holder;
    }

    @Override
    public <T extends Item> RegistryHandler.Items<T> registerItem(String name, Function<Item.Properties, T> func, Item.Properties p) {
        ResourceKey<Item> key = IRegistryHelper.itemKey(name);
        Holder<Item> holder = Registry.registerForHolder(BuiltInRegistries.ITEM, key, func.apply(p.setId(key)));

        return () -> holder;
    }

    @Override
    public RegistryHandler.Sounds registerSoundEvent(String name) {
        ResourceKey<SoundEvent> key = IRegistryHelper.soundEventKey(name);
        Holder<SoundEvent> holder = Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, key, SoundEvent.createVariableRangeEvent(key.identifier()));

        return () -> holder;
    }

    @Override
    public <T extends HandledCustomPacketPayload> void registerClientboundPayload(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        this.clientboundPayloads.add(new ClientboundPayloadEntry<>(type, streamCodec));
    }

    @Override
    public void applyClientboundPayloadRegistrations(ClientboundPayloadRegistrar registrar) {
        for (ClientboundPayloadEntry<?> entry : clientboundPayloads) {
            entry.register(registrar);
        }
    }

    private record ClientboundPayloadEntry<T extends HandledCustomPacketPayload>(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec){
        private void register(ClientboundPayloadRegistrar registrar){
            registrar.register(this.type, this.streamCodec);
        }
    }
}