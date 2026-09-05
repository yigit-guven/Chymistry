package net.yigitguven.chymistry.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Consumer;

public class FreezingPowderItem extends Item {

    public FreezingPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            if (freezeAt(level, pos, player, stack, hand)) {
                return InteractionResult.SUCCESS;
            }
        }

        BlockPos playerPos = player.blockPosition();
        if (freezeAt(level, playerPos, player, stack, hand)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos eyePos = BlockPos.containing(player.getEyePosition());
        if (!eyePos.equals(playerPos) && freezeAt(level, eyePos, player, stack, hand)) {
            return InteractionResult.SUCCESS;
        }

        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        InteractionHand hand = context.getHand();

        if (freezeAt(level, clickedPos, player, stack, hand)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos relativePos = clickedPos.relative(context.getClickedFace());
        if (freezeAt(level, relativePos, player, stack, hand)) {
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    private boolean freezeAt(Level level, BlockPos pos, Player player, ItemStack stack, InteractionHand hand) {
        BlockState state = level.getBlockState(pos);
        FluidState fluid = level.getFluidState(pos);

        if (!fluid.isSource()) {
            return false;
        }

        if (state.is(Blocks.WATER) && fluid.is(FluidTags.WATER)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, Blocks.BLUE_ICE.defaultBlockState(), 3);
                level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0f, 1.4f);
                level.playSound(null, pos, SoundEvents.PLAYER_HURT_FREEZE, SoundSource.BLOCKS, 0.8f, 1.2f);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 25, 0.4, 0.4, 0.4, 0.05);
                    serverLevel.sendParticles(ParticleTypes.ITEM_SNOWBALL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 15, 0.3, 0.3, 0.3, 0.05);
                }

                if (player != null && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            if (player != null) {
                player.swing(hand, true);
            }
            return true;
        } else if (state.is(Blocks.LAVA) && fluid.is(FluidTags.LAVA)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.playSound(null, pos, SoundEvents.BASALT_PLACE, SoundSource.BLOCKS, 0.8f, 0.8f);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12, 0.3, 0.3, 0.3, 0.02);
                    serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 20, 0.35, 0.35, 0.35, 0.05);
                }

                if (player != null && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            if (player != null) {
                player.swing(hand, true);
            }
            return true;
        }

        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.freezing_powder.desc").withStyle(ChatFormatting.AQUA));
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
    }
}
