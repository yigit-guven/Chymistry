package net.yigitguven.chymistry.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Chymistry.MODID);

    public static final Supplier<CreativeModeTab> CHYMISTRY_TAB = CREATIVE_MODE_TABS.register("chymistry_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.chymistry"))
            .icon(() -> new ItemStack(ModBlocks.MORTAR.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.MORTAR.get());
            })
            .build());
}
