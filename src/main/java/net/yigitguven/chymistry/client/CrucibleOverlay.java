package net.yigitguven.chymistry.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.yigitguven.chymistry.block.CrucibleBlock;
import net.yigitguven.chymistry.block.CrucibleBlockEntity;
import net.yigitguven.chymistry.item.ModItems;

public class CrucibleOverlay implements GuiLayer {

    public static final CrucibleOverlay INSTANCE = new CrucibleOverlay();

    private float fadeProgress = 0f;

    // Cached state for smooth fade-out when looking away
    private float cachedHeat = 0f;
    private int cachedMaxHeat = 100;
    private int cachedMinHeat = -100;
    private ItemStack cachedCrucibleStack = ItemStack.EMPTY;

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        boolean holdingThermometer = player.getMainHandItem().is(ModItems.THERMOMETER.get()) ||
                                     player.getOffhandItem().is(ModItems.THERMOMETER.get());

        boolean validTarget = false;

        if (holdingThermometer) {
            HitResult hit = mc.hitResult;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockState state = mc.level.getBlockState(blockHit.getBlockPos());
                if (mc.level.getBlockEntity(blockHit.getBlockPos()) instanceof CrucibleBlockEntity crucible && state.getBlock() instanceof CrucibleBlock crucibleBlock) {
                    cachedHeat = crucible.currentHeat;
                    cachedMaxHeat = crucibleBlock.getMaxHeat();
                    cachedMinHeat = crucibleBlock.getMinHeat();
                    cachedCrucibleStack = new ItemStack(state.getBlock());
                    validTarget = true;
                }
            }
        }

        float deltaTicks = deltaTracker.getGameTimeDeltaTicks();
        // Fade in/out takes ~5 ticks (0.25s)
        float fadeSpeed = 0.2f * deltaTicks;
        
        if (validTarget) {
            fadeProgress = Math.min(1f, fadeProgress + fadeSpeed);
        } else {
            fadeProgress = Math.max(0f, fadeProgress - fadeSpeed);
        }

        if (fadeProgress <= 0f || cachedCrucibleStack.isEmpty()) {
            return;
        }

        int heatVal = Math.round(cachedHeat);
        
        int baseColor = 0xAAAAAA;
        ItemStack iconStack = new ItemStack(Items.CLAY_BALL);
        String ratioStr = String.valueOf(heatVal);

        // Lerp color based on heat intensity
        if (cachedHeat > 0) {
            iconStack = new ItemStack(Items.CAMPFIRE);
            ratioStr = heatVal + " / " + cachedMaxHeat;
            // Blend from Gray (0xAAAAAA) to Red (0xFF5555) based on percentage
            float percentage = Math.min(1f, cachedHeat / Math.max(1f, cachedMaxHeat));
            int r = (int)(0xAA + (0xFF - 0xAA) * percentage);
            int g = (int)(0xAA - (0xAA - 0x55) * percentage);
            int b = (int)(0xAA - (0xAA - 0x55) * percentage);
            baseColor = (r << 16) | (g << 8) | b;
        } else if (cachedHeat < 0) {
            iconStack = new ItemStack(Items.SNOWBALL);
            ratioStr = heatVal + " / " + cachedMinHeat;
            // Blend from Gray (0xAAAAAA) to Aqua (0x55FFFF)
            float percentage = Math.min(1f, cachedHeat / Math.min(-1f, cachedMinHeat));
            int r = (int)(0xAA - (0xAA - 0x55) * percentage);
            int g = (int)(0xAA + (0xFF - 0xAA) * percentage);
            int b = (int)(0xAA + (0xFF - 0xAA) * percentage);
            baseColor = (r << 16) | (g << 8) | b;
        } else {
            ratioStr = "0";
        }

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        // Calculate layout
        Component crucibleName = cachedCrucibleStack.getHoverName();
        final int finalBaseColor = baseColor;
        Component heatText = Component.literal(ratioStr).withStyle(style -> style.withColor(finalBaseColor));

        int crucibleNameWidth = mc.font.width(crucibleName);
        int heatTextWidth = mc.font.width(heatText);
        
        int textXOffset = 22;
        int maxTextWidth = Math.max(crucibleNameWidth, heatTextWidth);
        int totalWidth = textXOffset + maxTextWidth + 8;
        int totalHeight = 38;
        
        // Center the HUD overlay but offset it so it doesn't block crosshair
        int startX = screenWidth / 2 + 10;
        int startY = screenHeight / 2 - totalHeight / 2;

        // Apply alpha to background
        int alpha = (int)(fadeProgress * 255);
        int bgAlpha = (int)(fadeProgress * 0.5f * 255); // 50% max opacity for typical Minecraft HUD
        
        // Draw standard HUD background (semi-transparent black)
        int bgColor = (bgAlpha << 24) | 0x101010;
        guiGraphics.fill(startX, startY, startX + totalWidth, startY + totalHeight, bgColor);
        
        // Draw border
        int borderColor = (alpha << 24) | 0x2A2A2A;
        guiGraphics.fill(startX, startY, startX + totalWidth, startY + 1, borderColor); // Top
        guiGraphics.fill(startX, startY + totalHeight - 1, startX + totalWidth, startY + totalHeight, borderColor); // Bottom
        guiGraphics.fill(startX, startY, startX + 1, startY + totalHeight, borderColor); // Left
        guiGraphics.fill(startX + totalWidth - 1, startY, startX + totalWidth, startY + totalHeight, borderColor); // Right

        int textColor = (alpha << 24) | 0xFFFFFF; // White for name
        int textHeatColor = (alpha << 24) | baseColor;

        // Draw Crucible Info
        // Only draw items if fade is past a threshold to avoid sudden item popping if items don't fade properly
        if (fadeProgress > 0.1f) {
            guiGraphics.item(cachedCrucibleStack, startX + 4, startY + 4);
            guiGraphics.item(iconStack, startX + 4, startY + 20);
        }

        guiGraphics.text(mc.font, crucibleName, startX + textXOffset, startY + 8, textColor);
        guiGraphics.text(mc.font, heatText, startX + textXOffset, startY + 24, textHeatColor);
    }
}
