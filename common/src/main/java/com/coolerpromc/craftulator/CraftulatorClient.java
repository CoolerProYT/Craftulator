package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.keybind.ModKeyMappings;
import net.minecraft.client.Minecraft;

public class CraftulatorClient {
    public static void init(){
        ModKeyMappings.init();
    }

    public static void onEndClientTick(Minecraft minecraft){
        ModKeyMappings.handleInput(minecraft);
    }
}
