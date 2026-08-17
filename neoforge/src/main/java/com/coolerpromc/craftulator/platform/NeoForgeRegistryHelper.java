package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.services.IRegistryHelper;
import com.coolerpromc.craftulator.platform.util.RegistryHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Constants.MODID);

    private final List<ClientboundPayloadEntry<?>> clientboundPayloads = new ArrayList<>();

    @Override
    public <T extends Block> RegistryHandler.Blocks<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties p) {
        DeferredBlock<T> deferredBlock = BLOCKS.registerBlock(name, func, () -> p);
        return () -> deferredBlock;
    }

    @Override
    public <T extends Item> RegistryHandler.Items<T> registerItem(String name, Function<Item.Properties, T> func, Item.Properties p) {
        DeferredItem<T> deferredItem = ITEMS.registerItem(name, func, () -> p);
        return () -> deferredItem;
    }

    @Override
    public RegistryHandler.Sounds registerSoundEvent(String name) {
        DeferredHolder<SoundEvent, SoundEvent> deferredSound = SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Constants.id(name)));
        return () -> deferredSound;
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

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        SOUND_EVENTS.register(eventBus);
    }
}