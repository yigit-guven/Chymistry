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
}
