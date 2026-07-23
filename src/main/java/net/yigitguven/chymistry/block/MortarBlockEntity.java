package net.yigitguven.chymistry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.yigitguven.chymistry.recipe.MortarRecipe;
import net.yigitguven.chymistry.recipe.ModRecipes;
import net.yigitguven.chymistry.menu.MortarMenu;

import java.util.Optional;

public class MortarBlockEntity extends BaseContainerBlockEntity {

    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private int currentPresses = 0;

    public MortarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MORTAR_BE.get(), pos, state);
    }

    public void handleMeshPress() {
        if (this.level == null || this.level.isClientSide()) return;

        SingleRecipeInput input = new SingleRecipeInput(this.getItem(0));
        Optional<RecipeHolder<MortarRecipe>> recipe = ((net.minecraft.server.level.ServerLevel)this.level).recipeAccess().getRecipeFor(ModRecipes.MORTAR_TYPE.get(), input, this.level);

        if (recipe.isPresent()) {
            this.currentPresses++;
            if (this.currentPresses >= recipe.get().value().presses()) {
                // Craft
                ItemStack result = recipe.get().value().assemble(input);
                ItemStack outputSlot = this.getItem(1);

                if (outputSlot.isEmpty() || (ItemStack.isSameItemSameComponents(outputSlot, result) && outputSlot.getCount() + result.getCount() <= this.getMaxStackSize())) {
                    this.getItem(0).shrink(1);
                    if (outputSlot.isEmpty()) {
                        this.setItem(1, result.copy());
                    } else {
                        outputSlot.grow(result.getCount());
                    }
                    this.currentPresses = 0; // Reset
                } else {
                    this.currentPresses--; // Revert if output is full
                }
            }
            setChanged();
        } else {
            this.currentPresses = 0; // Reset if invalid
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("currentPresses", currentPresses);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        currentPresses = input.getIntOr("currentPresses", 0);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.chymistry.mortar");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new MortarMenu(id, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
