package net.yigitguven.chymistry.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.menu.MortarMenu;
import net.yigitguven.chymistry.network.MeshButtonPressedPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public class MortarScreen extends AbstractContainerScreen<MortarMenu> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/mortar_gui.png");

    private static final Identifier ERROR_ARROW = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/error_arrow.png");
    private static final Identifier FULL_ARROW = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/full_arrow.png");

    private static final Identifier MESH_BUTTON = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/mesh_button.png");
    private static final Identifier MESH_BUTTON_HOVER = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/mesh_button_hover.png");
    private static final Identifier MESH_BUTTON_PRESSED = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/mesh_button_pressed.png");

    // TODO: Update these coordinates if they are incorrect! 
    // I set the button below the indicator by default.
    private static final int BUTTON_X = 72;
    private static final int BUTTON_Y = 56;
    private static final int BUTTON_SIZE = 32;

    private boolean isButtonPressed = false;

    public MortarScreen(MortarMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean handled) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        double pMouseX = event.x();
        double pMouseY = event.y();

        if (pMouseX >= x + BUTTON_X && pMouseX <= x + BUTTON_X + BUTTON_SIZE && pMouseY >= y + BUTTON_Y && pMouseY <= y + BUTTON_Y + BUTTON_SIZE) {
            net.minecraft.client.Minecraft.getInstance().getConnection().send(new MeshButtonPressedPayload(this.menu.blockEntity.getBlockPos()));
            this.isButtonPressed = true;
            return true; // handled
        }
        return super.mouseClicked(event, handled);
    }

    @Override
    public boolean mouseReleased(net.minecraft.client.input.MouseButtonEvent event) {
        if (event.button() == 0) {
            this.isButtonPressed = false;
        }
        return super.mouseReleased(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor pGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.extractBackground(pGraphics, pMouseX, pMouseY, pPartialTick);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        pGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0f, 0f, this.imageWidth, this.imageHeight, 256, 256);

        // Draw indicator
        int indicatorX = x + 72;
        int indicatorY = y + 32;

        if (this.menu.hasError()) {
            pGraphics.blit(RenderPipelines.GUI_TEXTURED, ERROR_ARROW, indicatorX, indicatorY, 0f, 0f, 28, 21, 28, 21);
        } else {
            int progress = this.menu.getScaledProgress();
            if (progress > 0) {
                pGraphics.blit(RenderPipelines.GUI_TEXTURED, FULL_ARROW, indicatorX, indicatorY, 0f, 0f, progress, 21, 28, 21);
            }
        }

        // Draw button
        int btnX = x + BUTTON_X;
        int btnY = y + BUTTON_Y;
        boolean isHovered = pMouseX >= btnX && pMouseX <= btnX + BUTTON_SIZE && pMouseY >= btnY && pMouseY <= btnY + BUTTON_SIZE;

        Identifier buttonTexture;
        if (this.isButtonPressed && isHovered) {
            buttonTexture = MESH_BUTTON_PRESSED;
        } else if (isHovered) {
            buttonTexture = MESH_BUTTON_HOVER;
        } else {
            buttonTexture = MESH_BUTTON;
        }

        pGraphics.blit(RenderPipelines.GUI_TEXTURED, buttonTexture, btnX, btnY, 0f, 0f, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
    }
}
