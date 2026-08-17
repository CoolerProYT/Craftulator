package com.coolerpromc.craftulator.event;

import com.coolerpromc.craftulator.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class CreativeTabEvents {
    public static void onModify(BiConsumer<ItemStack, CreativeModeTab.TabVisibility> output, CreativeModeTab.ItemDisplayParameters parameters) {
        output.accept(ModItems.CALCULATOR.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
