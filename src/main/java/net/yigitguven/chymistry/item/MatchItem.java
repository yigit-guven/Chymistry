package net.yigitguven.chymistry.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class MatchItem extends Item {

    private final DyeColor dyeColor;

    public MatchItem(DyeColor dyeColor, Properties properties) {
        super(properties.durability(60));
        this.dyeColor = dyeColor;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    public static boolean isLit(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.copyTag().getBoolean("Lit").orElse(false);
    }

    public static void setLit(ItemStack stack, boolean lit) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        tag.putBoolean("Lit", lit);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, lit);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean lit = isLit(stack);
        setLit(stack, !lit);
        if (!lit) {
            level.playSound(player, player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, 1.5F);
        } else {
            level.playSound(player, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.6F, 1.8F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            boolean lit = isLit(stack);
            setLit(stack, !lit);
            Level level = player.level();
            if (!lit) {
                level.playSound(player, player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, 1.5F);
            } else {
                level.playSound(player, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.6F, 1.8F);
            }
            return true;
        }
        return super.overrideStackedOnOther(stack, slot, action, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && other.isEmpty()) {
            boolean lit = isLit(stack);
            setLit(stack, !lit);
            Level level = player.level();
            if (!lit) {
                level.playSound(player, player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, 1.5F);
            } else {
                level.playSound(player, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.6F, 1.8F);
            }
            return true;
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    public void tickCustom(ItemStack stack, Level level, Player player) {
        if (!isLit(stack)) {
            return;
        }

        int rgb = this.dyeColor.getTextureDiffuseColor();

        if (player.tickCount % 20 == 0) {
            int nextDamage = stack.getDamageValue() + 1;
            stack.setDamageValue(nextDamage);
            if (nextDamage >= stack.getMaxDamage()) {
                stack.shrink(1);
                level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.8F, 1.8F);
            }
        }

        if (player.tickCount % 4 == 0 && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    new DustParticleOptions(rgb, 0.75F),
                    player.getX(),
                    player.getY() + 1.2,
                    player.getZ(),
                    2,
                    0.25,
                    0.25,
                    0.25,
                    0.01
            );
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
        if (isLit(pStack)) {
            int remaining = Math.max(0, pStack.getMaxDamage() - pStack.getDamageValue());
            pTooltipComponents.accept(Component.translatable("tooltip.chymistry.match.lit").append(" (" + remaining + "s)").withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.accept(Component.translatable("tooltip.chymistry.match.unlit").withStyle(ChatFormatting.GRAY));
        }
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.match.desc").withStyle(ChatFormatting.DARK_GRAY));
    }
}
