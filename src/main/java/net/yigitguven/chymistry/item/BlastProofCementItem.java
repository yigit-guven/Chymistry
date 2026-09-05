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
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class BlastProofCementItem extends BlockItem {

    public BlastProofCementItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Creeper creeper) {
            Level level = player.level();
            if (!level.isClientSide()) {
                creeper.setSwellDir(-1);

                level.playSound(null, creeper.blockPosition(), SoundEvents.SLIME_BLOCK_STEP, SoundSource.PLAYERS, 1.0f, 0.8f);
                level.playSound(null, creeper.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.6f, 1.2f);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.ITEM_SLIME, creeper.getX(), creeper.getY() + 0.5, creeper.getZ(), 15, 0.3, 0.3, 0.3, 0.05);
                    serverLevel.sendParticles(ParticleTypes.SMOKE, creeper.getX(), creeper.getY() + 0.5, creeper.getZ(), 10, 0.2, 0.2, 0.2, 0.02);
                }

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, entity, hand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.blast_proof_cement.desc").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.blast_proof_cement.defuse").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
    }
}
