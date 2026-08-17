package com.coolerpromc.craftulator;

import com.coolerpromc.craftulator.event.CreativeTabEvents;
import com.coolerpromc.craftulator.network.HandledCustomPacketPayload;
import com.coolerpromc.craftulator.platform.NeoForgeRegistryHelper;
import com.coolerpromc.craftulator.platform.Services;
import com.coolerpromc.craftulator.platform.services.IRegistryHelper;
import com.coolerpromc.craftulator.platform.util.NeoForgePayloadContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(Constants.MODID)
@EventBusSubscriber(modid = Constants.MODID)
public class NeoForgeCraftulator {
    public NeoForgeCraftulator(IEventBus eventBus) {
        NeoForgeRegistryHelper.register(eventBus);

        Craftulator.init();
    }

    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)){
            CreativeTabEvents.onModify(event::accept, event.getParameters());
        }
    }

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        Craftulator.initPayloadType();
        PayloadRegistrar registrar = event.registrar("1");
        Services.REGISTRY.applyClientboundPayloadRegistrations(new NeoForgeClientboundRegistrar(registrar));
    }

    private record NeoForgeClientboundRegistrar(PayloadRegistrar registrar) implements IRegistryHelper.ClientboundPayloadRegistrar {
        @Override
        public <T extends HandledCustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
            registrar.playToClient(type, streamCodec, (payload, context) -> payload.handle(new NeoForgePayloadContext(context)));
        }
    }
}
