package net.yigitguven.chymistry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.yigitguven.chymistry.item.ModItems;

public class CyanDyeComposterBlock extends Block {
    public static final MapCodec<CyanDyeComposterBlock> CODEC = simpleCodec(CyanDyeComposterBlock::new);

    public static final BooleanProperty ALCOHOL = BooleanProperty.create("alcohol");
    public static final BooleanProperty RAW_COPPER = BooleanProperty.create("raw_copper");

    private static final VoxelShape INSIDE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape SHAPE = Shapes.join(Shapes.block(), INSIDE, BooleanOp.ONLY_FIRST);
    private static final VoxelShape FULL_SHAPE = Shapes.or(SHAPE, Block.box(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D));

    public CyanDyeComposterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ALCOHOL, false)
                .setValue(RAW_COPPER, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ALCOHOL, RAW_COPPER);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FULL_SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean changed = false;
        BlockState newState = state;

        if (stack.is(ModItems.ALCOHOL_BOTTLE.get()) && !state.getValue(ALCOHOL)) {
            if (!player.isCreative()) {
                stack.shrink(1);
                ItemStack bottle = new ItemStack(ModItems.REINFORCED_GLASS_BOTTLE.get());
                if (!player.getInventory().add(bottle)) {
                    player.drop(bottle, false);
                }
            }
            newState = newState.setValue(ALCOHOL, true);
            changed = true;
        } else if (stack.is(Items.RAW_COPPER) && !state.getValue(RAW_COPPER)) {
            if (!player.isCreative()) stack.shrink(1);
            newState = newState.setValue(RAW_COPPER, true);
            changed = true;
        }

        if (changed) {
            if (!level.isClientSide()) {
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (newState.getValue(ALCOHOL) && newState.getValue(RAW_COPPER)) {
                    level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.7F, 1.2F);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, 20, 0.3, 0.2, 0.3, 0.05);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            if (state.getValue(ALCOHOL) && state.getValue(RAW_COPPER)) {
                popResource(level, pos, new ItemStack(Items.DYE.cyan(), 2));
            } else if (state.getValue(ALCOHOL)) {
                popResource(level, pos, new ItemStack(ModItems.ALCOHOL_BOTTLE.get()));
            } else if (state.getValue(RAW_COPPER)) {
                popResource(level, pos, new ItemStack(Items.RAW_COPPER));
            }
            level.playSound(null, pos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, Blocks.COMPOSTER.defaultBlockState(), 3);
        }
        return InteractionResult.SUCCESS;
    }
}
