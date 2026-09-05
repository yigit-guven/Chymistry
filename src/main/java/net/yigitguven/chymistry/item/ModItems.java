package net.yigitguven.chymistry.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Chymistry.MODID);

    public static final DeferredItem<Item> ASH = ITEMS.registerItem("ash", properties -> new Item(properties.fireResistant()));
    public static final DeferredItem<Item> NITER_DUST = ITEMS.registerSimpleItem("niter_dust");
    public static final DeferredItem<Item> SUPER_FERTILIZER = ITEMS.registerItem("super_fertilizer", SuperFertilizerItem::new);
    public static final DeferredItem<Item> ANIMAL_FAT = ITEMS.registerSimpleItem("animal_fat");
    public static final DeferredItem<Item> COPPER_DUST = ITEMS.registerSimpleItem("copper_dust");
    public static final DeferredItem<Item> IRON_DUST = ITEMS.registerSimpleItem("iron_dust");
    public static final DeferredItem<Item> RUST_POWDER = ITEMS.registerSimpleItem("rust_powder");
    public static final DeferredItem<Item> PURIFIED_GOLD_DUST = ITEMS.registerSimpleItem("purified_gold_dust");
    public static final DeferredItem<Item> SEA_WATER_BUCKET = ITEMS.registerItem("sea_water_bucket", properties -> new net.minecraft.world.item.BucketItem(net.minecraft.world.level.material.Fluids.WATER, properties.craftRemainder(net.minecraft.world.item.Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<Item> TINTED_GLASS_BOTTLE = ITEMS.registerSimpleItem("tinted_glass_bottle");
    public static final DeferredItem<Item> SEA_SALT = ITEMS.registerSimpleItem("sea_salt");
    public static final DeferredItem<Item> IRON_TONGS = ITEMS.registerItem("iron_tongs", properties -> new IronTongsItem(properties.stacksTo(1)));
    public static final DeferredItem<Item> THERMOMETER = ITEMS.registerItem("thermometer", properties -> new Item(properties.stacksTo(1)) {
        @Override
        public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, net.minecraft.world.item.component.TooltipDisplay pTooltipDisplay, java.util.function.Consumer<Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
            pTooltipComponents.accept(Component.translatable("tooltip.chymistry.thermometer.desc").withStyle(net.minecraft.ChatFormatting.GRAY));
            super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
        }
    });
    public static final DeferredItem<Item> ELIXIR_OF_VITRIOL = ITEMS.registerItem("elixir_of_vitriol", properties -> new Item(properties.stacksTo(1)
            .component(net.minecraft.core.component.DataComponents.FOOD, new net.minecraft.world.food.FoodProperties.Builder().alwaysEdible().build())
            .component(net.minecraft.core.component.DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumable.builder()
                .animation(net.minecraft.world.item.ItemUseAnimation.DRINK)
                .sound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK)
                .onConsume(new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(new net.minecraft.world.effect.MobEffectInstance(net.yigitguven.chymistry.effect.ModMobEffects.VITRIOL_IMMUNITY, 600, 0), 1.0f))
                .onConsume(new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.SLOWNESS, 100, 0), 1.0f))
                .build())
            .usingConvertsTo(TINTED_GLASS_BOTTLE.get())) {
        @Override
        public Component getName(ItemStack pStack) {
            return super.getName(pStack).copy().withStyle(style -> style.withColor(0x8B008B));
        }

        @Override
        public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, net.minecraft.world.item.component.TooltipDisplay pTooltipDisplay, java.util.function.Consumer<Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable(net.yigitguven.chymistry.effect.ModMobEffects.VITRIOL_IMMUNITY.value().getDescriptionId()).append(" (0:30)").withStyle(net.minecraft.ChatFormatting.BLUE));
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable(net.minecraft.world.effect.MobEffects.SLOWNESS.value().getDescriptionId()).append(" (0:05)").withStyle(net.minecraft.ChatFormatting.RED));
            
            pTooltipComponents.accept(net.minecraft.network.chat.Component.empty());
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("potion.whenDrank").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE));
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.vitriol_immunity.modifier").withStyle(net.minecraft.ChatFormatting.BLUE));
            pTooltipComponents.accept(net.minecraft.network.chat.Component.literal("-15% ").append(net.minecraft.network.chat.Component.translatable(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED.value().getDescriptionId())).withStyle(net.minecraft.ChatFormatting.RED));
        }
    });

    public static final DeferredItem<Item> BERRY_ESSENCE = ITEMS.registerItem("berry_essence", properties -> new Item(properties.stacksTo(1)
            .craftRemainder(net.minecraft.world.item.Items.GLASS_BOTTLE)));

    public static final DeferredItem<Item> BATTERY = ITEMS.registerSimpleItem("battery");
    public static final DeferredItem<BlockItem> BLAST_PROOF_CEMENT = ITEMS.registerItem("blast_proof_cement", properties -> new BlastProofCementItem(net.yigitguven.chymistry.block.ModBlocks.BLAST_PROOF_CEMENT.get(), properties));
    public static final DeferredItem<Item> BRASS_INGOT = ITEMS.registerSimpleItem("brass_ingot");
    public static final DeferredItem<Item> DYNAMITE = ITEMS.registerItem("dynamite", properties -> new DynamiteItem(properties.stacksTo(16)));
    public static final DeferredItem<Item> FREEZING_POWDER = ITEMS.registerItem("freezing_powder", FreezingPowderItem::new);
    public static final DeferredItem<Item> GREEN_CRYSTAL = ITEMS.registerSimpleItem("green_crystal");
    public static final DeferredItem<Item> INCENDIARY_DUST = ITEMS.registerSimpleItem("incendiary_dust");
    public static final DeferredItem<Item> WHITE_MATCH = ITEMS.registerItem("white_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.WHITE, properties));
    public static final DeferredItem<Item> ORANGE_MATCH = ITEMS.registerItem("orange_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.ORANGE, properties));
    public static final DeferredItem<Item> MAGENTA_MATCH = ITEMS.registerItem("magenta_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.MAGENTA, properties));
    public static final DeferredItem<Item> LIGHT_BLUE_MATCH = ITEMS.registerItem("light_blue_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.LIGHT_BLUE, properties));
    public static final DeferredItem<Item> YELLOW_MATCH = ITEMS.registerItem("yellow_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.YELLOW, properties));
    public static final DeferredItem<Item> LIME_MATCH = ITEMS.registerItem("lime_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.LIME, properties));
    public static final DeferredItem<Item> PINK_MATCH = ITEMS.registerItem("pink_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.PINK, properties));
    public static final DeferredItem<Item> GRAY_MATCH = ITEMS.registerItem("gray_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.GRAY, properties));
    public static final DeferredItem<Item> LIGHT_GRAY_MATCH = ITEMS.registerItem("light_gray_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.LIGHT_GRAY, properties));
    public static final DeferredItem<Item> CYAN_MATCH = ITEMS.registerItem("cyan_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.CYAN, properties));
    public static final DeferredItem<Item> PURPLE_MATCH = ITEMS.registerItem("purple_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.PURPLE, properties));
    public static final DeferredItem<Item> BLUE_MATCH = ITEMS.registerItem("blue_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.BLUE, properties));
    public static final DeferredItem<Item> BROWN_MATCH = ITEMS.registerItem("brown_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.BROWN, properties));
    public static final DeferredItem<Item> GREEN_MATCH = ITEMS.registerItem("green_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.GREEN, properties));
    public static final DeferredItem<Item> RED_MATCH = ITEMS.registerItem("red_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.RED, properties));
    public static final DeferredItem<Item> BLACK_MATCH = ITEMS.registerItem("black_match", properties -> new MatchItem(net.minecraft.world.item.DyeColor.BLACK, properties));
    public static final DeferredItem<Item> MATCH = RED_MATCH;
    public static final DeferredItem<Item> PHOSPHORUS = ITEMS.registerSimpleItem("phosphorus");
    public static final DeferredItem<Item> PLASTIC_PELLETS = ITEMS.registerSimpleItem("plastic_pellets");
    public static final DeferredItem<Item> SILVER_INGOT = ITEMS.registerSimpleItem("silver_ingot");
    public static final DeferredItem<Item> SILVER_SLUDGE = ITEMS.registerSimpleItem("silver_sludge");
    public static final DeferredItem<Item> SOAP = ITEMS.registerItem("soap", properties -> new SoapItem(properties.durability(64)));
    public static final DeferredItem<Item> STEEL_INGOT = ITEMS.registerSimpleItem("steel_ingot");
    public static final DeferredItem<Item> STORM_POWDER = ITEMS.registerSimpleItem("storm_powder");
    public static final DeferredItem<Item> VULCANIZED_RUBBER = ITEMS.registerSimpleItem("vulcanized_rubber");
    public static final DeferredItem<Item> REINFORCED_GLASS_BOTTLE = ITEMS.registerSimpleItem("reinforced_glass_bottle");

    public static final DeferredItem<Item> ALCOHOL_BOTTLE = ITEMS.registerItem("alcohol_bottle", properties -> new Item(properties.stacksTo(1).craftRemainder(REINFORCED_GLASS_BOTTLE.get())));
    public static final DeferredItem<Item> CREOSOTE_OIL = ITEMS.registerItem("creosote_oil", properties -> new Item(properties.stacksTo(1).craftRemainder(net.minecraft.world.item.Items.GLASS_BOTTLE)) {
        @Override
        public int getBurnTime(ItemStack itemStack, net.minecraft.world.item.crafting.@org.jspecify.annotations.Nullable RecipeType<?> recipeType, net.minecraft.world.level.block.entity.FuelValues fuelValues) {
            return 2400;
        }
    });
    public static final DeferredItem<Item> DEFOLIANT_POTION = ITEMS.registerItem("defoliant_potion", properties -> new DefoliantPotionItem(properties.stacksTo(1)));
    public static final DeferredItem<Item> EXPLOSIVE_LIQUID = ITEMS.registerItem("explosive_liquid", properties -> new ExplosiveLiquidItem(properties.stacksTo(16).craftRemainder(TINTED_GLASS_BOTTLE.get())));
    public static final DeferredItem<Item> GOLD_SOLVENT_BOTTLE = ITEMS.registerItem("gold_solvent_bottle", properties -> new Item(properties.stacksTo(1).craftRemainder(REINFORCED_GLASS_BOTTLE.get())));
    public static final DeferredItem<Item> HYDROCHLORIC_ACID_BOTTLE = ITEMS.registerItem("hydrochloric_acid_bottle", properties -> new Item(properties.stacksTo(1).craftRemainder(REINFORCED_GLASS_BOTTLE.get())));
    public static final DeferredItem<Item> NITRIC_ACID_BOTTLE = ITEMS.registerItem("nitric_acid_bottle", properties -> new Item(properties.stacksTo(1).craftRemainder(REINFORCED_GLASS_BOTTLE.get())));
    public static final DeferredItem<Item> SULFURIC_ACID_BOTTLE = ITEMS.registerItem("sulfuric_acid_bottle", properties -> new Item(properties.stacksTo(1).craftRemainder(REINFORCED_GLASS_BOTTLE.get())));
    public static final DeferredItem<Item> VIGOR_POTION = ITEMS.registerItem("vigor_potion", properties -> new Item(properties.stacksTo(1)
            .component(net.minecraft.core.component.DataComponents.FOOD, new net.minecraft.world.food.FoodProperties.Builder().alwaysEdible().build())
            .component(net.minecraft.core.component.DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumable.builder()
                .animation(net.minecraft.world.item.ItemUseAnimation.DRINK)
                .sound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK)
                .onConsume(new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(new net.minecraft.world.effect.MobEffectInstance(net.yigitguven.chymistry.effect.ModMobEffects.VIGOR, 200, 0), 1.0f))
                .build())
            .usingConvertsTo(TINTED_GLASS_BOTTLE.get())) {
        @Override
        public Component getName(ItemStack pStack) {
            return super.getName(pStack).copy().withStyle(style -> style.withColor(0x00C4B4));
        }

        @Override
        public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, net.minecraft.world.item.component.TooltipDisplay pTooltipDisplay, java.util.function.Consumer<Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable(net.yigitguven.chymistry.effect.ModMobEffects.VIGOR.value().getDescriptionId()).append(" (0:10)").withStyle(net.minecraft.ChatFormatting.BLUE));
            pTooltipComponents.accept(net.minecraft.network.chat.Component.empty());
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("potion.whenDrank").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE));
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.vigor.desc").withStyle(net.minecraft.ChatFormatting.BLUE));
        }
    });
    public static final DeferredItem<Item> IMMUNITY_POTION = ITEMS.registerItem("immunity_potion", properties -> new Item(properties.stacksTo(1)
            .component(net.minecraft.core.component.DataComponents.FOOD, new net.minecraft.world.food.FoodProperties.Builder().alwaysEdible().build())
            .component(net.minecraft.core.component.DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumable.builder()
                .animation(net.minecraft.world.item.ItemUseAnimation.DRINK)
                .sound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK)
                .onConsume(new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(new net.minecraft.world.effect.MobEffectInstance(net.yigitguven.chymistry.effect.ModMobEffects.IMMUNITY, 3600, 0), 1.0f))
                .build())
            .usingConvertsTo(net.minecraft.world.item.Items.GLASS_BOTTLE)) {
        @Override
        public Component getName(ItemStack pStack) {
            return super.getName(pStack).copy().withStyle(style -> style.withColor(0x70E8E8));
        }

        @Override
        public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, net.minecraft.world.item.component.TooltipDisplay pTooltipDisplay, java.util.function.Consumer<Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable(net.yigitguven.chymistry.effect.ModMobEffects.IMMUNITY.value().getDescriptionId()).append(" (3:00)").withStyle(net.minecraft.ChatFormatting.BLUE));
            pTooltipComponents.accept(net.minecraft.network.chat.Component.empty());
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("potion.whenDrank").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE));
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.immunity.desc").withStyle(net.minecraft.ChatFormatting.BLUE));
        }
    });
    public static final DeferredItem<Item> DISINFECTANT = ITEMS.registerItem("disinfectant", properties -> new DisinfectantItem(properties.stacksTo(1)
            .component(net.minecraft.core.component.DataComponents.FOOD, new net.minecraft.world.food.FoodProperties.Builder().alwaysEdible().build())
            .component(net.minecraft.core.component.DataComponents.CONSUMABLE, net.minecraft.world.item.component.Consumable.builder()
                .animation(net.minecraft.world.item.ItemUseAnimation.DRINK)
                .sound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK)
                .build())
            .usingConvertsTo(net.minecraft.world.item.Items.GLASS_BOTTLE)));



    public static final DeferredItem<BlockItem> MORTAR = ITEMS.registerSimpleBlockItem("mortar", ModBlocks.MORTAR);
    public static final DeferredItem<BlockItem> QUICKLIME = ITEMS.registerSimpleBlockItem("quicklime", ModBlocks.QUICKLIME);
    public static final DeferredItem<BlockItem> QUICKLIME_STAIRS = ITEMS.registerSimpleBlockItem("quicklime_stairs", ModBlocks.QUICKLIME_STAIRS);
    public static final DeferredItem<BlockItem> QUICKLIME_SLAB = ITEMS.registerSimpleBlockItem("quicklime_slab", ModBlocks.QUICKLIME_SLAB);
    public static final DeferredItem<BlockItem> QUICKLIME_WALL = ITEMS.registerSimpleBlockItem("quicklime_wall", ModBlocks.QUICKLIME_WALL);
    public static final DeferredItem<BlockItem> BRICK_CRUCIBLE = ITEMS.registerItem("brick_crucible", properties -> new CrucibleBlockItem(ModBlocks.BRICK_CRUCIBLE.get(), properties));
    public static final DeferredItem<BlockItem> DEEPSLATE_CRUCIBLE = ITEMS.registerItem("deepslate_crucible", properties -> new CrucibleBlockItem(ModBlocks.DEEPSLATE_CRUCIBLE.get(), properties));
    public static final DeferredItem<BlockItem> NETHERITE_CRUCIBLE = ITEMS.registerItem("netherite_crucible", properties -> new CrucibleBlockItem(ModBlocks.NETHERITE_CRUCIBLE.get(), properties));
    public static final DeferredItem<BlockItem> ALEMBIC = ITEMS.registerSimpleBlockItem("alembic", ModBlocks.ALEMBIC);
    public static final DeferredItem<BlockItem> PLASTIC_BLOCK = ITEMS.registerSimpleBlockItem("plastic_block", ModBlocks.PLASTIC_BLOCK);
    
    public static final DeferredItem<BlockItem> REINFORCED_GLASS = ITEMS.registerItem("reinforced_glass", properties -> new BlockItem(ModBlocks.REINFORCED_GLASS.get(), properties) {
        @Override
        public boolean isFoil(ItemStack pStack) {
            return true;
        }
    });

    public static final DeferredItem<BlockItem> REPELLENT_BASE = ITEMS.registerItem("repellent_base", properties -> new BlockItem(ModBlocks.REPELLENT_BASE.get(), properties.durability(600)));

    public static final DeferredItem<Item> PHOSPHORUS_TORCH = ITEMS.registerItem("phosphorus_torch",
        properties -> new net.minecraft.world.item.StandingAndWallBlockItem(ModBlocks.PHOSPHORUS_TORCH.get(), ModBlocks.PHOSPHORUS_WALL_TORCH.get(), net.minecraft.core.Direction.DOWN, properties));
}


