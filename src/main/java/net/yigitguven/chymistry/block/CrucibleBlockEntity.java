package net.yigitguven.chymistry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class CrucibleBlockEntity extends BlockEntity {
    public float currentHeat = 0.0f;

    public CrucibleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CRUCIBLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(ValueOutput pOutput) {
        super.saveAdditional(pOutput);
        pOutput.putFloat("heat", this.currentHeat);
    }

    @Override
    protected void loadAdditional(ValueInput pInput) {
        super.loadAdditional(pInput);
        this.currentHeat = pInput.getFloatOr("heat", 0.0f);
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public net.minecraft.nbt.CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CrucibleBlockEntity blockEntity) {
        if (level.isClientSide()) return;

        BlockState below = level.getBlockState(pos.below());
        float heatChange = 0.0f;
        boolean hasSource = false;

        if (below.is(net.minecraft.world.level.block.Blocks.LAVA)) {
            heatChange = 0.1665f;
            hasSource = true;
        } else if (below.is(net.minecraft.world.level.block.Blocks.CAMPFIRE) || 
                   below.is(net.minecraft.world.level.block.Blocks.SOUL_CAMPFIRE) || 
                   below.is(net.minecraft.world.level.block.Blocks.FIRE) || 
                   below.is(net.minecraft.world.level.block.Blocks.SOUL_FIRE)) {
            heatChange = 0.08f;
            hasSource = true;
        } else if (below.is(net.minecraft.world.level.block.Blocks.MAGMA_BLOCK)) {
            heatChange = 0.04f;
            hasSource = true;
        } else if (below.is(net.minecraft.world.level.block.Blocks.BLUE_ICE)) {
            heatChange = -0.1665f;
            hasSource = true;
        } else if (below.is(net.minecraft.world.level.block.Blocks.PACKED_ICE)) {
            heatChange = -0.08f;
            hasSource = true;
        } else if (below.is(net.minecraft.world.level.block.Blocks.ICE) || 
                   below.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK)) {
            heatChange = -0.04f;
            hasSource = true;
        }

        boolean isWaterlogged = state.getValue(CrucibleBlock.WATERLOGGED);

        // Water prevents heating/cooling and forcefully normalizes to 0
        if (isWaterlogged || !hasSource) {
            float normalizationRate = isWaterlogged ? 0.5f : 0.1f;
            if (blockEntity.currentHeat > 0) {
                heatChange = -normalizationRate;
                if (blockEntity.currentHeat + heatChange < 0) heatChange = -blockEntity.currentHeat;
            } else if (blockEntity.currentHeat < 0) {
                heatChange = normalizationRate;
                if (blockEntity.currentHeat + heatChange > 0) heatChange = -blockEntity.currentHeat;
            } else {
                heatChange = 0.0f;
            }
        }

        if (heatChange == 0.0f && blockEntity.currentHeat == 0.0f) {
            return;
        }

        float oldHeat = blockEntity.currentHeat;
        blockEntity.currentHeat += heatChange;
        
        int maxHeat = 0;
        int minHeat = 0;
        if (state.getBlock() instanceof CrucibleBlock crucible) {
            maxHeat = crucible.getMaxHeat();
            minHeat = crucible.getMinHeat();
        }
        
        if (blockEntity.currentHeat > maxHeat) blockEntity.currentHeat = maxHeat;
        if (blockEntity.currentHeat < minHeat) blockEntity.currentHeat = minHeat;

        if (oldHeat != blockEntity.currentHeat) {
            blockEntity.setChanged();
            level.sendBlockUpdated(pos, state, state, net.minecraft.world.level.block.Block.UPDATE_ALL);
        }
    }
}
