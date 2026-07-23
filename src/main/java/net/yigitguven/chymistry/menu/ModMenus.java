package net.yigitguven.chymistry.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Chymistry.MODID);

    public static final Supplier<MenuType<MortarMenu>> MORTAR_MENU =
            MENUS.register("mortar", () -> IMenuTypeExtension.create(MortarMenu::new));
}
