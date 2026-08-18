package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.event.CreativeTabEvents;
import com.coolerpromc.craftulator.network.ClientBoundOpenCalculatorPacket;
import com.coolerpromc.craftulator.platform.ForgeRegistryHelper;
import com.coolerpromc.craftulator.platform.util.ForgeClientPayloadContext;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(Constants.MODID)
public class ForgeCraftulator {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            Constants.id("main"),
            () -> PROTOCOL_VERSION,
            s -> true,
            s -> true
    );

    public ForgeCraftulator() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeRegistryHelper.register(eventBus);
        eventBus.addListener(ForgeCraftulator::onBuildCreativeModeTabContents);

        Craftulator.init();

        registerPackets();
    }

    /**
     * This version has no payload registry, so each packet gets an index on the
     * mod channel together with the handler that receives it.
     */
    private static void registerPackets() {
        CHANNEL.registerMessage(0, ClientBoundOpenCalculatorPacket.class,
                (packet, buf) -> packet.encode(buf),
                ClientBoundOpenCalculatorPacket::decode,
                (packet, context) -> {
                    packet.handle(new ForgeClientPayloadContext(context.get()));
                    context.get().setPacketHandled(true);
                });
    }

    private static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            CreativeTabEvents.onModify(event::accept, event.getParameters());
        }
    }
}
