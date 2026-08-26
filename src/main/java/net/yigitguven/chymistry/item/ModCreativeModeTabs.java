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
            .icon(() -> new ItemStack(ModItems.ELIXIR_OF_VITRIOL.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.MORTAR.get());
                output.accept(ModItems.ASH.get());
                output.accept(ModItems.NITER_DUST.get());
                output.accept(ModItems.ANIMAL_FAT.get());
                output.accept(ModItems.COPPER_DUST.get());
                output.accept(ModItems.IRON_DUST.get());
                output.accept(ModItems.IRON_TONGS.get());
                output.accept(ModItems.RUST_POWDER.get());
                output.accept(ModItems.PURIFIED_GOLD_DUST.get());
                output.accept(ModItems.SEA_WATER_BUCKET.get());
                output.accept(ModItems.SEA_SALT.get());
                output.accept(ModBlocks.QUICKLIME.get());
                output.accept(ModBlocks.QUICKLIME_STAIRS.get());
                output.accept(ModBlocks.QUICKLIME_SLAB.get());
                output.accept(ModBlocks.QUICKLIME_WALL.get());
                output.accept(ModBlocks.BRICK_CRUCIBLE.get());
                output.accept(ModBlocks.DEEPSLATE_CRUCIBLE.get());
                output.accept(ModBlocks.NETHERITE_CRUCIBLE.get());
                output.accept(ModItems.TINTED_GLASS_BOTTLE.get());
                output.accept(ModItems.ELIXIR_OF_VITRIOL.get());
                output.accept(ModItems.THERMOMETER.get());
            })
            .build());
}
