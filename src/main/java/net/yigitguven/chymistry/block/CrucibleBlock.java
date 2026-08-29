package net.yigitguven.chymistry.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.yigitguven.chymistry.block.CrucibleBlockEntity;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;

public class CrucibleBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static final MapCodec<CrucibleBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            Codec.INT.fieldOf("max_heat").forGetter(CrucibleBlock::getMaxHeat),
            Codec.INT.fieldOf("min_heat").forGetter(CrucibleBlock::getMinHeat)
    ).apply(instance, CrucibleBlock::new));
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);

    private final int maxHeat;
    private final int minHeat;

    public CrucibleBlock(Properties properties, int maxHeat, int minHeat) {
        super(properties);
        this.maxHeat = maxHeat;
        this.minHeat = minHeat;
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    public int getMaxHeat() {
        return this.maxHeat;
    }

    public int getMinHeat() {
        return this.minHeat;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrucibleBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof CrucibleBlockEntity crucible) {
                CrucibleBlockEntity.tick(lvl, pos, st, crucible);
            }
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            net.minecraft.world.level.LevelReader level,
            net.minecraft.world.level.ScheduledTickAccess ticks,
            BlockPos pos,
            Direction directionToNeighbour,
            BlockPos neighbourPos,
            BlockState neighbourState,
            net.minecraft.util.RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected net.minecraft.world.InteractionResult useWithoutItem(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrucibleBlockEntity crucible) {
                player.openMenu(new net.minecraft.world.MenuProvider() {
                    @Override
                    public net.minecraft.network.chat.Component getDisplayName() {
                        return net.minecraft.network.chat.Component.translatable("container.chymistry.crucible");
                    }

                    @Override
                    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int pContainerId, net.minecraft.world.entity.player.Inventory pInventory, net.minecraft.world.entity.player.Player pPlayer) {
                        return new net.yigitguven.chymistry.menu.CrucibleMenu(pContainerId, pInventory, crucible.inventory, crucible.data, net.minecraft.world.inventory.ContainerLevelAccess.create(level, pos));
                    }
                }, pos);
            }
        }
        return net.minecraft.world.InteractionResult.SUCCESS;
    }
}
