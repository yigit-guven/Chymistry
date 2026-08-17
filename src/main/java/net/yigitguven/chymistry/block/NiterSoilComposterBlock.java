package net.yigitguven.chymistry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.yigitguven.chymistry.item.ModItems;

public class NiterSoilComposterBlock extends Block {
    public static final MapCodec<NiterSoilComposterBlock> CODEC = simpleCodec(NiterSoilComposterBlock::new);
    
    private static final VoxelShape INSIDE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape SHAPE = Shapes.join(Shapes.block(), INSIDE, BooleanOp.ONLY_FIRST);
    private static final VoxelShape FULL_SHAPE = Shapes.or(SHAPE, Block.box(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D));

    public NiterSoilComposterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            popResource(level, pos, new ItemStack(ModItems.NITER_DUST.get()));
            level.playSound(null, pos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, Blocks.COMPOSTER.defaultBlockState(), 3);
        }
        return InteractionResult.SUCCESS;
    }
}
