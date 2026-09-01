package net.yigitguven.chymistry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlacedBottleBlockEntity extends BlockEntity {
    public final net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            if (PlacedBottleBlockEntity.this.level != null && !PlacedBottleBlockEntity.this.level.isClientSide()) {
                BlockState state = PlacedBottleBlockEntity.this.getBlockState();
                if (state.hasProperty(PlacedBottleBlock.FILLED)) {
                    boolean isFilled = !this.getItem(0).isEmpty();
                    if (state.getValue(PlacedBottleBlock.FILLED) != isFilled) {
                        BlockState newState = state.setValue(PlacedBottleBlock.FILLED, isFilled);
                        PlacedBottleBlockEntity.this.level.setBlock(PlacedBottleBlockEntity.this.worldPosition, newState, 3);
                    }
                }
                PlacedBottleBlockEntity.this.level.sendBlockUpdated(PlacedBottleBlockEntity.this.worldPosition, state, state, 3);
            }
            PlacedBottleBlockEntity.this.setChanged();
        }
    };

    public PlacedBottleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PLACED_BOTTLE_BE.get(), pPos, pBlockState);
    }

    public void setStoredItem(ItemStack item) {
        this.inventory.setItem(0, item);
    }

    public ItemStack getStoredItem() {
        return this.inventory.getItem(0);
    }

    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput pOutput) {
        super.saveAdditional(pOutput);
        net.minecraft.world.ContainerHelper.saveAllItems(pOutput, this.inventory.getItems());
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput pInput) {
        super.loadAdditional(pInput);
        net.minecraft.world.ContainerHelper.loadAllItems(pInput, this.inventory.getItems());
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public net.minecraft.nbt.CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider pRegistries) {
        return this.saveCustomOnly(pRegistries);
    }
}
