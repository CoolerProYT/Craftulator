package com.coolerpromc.craftulator.platform;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.platform.services.IRegistryHelper;
import com.coolerpromc.craftulator.platform.util.RegistryHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class ForgeRegistryHelper implements IRegistryHelper {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Constants.MODID);

    /**
     * Forge hands back a RegistryObject rather than a Holder here, so the handler
     * looks the holder up from the game registry the first time it is asked for.
     */
    @Override
    public <T extends Block> RegistryHandler.Blocks<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties p) {
        BLOCKS.register(name, () -> func.apply(p));
        ResourceKey<Block> key = IRegistryHelper.blockKey(name);

        return () -> BuiltInRegistries.BLOCK.getHolderOrThrow(key);
    }

    @Override
    public <T extends Item> RegistryHandler.Items<T> registerItem(String name, Function<Item.Properties, T> func, Item.Properties p) {
        ITEMS.register(name, () -> func.apply(p));
        ResourceKey<Item> key = IRegistryHelper.itemKey(name);

        return () -> BuiltInRegistries.ITEM.getHolderOrThrow(key);
    }

    @Override
    public RegistryHandler.Sounds registerSoundEvent(String name) {
        SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Constants.id(name)));
        ResourceKey<SoundEvent> key = IRegistryHelper.soundEventKey(name);

        return () -> BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(key);
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        SOUND_EVENTS.register(eventBus);
    }
}
