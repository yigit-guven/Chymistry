package net.yigitguven.chymistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;

@EventBusSubscriber(modid = Chymistry.MODID, value = Dist.CLIENT)
public class AlphaWarningScreen extends Screen {

    private static boolean shouldShow = false;
    private static int delay = -1;

    @SubscribeEvent
    public static void onClientJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        shouldShow = true;
        delay = 40;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (shouldShow) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                if (delay > 0) {
                    delay--;
                } else if (delay == 0) {
                    mc.setScreenAndShow(new AlphaWarningScreen());
                    shouldShow = false;
                    delay = -1;
                }
            }
        }
    }

    public AlphaWarningScreen() {
        super(Component.literal("Chymistry Alpha Warning"));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 150;
        int buttonHeight = 20;
        int spacing = 10;

        int startX = (this.width / 2) - buttonWidth - (spacing / 2);
        int y = this.height - 40;

        this.addRenderableWidget(Button
                .builder(Component.literal("Open GitHub Issues"),
                        ConfirmLinkScreen.confirmLink(this, "https://github.com/yigit-guven/Chymistry/issues"))
                .bounds(startX, y, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(Component.literal("Continue"), button -> {
            this.onClose();
        }).bounds(startX + buttonWidth + spacing, y, buttonWidth, buttonHeight).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        Component c1 = Component.literal("§6Welcome to the alpha of Chymistry mod!");
        Component c2 = Component.literal("This mod is in development for Curseforge ModJam 2026,");
        Component c3 = Component.literal("and will be fully done before 1st of September 2026.");
        Component c4 = Component.literal("Until then, this screen will appear indicating full version isn't up yet.");
        Component c5 = Component.literal("Please consider sending issue reports/feature requests to GitHub!");
        Component c6 = Component.literal("§7Thanks for playing! Hope you like it.");

        guiGraphics.text(this.font, c1, (this.width - this.font.width(c1)) / 2, this.height / 2 - 60, 0xFFFFFFFF, false);
        guiGraphics.text(this.font, c2, (this.width - this.font.width(c2)) / 2, this.height / 2 - 40, 0xFFFFFFFF, false);
        guiGraphics.text(this.font, c3, (this.width - this.font.width(c3)) / 2, this.height / 2 - 30, 0xFFFFFFFF, false);
        guiGraphics.text(this.font, c4, (this.width - this.font.width(c4)) / 2, this.height / 2 - 10, 0xFFFFFFFF, false);
        guiGraphics.text(this.font, c5, (this.width - this.font.width(c5)) / 2, this.height / 2, 0xFFFFFFFF, false);
        guiGraphics.text(this.font, c6, (this.width - this.font.width(c6)) / 2, this.height / 2 + 20, 0xFFFFFFFF, false);
    }
}
