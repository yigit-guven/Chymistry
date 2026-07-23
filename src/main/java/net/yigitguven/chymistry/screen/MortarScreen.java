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
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/mortar.png");

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

        if (pMouseX >= x + 79 && pMouseX <= x + 97 && pMouseY >= y + 34 && pMouseY <= y + 52) {
            net.minecraft.client.Minecraft.getInstance().getConnection().send(new MeshButtonPressedPayload(this.menu.blockEntity.getBlockPos()));
            return true; // handled
        }
        return super.mouseClicked(event, handled);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor pGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.extractBackground(pGraphics, pMouseX, pMouseY, pPartialTick);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        pGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0f, 0f, this.imageWidth, this.imageHeight, 256, 256);
    }
}
