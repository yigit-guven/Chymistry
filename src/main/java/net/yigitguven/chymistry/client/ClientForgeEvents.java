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
import net.neoforged.neoforge.network.PacketDistributor;

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
}
