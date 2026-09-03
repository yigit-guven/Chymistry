package net.yigitguven.chymistry.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.yigitguven.chymistry.Chymistry;

import java.util.function.Consumer;

public class DefoliantPotionItem extends Item {

    public static final TagKey<Block> HERBS_BLOCK_TAG = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Chymistry.MODID, "herbs"));

    public DefoliantPotionItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return super.getName(pStack).copy().withStyle(style -> style.withColor(0x44B029));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);

        if (!isSoil(clickedState)) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            int radius = 10;
            BlockPos from = clickedPos.offset(-radius, -radius, -radius);
            BlockPos to = clickedPos.offset(radius, radius, radius);

            for (BlockPos pos : BlockPos.betweenClosed(from, to)) {
                BlockState targetState = level.getBlockState(pos);
                if (isDefoliantTarget(targetState)) {
                    level.removeBlock(pos, false);
                }
            }

            serverLevel.sendParticles(ParticleTypes.SNEEZE, clickedPos.getX() + 0.5, clickedPos.getY() + 1.0, clickedPos.getZ() + 0.5, 30, 2.0, 1.0, 2.0, 0.05);
            serverLevel.sendParticles(ParticleTypes.SMOKE, clickedPos.getX() + 0.5, clickedPos.getY() + 0.5, clickedPos.getZ() + 0.5, 20, 1.5, 0.5, 1.5, 0.02);

            level.playSound(null, clickedPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.8F, 0.7F);
            level.playSound(null, clickedPos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 0.6F);

            if (player != null && !player.getAbilities().instabuild) {
                stack.shrink(1);
                ItemStack bottle = new ItemStack(ModItems.TINTED_GLASS_BOTTLE.get());
                if (stack.isEmpty()) {
                    player.setItemInHand(context.getHand(), bottle);
                } else if (!player.getInventory().add(bottle)) {
                    player.drop(bottle, false);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static boolean isSoil(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND) || state.is(Blocks.DIRT_PATH);
    }

    public static boolean isDefoliantTarget(BlockState state) {
        if (state.isAir()) {
            return false;
        }
        Block block = state.getBlock();
        if (state.is(BlockTags.LEAVES) || block instanceof LeavesBlock) {
            return true;
        }
        if (state.is(BlockTags.FLOWERS) || state.is(BlockTags.CROPS)) {
            return true;
        }
        if (state.is(HERBS_BLOCK_TAG)) {
            return true;
        }
        return block instanceof BushBlock || block instanceof DoublePlantBlock || block instanceof VineBlock
                || block instanceof GrowingPlantBlock || block instanceof GrowingPlantHeadBlock
                || block instanceof GrowingPlantBodyBlock || block instanceof SugarCaneBlock;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.defoliant.effect").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.accept(Component.empty());
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.defoliant.when_used").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.defoliant.desc").withStyle(ChatFormatting.BLUE));
    }
}
