package net.yigitguven.chymistry.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.item.ModItems;
import net.yigitguven.chymistry.network.OpenCrucibleUIPayload;

@EventBusSubscriber(modid = Chymistry.MODID, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof InventoryScreen) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                Player player = mc.player;
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();
                ItemStack tongs = ItemStack.EMPTY;

                if (mainHand.is(ModItems.IRON_TONGS.get())) {
                    tongs = mainHand;
                } else if (offHand.is(ModItems.IRON_TONGS.get())) {
                    tongs = offHand;
                }

                if (!tongs.isEmpty()) {
                    CustomData customData = tongs.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                    if (customData.contains("CrucibleType")) {
                        event.setCanceled(true);
                        if (mc.getConnection() != null) {
                            mc.getConnection().send(new OpenCrucibleUIPayload());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(net.neoforged.neoforge.event.entity.player.ItemTooltipEvent event) {
        if (!event.getItemStack().is(ModItems.STORM_POWDER.get())) {
            return;
        }

        Player player = event.getEntity() != null ? event.getEntity() : Minecraft.getInstance().player;
        if (player == null || player.level() == null) {
            return;
        }

        net.minecraft.core.BlockPos targetPos = player.blockPosition();
        boolean inCrucible = false;

        if (Minecraft.getInstance().gui.screen() instanceof net.yigitguven.chymistry.screen.CrucibleScreen crucibleScreen) {
            net.minecraft.world.inventory.Slot hovered = crucibleScreen.getHoveredSlot();
            if (hovered != null && crucibleScreen.getMenu().isCrucibleSlot(hovered)) {
                inCrucible = true;
                if (crucibleScreen.getMenu().blockPos != null) {
                    targetPos = crucibleScreen.getMenu().blockPos;
                }
            }
        }

        net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biomeHolder = player.level().getBiome(targetPos);
        float temp = biomeHolder.value().getBaseTemperature();
        net.minecraft.network.chat.Component biomeName = biomeHolder.unwrapKey()
                .map(key -> net.minecraft.network.chat.Component.translatable(key.identifier().toLanguageKey("biome")))
                .orElse(net.minecraft.network.chat.Component.literal("Unknown"));

        java.util.List<net.minecraft.network.chat.Component> tooltip = event.getToolTip();
        tooltip.add(net.minecraft.network.chat.Component.empty());

        if (inCrucible) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.crucible_biome", biomeName)
                    .withStyle(net.minecraft.ChatFormatting.GOLD));
        } else {
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.current_biome", biomeName)
                    .withStyle(net.minecraft.ChatFormatting.GOLD));
        }

        if (temp > 0.95f) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.temp_hot")
                    .withStyle(net.minecraft.ChatFormatting.AQUA));
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.warning_hot")
                    .withStyle(net.minecraft.ChatFormatting.RED));
        } else if (temp < 0.25f) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.temp_cold")
                    .withStyle(net.minecraft.ChatFormatting.GOLD));
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.warning_cold")
                    .withStyle(net.minecraft.ChatFormatting.AQUA));
        } else {
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.temp_temperate")
                    .withStyle(net.minecraft.ChatFormatting.GREEN));
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.info_temperate")
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
        }

        tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.storm_powder.hint")
                .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
    }
}
