package net.yigitguven.chymistry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import javax.annotation.Nullable;

public class AlembicBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<BottleConnection> CONNECTION = EnumProperty.create("connection", BottleConnection.class);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final com.mojang.serialization.MapCodec<AlembicBlock> CODEC = simpleCodec(AlembicBlock::new);

    @Override
    protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public AlembicBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, Boolean.valueOf(false))
                .setValue(CONNECTION, BottleConnection.NONE)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, CONNECTION, HALF, FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return new AlembicBlockEntity(pos, state);
        }
        return null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return null;
        }
        return createTickerHelper(type, ModBlockEntities.ALEMBIC.get(),
                (pLevel, pPos, pState, pBlockEntity) -> pBlockEntity.tick(pLevel, pPos, pState));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        if (level.getBlockState(blockPos.above()).canBeReplaced(context)) {
            BlockState defaultState = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
            return updateConnectionState(defaultState, context.getLevel(), context.getClickedPos());
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    protected BlockState updateShape(BlockState state, net.minecraft.world.level.LevelReader level, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, net.minecraft.util.RandomSource random) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (directionToNeighbour.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (directionToNeighbour == Direction.UP)) {
            if (neighbourState.is(this) && neighbourState.getValue(HALF) != half) {
                if (half == DoubleBlockHalf.UPPER) {
                    return state.setValue(CONNECTION, neighbourState.getValue(CONNECTION)).setValue(FACING, neighbourState.getValue(FACING));
                }
                return state;
            } else {
                return Blocks.AIR.defaultBlockState();
            }
        } else {
            if (half == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        
        BlockState newState = state;
        if (half == DoubleBlockHalf.LOWER) {
            if (hasBottle(level, pos.north())) newState = newState.setValue(CONNECTION, BottleConnection.NORTH);
            else if (hasBottle(level, pos.east())) newState = newState.setValue(CONNECTION, BottleConnection.EAST);
            else if (hasBottle(level, pos.south())) newState = newState.setValue(CONNECTION, BottleConnection.SOUTH);
            else if (hasBottle(level, pos.west())) newState = newState.setValue(CONNECTION, BottleConnection.WEST);
            else newState = newState.setValue(CONNECTION, BottleConnection.NONE);
        }

        return super.updateShape(newState, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            if (player.isCreative()) {
                // If in creative, break the other half without drops
                DoubleBlockHalf half = state.getValue(HALF);
                if (half == DoubleBlockHalf.UPPER) {
                    BlockPos blockpos = pos.below();
                    BlockState blockstate = level.getBlockState(blockpos);
                    if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                        level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
                        level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
                    }
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private BlockState updateConnectionState(BlockState state, net.minecraft.world.level.LevelReader level, BlockPos pos) {
        if (hasBottle(level, pos.north())) return state.setValue(CONNECTION, BottleConnection.NORTH);
        if (hasBottle(level, pos.east())) return state.setValue(CONNECTION, BottleConnection.EAST);
        if (hasBottle(level, pos.south())) return state.setValue(CONNECTION, BottleConnection.SOUTH);
        if (hasBottle(level, pos.west())) return state.setValue(CONNECTION, BottleConnection.WEST);
        return state.setValue(CONNECTION, BottleConnection.NONE);
    }

    private boolean hasBottle(net.minecraft.world.level.LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(ModBlocks.PLACED_BOTTLE.get()) || state.is(ModBlocks.PLACED_TINTED_BOTTLE.get());
    }

    public void onRemove(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AlembicBlockEntity alembicEntity) {
                net.minecraft.world.Containers.dropContents(level, pos, alembicEntity.inventory);
            }
            level.removeBlockEntity(pos);
        }
    }

    @Override
    protected net.minecraft.world.InteractionResult useWithoutItem(BlockState pState, net.minecraft.world.level.Level pLevel, BlockPos pPos, net.minecraft.world.entity.player.Player pPlayer, net.minecraft.world.phys.BlockHitResult pHitResult) {
        if (pLevel.isClientSide()) {
            return net.minecraft.world.InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = pLevel.getBlockEntity(pState.getValue(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below());
            if (blockentity instanceof AlembicBlockEntity alembicBlockEntity) {
                pPlayer.openMenu(alembicBlockEntity);
            }
            return net.minecraft.world.InteractionResult.CONSUME;
        }
    }
}
