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
                output.accept(ModBlocks.ALEMBIC.get());
                output.accept(ModBlocks.REINFORCED_GLASS.get());
                output.accept(ModItems.ASH.get());
                output.accept(ModItems.NITER_DUST.get());
                output.accept(ModItems.SUPER_FERTILIZER.get());
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
                output.accept(ModItems.REINFORCED_GLASS_BOTTLE.get());
                output.accept(ModItems.ELIXIR_OF_VITRIOL.get());
                output.accept(ModItems.BERRY_ESSENCE.get());
                output.accept(ModItems.ALCOHOL_BOTTLE.get());
                output.accept(ModItems.CREOSOTE_OIL.get());
                output.accept(ModItems.DEFOLIANT_POTION.get());
                output.accept(ModItems.EXPLOSIVE_LIQUID.get());
                output.accept(ModItems.GOLD_SOLVENT_BOTTLE.get());
                output.accept(ModItems.HYDROCHLORIC_ACID_BOTTLE.get());
                output.accept(ModItems.NITRIC_ACID_BOTTLE.get());
                output.accept(ModItems.PLASTIC_BLOCK.get());
                output.accept(ModItems.SULFURIC_ACID_BOTTLE.get());
                output.accept(ModItems.VIGOR_POTION.get());
                output.accept(ModItems.DISINFECTANT.get());

                        output.accept(ModItems.BLAST_PROOF_CEMENT.get());
                        output.accept(ModItems.BRASS_INGOT.get());
                        output.accept(ModItems.DYNAMITE.get());
                        output.accept(ModItems.FREEZING_POWDER.get());
                        output.accept(ModItems.GREEN_CRYSTAL.get());
                        output.accept(ModItems.INCENDIARY_DUST.get());
                        output.accept(ModItems.WHITE_MATCH.get());
                        output.accept(ModItems.ORANGE_MATCH.get());
                        output.accept(ModItems.MAGENTA_MATCH.get());
                        output.accept(ModItems.LIGHT_BLUE_MATCH.get());
                        output.accept(ModItems.YELLOW_MATCH.get());
                        output.accept(ModItems.LIME_MATCH.get());
                        output.accept(ModItems.PINK_MATCH.get());
                        output.accept(ModItems.GRAY_MATCH.get());
                        output.accept(ModItems.LIGHT_GRAY_MATCH.get());
                        output.accept(ModItems.CYAN_MATCH.get());
                        output.accept(ModItems.PURPLE_MATCH.get());
                        output.accept(ModItems.BLUE_MATCH.get());
                        output.accept(ModItems.BROWN_MATCH.get());
                        output.accept(ModItems.GREEN_MATCH.get());
                        output.accept(ModItems.RED_MATCH.get());
                        output.accept(ModItems.BLACK_MATCH.get());
                        output.accept(ModItems.PHOSPHORUS.get());
                        output.accept(ModItems.PHOSPHORUS_TORCH.get());
                        output.accept(ModItems.PLASTIC_PELLETS.get());
                        output.accept(ModItems.SILVER_INGOT.get());
                        output.accept(ModItems.SILVER_SLUDGE.get());
                        output.accept(ModItems.SOAP.get());
                        output.accept(ModItems.STEEL_INGOT.get());
                        output.accept(ModItems.STORM_POWDER.get());
                        output.accept(ModItems.VULCANIZED_RUBBER.get());
                        output.accept(ModItems.REPELLENT_BASE.get());

                        net.minecraft.core.registries.BuiltInRegistries.ITEM.stream()
                                .map(ItemStack::new)
                                .filter(net.yigitguven.chymistry.wood.TreatedWoodHelper::isWoodMaterial)
                                .map(stack -> net.yigitguven.chymistry.wood.TreatedWoodHelper.makeTreated(stack, 1))
                                .forEach(output::accept);
            })
            .build());
}


