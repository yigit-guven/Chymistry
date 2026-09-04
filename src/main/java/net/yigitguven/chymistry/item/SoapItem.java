package net.yigitguven.chymistry.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.yigitguven.chymistry.util.SoapCleaningHelper;

import java.util.function.Consumer;

public class SoapItem extends Item {

    public SoapItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());

        if (SoapCleaningHelper.cleanBlock(level, context.getClickedPos(), state, context.getPlayer(), context.getItemInHand(), context.getHand())) {
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand hand) {
        if (interactionTarget instanceof Sheep sheep && sheep.getColor() != DyeColor.WHITE) {
            Level level = player.level();
            if (!level.isClientSide()) {
                sheep.setColor(DyeColor.WHITE);

                level.playSound(null, sheep.blockPosition(), SoundEvents.SLIME_BLOCK_STEP, SoundSource.PLAYERS, 1.0f, 1.2f);
                level.playSound(null, sheep.blockPosition(), SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.PLAYERS, 0.6f, 1.5f);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.BUBBLE_POP, sheep.getX(), sheep.getY() + 0.5, sheep.getZ(), 12, 0.3, 0.3, 0.3, 0.05);
                    serverLevel.sendParticles(ParticleTypes.SPLASH, sheep.getX(), sheep.getY() + 0.5, sheep.getZ(), 12, 0.3, 0.3, 0.3, 0.05);
                }

                if (!player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, player, hand);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, hand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.soap.desc").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
    }
}
