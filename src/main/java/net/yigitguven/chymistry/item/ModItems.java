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

    public static final DeferredItem<Item> ASH = ITEMS.registerSimpleItem("ash");
    public static final DeferredItem<Item> NITER_DUST = ITEMS.registerSimpleItem("niter_dust");
    public static final DeferredItem<Item> ANIMAL_FAT = ITEMS.registerSimpleItem("animal_fat");
    public static final DeferredItem<Item> COPPER_DUST = ITEMS.registerSimpleItem("copper_dust");
    public static final DeferredItem<Item> IRON_DUST = ITEMS.registerSimpleItem("iron_dust");
    public static final DeferredItem<Item> PURIFIED_GOLD_DUST = ITEMS.registerSimpleItem("purified_gold_dust");
    public static final DeferredItem<Item> TINTED_GLASS_BOTTLE = ITEMS.registerSimpleItem("tinted_glass_bottle");
    public static final DeferredItem<Item> ELIXIR_OF_VITRIOL = ITEMS.registerItem("elixir_of_vitriol", properties -> new Item(properties
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

    public static final DeferredItem<BlockItem> MORTAR = ITEMS.registerSimpleBlockItem("mortar", ModBlocks.MORTAR);
    public static final DeferredItem<BlockItem> QUICKLIME = ITEMS.registerSimpleBlockItem("quicklime", ModBlocks.QUICKLIME);
    public static final DeferredItem<BlockItem> QUICKLIME_STAIRS = ITEMS.registerSimpleBlockItem("quicklime_stairs", ModBlocks.QUICKLIME_STAIRS);
    public static final DeferredItem<BlockItem> QUICKLIME_SLAB = ITEMS.registerSimpleBlockItem("quicklime_slab", ModBlocks.QUICKLIME_SLAB);
    public static final DeferredItem<BlockItem> QUICKLIME_WALL = ITEMS.registerSimpleBlockItem("quicklime_wall", ModBlocks.QUICKLIME_WALL);
}
