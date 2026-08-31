package net.yigitguven.chymistry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.yigitguven.chymistry.recipe.ModRecipes;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.yigitguven.chymistry.menu.AlembicMenu;
import javax.annotation.Nullable;

public class AlembicBlockEntity extends BlockEntity implements MenuProvider {
    public int progress = 0;
    public int maxProgress = 0;
    public int fuelTime = 0;
    public int maxFuelTime = 0;

    public final net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(6) {
        @Override
        public void setChanged() {
            super.setChanged();
            AlembicBlockEntity.this.setChanged();
        }
    };

    public final net.minecraft.world.inventory.ContainerData data = new net.minecraft.world.inventory.ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> AlembicBlockEntity.this.progress;
                case 1 -> AlembicBlockEntity.this.maxProgress;
                case 2 -> AlembicBlockEntity.this.fuelTime;
                case 3 -> AlembicBlockEntity.this.maxFuelTime;
                case 4 -> AlembicBlockEntity.this.getBlockState().getValue(AlembicBlock.CONNECTION) != BottleConnection.NONE ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> AlembicBlockEntity.this.progress = pValue;
                case 1 -> AlembicBlockEntity.this.maxProgress = pValue;
                case 2 -> AlembicBlockEntity.this.fuelTime = pValue;
                case 3 -> AlembicBlockEntity.this.maxFuelTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    public AlembicBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ALEMBIC.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput pOutput) {
        super.saveAdditional(pOutput);
        pOutput.putInt("progress", this.progress);
        pOutput.putInt("maxProgress", this.maxProgress);
        pOutput.putInt("fuelTime", this.fuelTime);
        pOutput.putInt("maxFuelTime", this.maxFuelTime);
        net.minecraft.world.ContainerHelper.saveAllItems(pOutput, this.inventory.getItems());
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput pInput) {
        super.loadAdditional(pInput);
        this.progress = pInput.getIntOr("progress", 0);
        this.maxProgress = pInput.getIntOr("maxProgress", 0);
        this.fuelTime = pInput.getIntOr("fuelTime", 0);
        this.maxFuelTime = pInput.getIntOr("maxFuelTime", 0);
        net.minecraft.world.ContainerHelper.loadAllItems(pInput, this.inventory.getItems());
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        // Implement tick logic when recipe is added
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.chymistry.alembic");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AlembicMenu(pContainerId, pPlayerInventory, this, this.inventory, this.data);
    }
}
