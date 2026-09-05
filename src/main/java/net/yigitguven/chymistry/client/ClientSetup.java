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

    @SubscribeEvent
    public static void registerEntityRenderers(net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(net.yigitguven.chymistry.entity.ModEntities.THROWN_EXPLOSIVE_LIQUID.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        event.registerEntityRenderer(net.yigitguven.chymistry.entity.ModEntities.THROWN_DYNAMITE.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(java.util.List.of(new net.minecraft.client.color.block.BlockTintSource() {
            @Override
            public int color(net.minecraft.world.level.block.state.BlockState state) {
                return -1;
            }

            @Override
            public int colorInWorld(net.minecraft.world.level.block.state.BlockState state, net.minecraft.client.renderer.block.BlockAndTintGetter level, net.minecraft.core.BlockPos pos) {
                if (level != null && pos != null) {
                    net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof net.yigitguven.chymistry.block.PlacedBottleBlockEntity bottleBE) {
                        net.minecraft.world.item.ItemStack stored = bottleBE.getStoredItem();
                        if (!stored.isEmpty()) {
                            if (stored.has(net.minecraft.core.component.DataComponents.POTION_CONTENTS)) {
                                net.minecraft.world.item.alchemy.PotionContents contents = stored.get(net.minecraft.core.component.DataComponents.POTION_CONTENTS);
                                if (contents != null) {
                                    return net.minecraft.util.ARGB.opaque(contents.getColor());
                                }
                            }
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.ELIXIR_OF_VITRIOL.get())) return 0xFF8B008B;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.BERRY_ESSENCE.get())) return 0xFFC71585;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.ALCOHOL_BOTTLE.get())) return 0xFFDFE9F3;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.CREOSOTE_OIL.get())) return 0xFF2A1D15;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.DEFOLIANT_POTION.get())) return 0xFF44B029;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.EXPLOSIVE_LIQUID.get())) return 0xFFEB5B28;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.GOLD_SOLVENT_BOTTLE.get())) return 0xFFF4D03F;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.HYDROCHLORIC_ACID_BOTTLE.get())) return 0xFFA3E048;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.NITRIC_ACID_BOTTLE.get())) return 0xFFE67E22;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.SULFURIC_ACID_BOTTLE.get())) return 0xFFF1C40F;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.VIGOR_POTION.get())) return 0xFF00C4B4;
                            if (stored.is(net.yigitguven.chymistry.item.ModItems.IMMUNITY_POTION.get())) return 0xFF70E8E8;

                            return 0xFF385DC6;
                        }
                    }
                }
                return -1;
            }
        }), net.yigitguven.chymistry.block.ModBlocks.PLACED_BOTTLE.get(),
           net.yigitguven.chymistry.block.ModBlocks.PLACED_TINTED_BOTTLE.get(),
           net.yigitguven.chymistry.block.ModBlocks.PLACED_REINFORCED_BOTTLE.get());
    }
}
