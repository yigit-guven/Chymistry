package net.yigitguven.chymistry.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.minecraft.resources.Identifier;
import net.yigitguven.chymistry.Chymistry;

@EventBusSubscriber(modid = Chymistry.MODID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Chymistry.MODID, "thermometer_overlay"), CrucibleOverlay.INSTANCE);
    }
}
