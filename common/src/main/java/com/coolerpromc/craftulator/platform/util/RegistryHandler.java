package com.coolerpromc.craftulator.platform.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface RegistryHandler<R, T extends R> extends Supplier<T> {
    Holder<R> holder();

    default ResourceKey<R> key(){
        return holder().unwrapKey().orElse(null);
    }

    default ResourceLocation id(){
        return key().location();
    }

    @Override
    default T get(){
        return (T) holder().value();
    }

    interface Items<I extends Item> extends RegistryHandler<Item, I>, ItemLike {
        @Override
        default @NotNull Item asItem(){
            return get();
        }

        default ItemStack toStack(){
            return asItem().getDefaultInstance();
        }
    }

    interface Blocks<B extends Block> extends RegistryHandler<Block, B>, ItemLike{
        @Override
        default @NotNull Item asItem(){
            return get().asItem();
        }

        default ItemStack toStack(){
            return asItem().getDefaultInstance();
        }
    }

    interface Entities<E extends Entity> extends RegistryHandler<EntityType<?>, EntityType<E>>{
    }

    interface Sounds extends RegistryHandler<SoundEvent, SoundEvent>{
    }

    interface Components<T> extends RegistryHandler<DataComponentType<?>, DataComponentType<T>>{
    }
}