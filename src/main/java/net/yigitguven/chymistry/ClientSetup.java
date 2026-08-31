package net.yigitguven.chymistry;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.yigitguven.chymistry.menu.ModMenus;
import net.yigitguven.chymistry.screen.MortarScreen;

@EventBusSubscriber(modid = Chymistry.MODID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MORTAR_MENU.get(), MortarScreen::new);
        event.register(ModMenus.CRUCIBLE_MENU.get(), net.yigitguven.chymistry.screen.CrucibleScreen::new);
    }

}
