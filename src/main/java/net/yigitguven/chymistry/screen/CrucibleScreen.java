package net.yigitguven.chymistry.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.menu.CrucibleMenu;

public class CrucibleScreen extends AbstractContainerScreen<CrucibleMenu> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/crucible_gui.png");
    private static final Identifier ARROW_FULL =
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/crucible_arrow_full.png");
    private static final Identifier FIRE_EMPTY =
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/empty_fire.png");
    private static final Identifier FIRE_FULL =
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/full_fire.png");
    private static final Identifier SNOW_EMPTY =
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/empty_snow.png");
    private static final Identifier SNOW_FULL =
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/full_snow.png");

    private static final Identifier CRUCIBLE_SLOT_ICON =
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/cruciblecontainersloticon.png");

    private static final int HEAT_WIDTH = 14;
    private static final int HEAT_HEIGHT = 14;
    
    // Progress arrow settings
    private static final int ARROW_WIDTH = 109; 
    private static final int ARROW_HEIGHT = 14;

    // gap info in absolute screen offsets
    private static final int GAP_START_X = 67;
    private static final int GAP_END_X = 90;

    public CrucibleScreen(CrucibleMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor pGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.extractBackground(pGraphics, pMouseX, pMouseY, pPartialTick);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        pGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0f, 0f, this.imageWidth, this.imageHeight,
                this.imageWidth, this.imageHeight);

        renderHeatIndicator(pGraphics, x, y);
        renderProgressArrow(pGraphics, x, y);
        
        if (!menu.getSlot(4).hasItem()) {
            pGraphics.blit(RenderPipelines.GUI_TEXTURED, CRUCIBLE_SLOT_ICON, x + 96, y + 52, 0f, 0f, 18, 18, 18, 18);
        }
        
        int heatIndicatorX = x + 98;
        int heatIndicatorY = y + 37;
        
        if (pMouseX >= heatIndicatorX && pMouseX < heatIndicatorX + HEAT_WIDTH &&
            pMouseY >= heatIndicatorY && pMouseY < heatIndicatorY + HEAT_HEIGHT) {
            
            boolean hasThermometer = false;
            net.minecraft.client.player.LocalPlayer player = net.minecraft.client.Minecraft.getInstance().player;
            if (player != null) {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    if (player.getInventory().getItem(i).getItem() == net.yigitguven.chymistry.item.ModItems.THERMOMETER.get()) {
                        hasThermometer = true;
                        break;
                    }
                }
            }
            
            if (hasThermometer) {
                float currentHeat = menu.getCurrentHeat();
                float maxHeat = menu.getMaxHeat();
                float minHeat = menu.getMinHeat();
                
                Component line1;
                Component line2;

                if (currentHeat >= 0) {
                    line1 = Component.translatable("tooltip.chymistry.iron_tongs.heat", Component.literal(String.format("%.1f", currentHeat)).withStyle(net.minecraft.ChatFormatting.RED)).withStyle(net.minecraft.ChatFormatting.GRAY);
                    line2 = Component.translatable("tooltip.chymistry.crucible.max_heat", String.format("%.1f", maxHeat)).withStyle(net.minecraft.ChatFormatting.DARK_GRAY);
                } else {
                    line1 = Component.translatable("tooltip.chymistry.iron_tongs.heat", Component.literal(String.format("%.1f", currentHeat)).withStyle(net.minecraft.ChatFormatting.AQUA)).withStyle(net.minecraft.ChatFormatting.GRAY);
                    line2 = Component.translatable("tooltip.chymistry.crucible.min_heat", String.format("%.1f", minHeat)).withStyle(net.minecraft.ChatFormatting.DARK_GRAY);
                }
                
                java.util.List<Component> tooltip = new java.util.ArrayList<>();
                tooltip.add(line1);
                tooltip.add(line2);
                
                pGraphics.setComponentTooltipForNextFrame(this.font, tooltip, pMouseX, pMouseY);
            }
        }
    }

    private void renderHeatIndicator(GuiGraphicsExtractor pGraphics, int x, int y) {
        float currentHeat = menu.getCurrentHeat();
        float maxHeat = menu.getMaxHeat(); 
        float minHeat = menu.getMinHeat();
        
        int heatIndicatorX = x + 98;
        int heatIndicatorY = y + 37;

        if (currentHeat >= 0) {
            pGraphics.blit(RenderPipelines.GUI_TEXTURED, FIRE_EMPTY, heatIndicatorX, heatIndicatorY, 0f, 0f, HEAT_WIDTH, HEAT_HEIGHT, HEAT_WIDTH, HEAT_HEIGHT);
            
            if (currentHeat > 0) {
                float fillRatio = Math.min(1.0f, currentHeat / maxHeat);
                int steps = (int) Math.ceil((HEAT_HEIGHT - 1) * fillRatio);
                
                if (steps > 0) {
                    int fillHeight = steps + 1;
                    pGraphics.blit(RenderPipelines.GUI_TEXTURED, FIRE_FULL, heatIndicatorX, heatIndicatorY + HEAT_HEIGHT - fillHeight, 
                            0f, (float) (HEAT_HEIGHT - fillHeight), 
                            HEAT_WIDTH, fillHeight, HEAT_WIDTH, HEAT_HEIGHT);
                }
            }
        } else {
            pGraphics.blit(RenderPipelines.GUI_TEXTURED, SNOW_EMPTY, heatIndicatorX, heatIndicatorY, 0f, 0f, HEAT_WIDTH, HEAT_HEIGHT, HEAT_WIDTH, HEAT_HEIGHT);
            
            float fillRatio = Math.min(1.0f, Math.abs(currentHeat) / Math.abs(minHeat));
            int steps = (int) Math.ceil((HEAT_HEIGHT - 1) * fillRatio);
            
            if (steps > 0) {
                int fillHeight = steps + 1;
                pGraphics.blit(RenderPipelines.GUI_TEXTURED, SNOW_FULL, heatIndicatorX, heatIndicatorY + HEAT_HEIGHT - fillHeight, 
                        0f, (float) (HEAT_HEIGHT - fillHeight), 
                        HEAT_WIDTH, fillHeight, HEAT_WIDTH, HEAT_HEIGHT);
            }
        }
    }

    private void renderProgressArrow(GuiGraphicsExtractor pGraphics, int x, int y) {
        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();

        if (maxProgress > 0 && progress > 0) {
            int arrowX = 26;
            int arrowY = 37;
            
            int gapStart = GAP_START_X - arrowX; 
            int gapEnd = GAP_END_X - arrowX; 
            int gapSize = gapEnd - gapStart; 
            
            int activeWidth = ARROW_WIDTH - gapSize;
            int activePixels = (int) (((float) progress / maxProgress) * activeWidth);
            
            if (activePixels > 0) {
                if (activePixels <= gapStart) {
                    pGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_FULL, x + arrowX, y + arrowY, 0f, 0f, activePixels, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
                } else {
                    pGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_FULL, x + arrowX, y + arrowY, 0f, 0f, gapStart, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
                    
                    int remainingPixels = activePixels - gapStart;
                    pGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_FULL, x + arrowX + gapEnd, y + arrowY, (float) gapEnd, 0f, remainingPixels, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
                }
            }
        }
    }
}
