package com.coolerpromc.craftulator.item;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.item.custom.CalculatorItem;
import com.coolerpromc.craftulator.platform.Services;
import com.coolerpromc.craftulator.platform.util.RegistryHandler;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final RegistryHandler.Items<CalculatorItem> CALCULATOR = Services.REGISTRY.registerItem("calculator", CalculatorItem::new, new Item.Properties().stacksTo(1));

    public static void init(){
        Constants.LOG.info("Registering items.");
    }
}
