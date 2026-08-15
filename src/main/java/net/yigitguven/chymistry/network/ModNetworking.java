package net.yigitguven.chymistry.network;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.yigitguven.chymistry.Chymistry;

public class ModNetworking {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Chymistry.MODID);
        registrar.playToServer(
                MeshButtonPressedPayload.TYPE,
                MeshButtonPressedPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        if (context.player().containerMenu instanceof net.yigitguven.chymistry.menu.MortarMenu menu) {
                            menu.handleMeshPress();
                        }
                    });
                }
        );
    }
}
