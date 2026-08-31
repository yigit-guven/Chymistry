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

    private final net.minecraft.world.item.crafting.RecipeManager.CachedCheck<net.yigitguven.chymistry.recipe.AlembicRecipeInput, net.yigitguven.chymistry.recipe.AlembicRecipe> quickCheck = 
            net.minecraft.world.item.crafting.RecipeManager.createCheck(net.yigitguven.chymistry.recipe.ModRecipes.ALEMBIC_TYPE.get());

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
        if (level.isClientSide()) return;

        boolean changed = false;

        // Decrease fuel time
        if (this.fuelTime > 0) {
            this.fuelTime--;
            changed = true;
        }

        // Build Recipe Input
        java.util.List<net.minecraft.world.item.ItemStack> inputList = new java.util.ArrayList<>();
        for (int i = 0; i < 4; i++) {
            inputList.add(this.inventory.getItem(i));
        }
        net.yigitguven.chymistry.recipe.AlembicRecipeInput recipeInput = new net.yigitguven.chymistry.recipe.AlembicRecipeInput(inputList, this.inventory.getItem(4), this.inventory.getItem(5));

        // Find Recipe
        var recipeHolder = this.quickCheck.getRecipeFor(recipeInput, (net.minecraft.server.level.ServerLevel) level).orElse(null);

        if (recipeHolder != null) {
            net.yigitguven.chymistry.recipe.AlembicRecipe recipe = recipeHolder.value();
            
            // Check if we need fuel and have fuel item
            if (this.fuelTime <= 0) {
                net.minecraft.world.item.ItemStack fuelStack = this.inventory.getItem(4);
                if (!fuelStack.isEmpty()) {
                    int burnDuration = level.fuelValues().burnDuration(fuelStack);
                    if (burnDuration > 0) {
                        this.fuelTime = burnDuration;
                        this.maxFuelTime = burnDuration;
                        fuelStack.shrink(1);
                        changed = true;
                    }
                }
            }

            // If we have fuel, check output capacity
            if (this.fuelTime > 0) {
                net.minecraft.world.item.ItemStack outputStack = this.inventory.getItem(5);
                net.minecraft.world.item.ItemStack recipeOutput = recipe.output().create();
                
                boolean canOutputItem = outputStack.isEmpty() || (net.minecraft.world.item.ItemStack.isSameItemSameComponents(outputStack, recipeOutput) && outputStack.getCount() + recipeOutput.getCount() <= outputStack.getMaxStackSize());
                
                boolean hasAvailableBottle = false;
                net.minecraft.core.Direction targetBottleDir = null;
                
                if (recipe.secondaryOutput().isPresent()) {
                    BottleConnection connection = state.getValue(AlembicBlock.CONNECTION);
                    if (connection != BottleConnection.NONE) {
                        net.minecraft.core.Direction dir = switch (connection) {
                            case NORTH -> net.minecraft.core.Direction.NORTH;
                            case EAST -> net.minecraft.core.Direction.EAST;
                            case SOUTH -> net.minecraft.core.Direction.SOUTH;
                            case WEST -> net.minecraft.core.Direction.WEST;
                            default -> null;
                        };
                        if (dir != null) {
                            BlockEntity be = level.getBlockEntity(pos.relative(dir));
                            if (be instanceof PlacedBottleBlockEntity bottleBE && bottleBE.getStoredItem().isEmpty()) {
                                hasAvailableBottle = true;
                                targetBottleDir = dir;
                            }
                        }
                    }
                    
                    if (!hasAvailableBottle) {
                        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                            BlockEntity be = level.getBlockEntity(pos.relative(dir));
                            if (be instanceof PlacedBottleBlockEntity bottleBE && bottleBE.getStoredItem().isEmpty()) {
                                hasAvailableBottle = true;
                                targetBottleDir = dir;
                                BottleConnection newConn = switch(dir) {
                                    case NORTH -> BottleConnection.NORTH;
                                    case EAST -> BottleConnection.EAST;
                                    case SOUTH -> BottleConnection.SOUTH;
                                    case WEST -> BottleConnection.WEST;
                                    default -> BottleConnection.NONE;
                                };
                                state = state.setValue(AlembicBlock.CONNECTION, newConn);
                                level.setBlock(pos, state, 3);
                                changed = true;
                                break;
                            }
                        }
                        
                        if (!hasAvailableBottle && connection != BottleConnection.NONE) {
                            // No empty bottle found around, disconnect so UI gas bar stops
                            state = state.setValue(AlembicBlock.CONNECTION, BottleConnection.NONE);
                            level.setBlock(pos, state, 3);
                            changed = true;
                        }
                    }
                }
                
                if (canOutputItem) {
                    this.progress++;
                    this.maxProgress = recipe.processingTime();
                    
                    if (this.progress >= this.maxProgress) {
                        // Consume inputs
                        for (net.yigitguven.chymistry.recipe.SizedIngredient ingredient : recipe.inputs()) {
                            int toConsume = ingredient.count();
                            for (int i = 0; i < 4; i++) {
                                net.minecraft.world.item.ItemStack stack = this.inventory.getItem(i);
                                if (ingredient.ingredient().test(stack)) {
                                    int consumed = Math.min(toConsume, stack.getCount());
                                    stack.shrink(consumed);
                                    toConsume -= consumed;
                                    if (toConsume <= 0) break;
                                }
                            }
                        }

                        // Add output
                        if (outputStack.isEmpty()) {
                            this.inventory.setItem(5, recipeOutput.copy());
                        } else {
                            outputStack.grow(recipeOutput.getCount());
                        }
                        
                        // Handle secondary output (liquid)
                        if (recipe.secondaryOutput().isPresent() && targetBottleDir != null) {
                            BlockEntity be = level.getBlockEntity(pos.relative(targetBottleDir));
                            if (be instanceof PlacedBottleBlockEntity bottleBE) {
                                bottleBE.setStoredItem(recipe.secondaryOutput().get().create().copy());
                            }
                        }

                        this.progress = 0;
                    }
                    changed = true;
                } else {
                    if (this.progress > 0) {
                        this.progress = 0;
                        changed = true;
                    }
                }
            } else {
                if (this.progress > 0) {
                    this.progress = 0;
                    changed = true;
                }
            }
        } else {
            if (this.progress > 0) {
                this.progress = 0;
                changed = true;
            }
        }

        boolean isLit = state.getValue(AlembicBlock.LIT);
        boolean shouldBeLit = this.fuelTime > 0;
        if (isLit != shouldBeLit) {
            BlockState newState = state.setValue(AlembicBlock.LIT, shouldBeLit);
            level.setBlock(pos, newState, 3);
            changed = true;
        }

        if (changed) {
            this.setChanged();
        }
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
