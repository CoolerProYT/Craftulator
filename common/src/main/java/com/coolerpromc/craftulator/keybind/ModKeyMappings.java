package com.coolerpromc.craftulator.keybind;

import com.coolerpromc.craftulator.Constants;
import com.coolerpromc.craftulator.item.ModItems;
import com.coolerpromc.craftulator.platform.Services;
import com.coolerpromc.craftulator.platform.ServicesClient;
import com.coolerpromc.craftulator.screen.CalculatorScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final String CATEGORY = registerCategory("craftulator");

    public static final KeyMapping OPEN_CALCULATOR = register("open_calculator", GLFW.GLFW_KEY_C);

    public static void handleInput(Minecraft minecraft) {
        while (OPEN_CALCULATOR.consumeClick()) {
            if (minecraft.player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.CALCULATOR.holder())){
                minecraft.setScreen(new CalculatorScreen());
                return;
            }
            if (minecraft.player.getItemInHand(InteractionHand.OFF_HAND).is(ModItems.CALCULATOR.holder())){
                minecraft.setScreen(new CalculatorScreen());
                return;
            }
            if (Services.COMPAT.isWearing(minecraft.player, ModItems.CALCULATOR)){
                minecraft.setScreen(new CalculatorScreen());
                return;
            }
        }
    }

    private static String registerCategory(String name) {
        return "key.categories." + name;
    }

    private static KeyMapping register(String name, int defaultKey) {
        KeyMapping keyMapping = new KeyMapping("key." + Constants.MODID + "." + name, InputConstants.Type.KEYSYM, defaultKey, CATEGORY);
        ServicesClient.REGISTRY.registerKeyMapping(keyMapping);

        return keyMapping;
    }

    public static void init() {
        Constants.LOG.info("Registering key mappings.");
    }
}
