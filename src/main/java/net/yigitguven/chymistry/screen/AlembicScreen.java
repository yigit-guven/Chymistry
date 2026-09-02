package net.yigitguven.chymistry.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.menu.AlembicMenu;

public class AlembicScreen extends AbstractContainerScreen<AlembicMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/alembicui.png");
    private static final Identifier ARROW_TEXTURE = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/alembicfullarrow.png");
    private static final Identifier FUEL_ICON = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/fuelinputicon.png");
    private static final Identifier GAS_TEXTURE = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/gasfull.png");
    private static final Identifier EMPTY_FIRE = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/empty_fire.png");
    private static final Identifier FULL_FIRE = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/full_fire.png");

    private static final int ARROW_WIDTH = 68;
    private static final int ARROW_HEIGHT = 14;
    private static final int GAP_START = 25;
    private static final int GAP_END = 42;

    public AlembicScreen(AlembicMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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

        // Draw main UI background
        pGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0f, 0f, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        // Draw fuel input icon if fuel slot is empty
        if (!this.menu.slots.get(4).hasItem()) {
            pGraphics.blit(RenderPipelines.GUI_TEXTURED, FUEL_ICON, x + 88, y + 53, 0f, 0f, 18, 18, 18, 18);
        }

        // Draw fire indicator
        pGraphics.blit(RenderPipelines.GUI_TEXTURED, EMPTY_FIRE, x + 90, y + 36, 0f, 0f, 14, 14, 14, 14);
        
        if (this.menu.getFuelTime() > 0 && this.menu.getMaxFuelTime() > 0) {
            int fireHeight = (int) (14.0F * ((float) this.menu.getFuelTime() / this.menu.getMaxFuelTime()));
            if (fireHeight > 0) {
                // Draw bottom-up
                pGraphics.blit(RenderPipelines.GUI_TEXTURED, FULL_FIRE, x + 90, y + 36 + (14 - fireHeight), 0f, 14f - fireHeight, 14, fireHeight, 14, 14);
            }
        }

        // Draw progress arrow (left to right)
        int progress = this.menu.getProgress();
        int maxProgress = this.menu.getMaxProgress();

        if (progress > 0 && maxProgress > 0) {
            int arrowX = 63;
            int arrowY = 35;

            int gapSize = GAP_END - GAP_START;
            int activeWidth = ARROW_WIDTH - gapSize;
            int activePixels = (int) (((float) progress / maxProgress) * activeWidth);

            if (activePixels > 0) {
                if (activePixels <= GAP_START) {
                    pGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_TEXTURE, x + arrowX, y + arrowY, 0f, 0f,
                            activePixels, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
                } else {
                    pGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_TEXTURE, x + arrowX, y + arrowY, 0f, 0f, GAP_START,
                            ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);

                    int remainingPixels = activePixels - GAP_START;
                    pGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW_TEXTURE, x + arrowX + GAP_END, y + arrowY,
                            (float) GAP_END, 0f, remainingPixels, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
                }
            }
        }

        // Draw gas full indicator (bottom to top, same % as arrow)
        if (this.menu.hasConnectedBottle() && this.menu.getProgress() > 0 && this.menu.getMaxProgress() > 0) {
            int gasHeight = (int) (29.0F * ((float) this.menu.getProgress() / this.menu.getMaxProgress()));
            if (gasHeight > 0) {
                pGraphics.blit(RenderPipelines.GUI_TEXTURED, GAS_TEXTURE, x + 91, y + 6 + (29 - gasHeight), 0f, 29f - gasHeight, 12, gasHeight, 12, 29);
            }
        }
    }
}
