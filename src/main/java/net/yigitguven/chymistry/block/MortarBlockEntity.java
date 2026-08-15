package net.yigitguven.chymistry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
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
import net.yigitguven.chymistry.recipe.MortarRecipeInput;

import java.util.Optional;

public class MortarBlockEntity extends BaseContainerBlockEntity {

    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private int currentPresses = 0;
    private int maxPresses = 0; // 0 means no recipe, -1 means invalid recipe
    private long lastClickTime = 0;
    protected final ContainerData data;

    public MortarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MORTAR_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> MortarBlockEntity.this.currentPresses;
                    case 1 -> MortarBlockEntity.this.maxPresses;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> MortarBlockEntity.this.currentPresses = pValue;
                    case 1 -> MortarBlockEntity.this.maxPresses = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void handleMeshPress() {
        if (this.level == null || this.level.isClientSide()) return;

        long currentTime = this.level.getGameTime();
        long timeSinceLastClick = this.lastClickTime > 0 ? (currentTime - this.lastClickTime) : -1;
        this.lastClickTime = currentTime;

        MortarRecipeInput input = new MortarRecipeInput(this.getItem(0), this.getItem(1), this.getItem(2));
        Optional<RecipeHolder<MortarRecipe>> recipe = ((net.minecraft.server.level.ServerLevel)this.level).recipeAccess().getRecipeFor(ModRecipes.MORTAR_TYPE.get(), input, this.level);

        if (this.getItem(0).isEmpty() && this.getItem(1).isEmpty() && this.getItem(2).isEmpty()) {
            this.maxPresses = 0;
            this.currentPresses = 0;
        } else if (recipe.isEmpty()) {
            this.maxPresses = -1; // -1 indicates error (invalid recipe for input)
            this.currentPresses = 0;
        } else {
            MortarRecipe activeRecipe = recipe.get().value();
            boolean timingValid = true;

            if (this.currentPresses > 0 && timeSinceLastClick >= 0) { // first click is always valid
                switch (activeRecipe.clickType()) {
                    case FAST:
                        if (timeSinceLastClick > 10) timingValid = false;
                        break;
                    case SLOW:
                        if (timeSinceLastClick < 20) timingValid = false;
                        break;
                    case ANY:
                    default:
                        break;
                }
            }

            if (!timingValid) {
                // If it's a gunpowder recipe, it explodes!
                if (activeRecipe.output().create().is(net.minecraft.world.item.Items.GUNPOWDER)) {
                    int gunpowderCount = this.getItem(3).is(net.minecraft.world.item.Items.GUNPOWDER) ? this.getItem(3).getCount() : 0;
                    float radius = 1.0f + (gunpowderCount / 64.0f) * 3.0f;
                    
                    this.level.explode(null, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, radius, net.minecraft.world.level.Level.ExplosionInteraction.BLOCK);
                    
                    // Reset mortar and clear items
                    for (int i = 0; i < 4; i++) {
                        this.setItem(i, net.minecraft.world.item.ItemStack.EMPTY);
                    }
                    this.currentPresses = 0;
                    this.maxPresses = 0;
                }
                return;
            }

            this.maxPresses = activeRecipe.presses();
            this.currentPresses++;
            
            // Play crushing sound
            this.level.playSound(null, this.worldPosition, SoundEvents.GRAVEL_STEP, SoundSource.BLOCKS, 1.0f, 1.0f);

            if (this.currentPresses >= this.maxPresses) {
                // Craft
                ItemStack result = activeRecipe.assemble(input);
                ItemStack outputSlot = this.getItem(3);

                if (outputSlot.isEmpty() || (ItemStack.isSameItemSameComponents(outputSlot, result) && outputSlot.getCount() + result.getCount() <= this.getMaxStackSize())) {
                    for (int i = 0; i < activeRecipe.inputs().size(); i++) {
                        this.getItem(i).shrink(activeRecipe.inputs().get(i).count());
                    }
                    if (outputSlot.isEmpty()) {
                        this.setItem(3, result.copy());
                    } else {
                        outputSlot.grow(result.getCount());
                    }
                    this.currentPresses = 0; // Reset
                    
                    // Reset max presses if inputs are now empty
                    if (this.getItem(0).isEmpty() && this.getItem(1).isEmpty() && this.getItem(2).isEmpty()) {
                        this.maxPresses = 0;
                    }
                } else {
                    this.currentPresses--; // Revert if output is full
                }
            }
        }
        setChanged();
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
        return new MortarMenu(id, playerInventory, this, this.data);
    }

    @Override
    public int getContainerSize() {
        return 4;
    }
}
