package net.yigitguven.chymistry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class CrucibleBlockEntity extends BlockEntity {
    public float currentHeat = 0.0f;
    public int progress = 0;
    public int maxProgress = 0;

    private final net.minecraft.world.item.crafting.RecipeManager.CachedCheck<net.yigitguven.chymistry.recipe.CrucibleRecipeInput, net.yigitguven.chymistry.recipe.CrucibleRecipe> quickCheck = 
            net.minecraft.world.item.crafting.RecipeManager.createCheck(net.yigitguven.chymistry.recipe.ModRecipes.CRUCIBLE_TYPE.get());

    public final net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(6) {
        @Override
        public void setChanged() {
            super.setChanged();
            CrucibleBlockEntity.this.setChanged();
        }
    };

    public final net.minecraft.world.inventory.ContainerData data = new net.minecraft.world.inventory.ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> CrucibleBlockEntity.this.progress;
                case 1 -> CrucibleBlockEntity.this.maxProgress;
                case 2 -> (int) (CrucibleBlockEntity.this.currentHeat * 10);
                case 3 -> {
                    if (CrucibleBlockEntity.this.getBlockState().getBlock() instanceof CrucibleBlock crucible) {
                        yield crucible.getMaxHeat() * 10;
                    }
                    yield 1000;
                }
                case 4 -> {
                    if (CrucibleBlockEntity.this.getBlockState().getBlock() instanceof CrucibleBlock crucible) {
                        yield crucible.getMinHeat() * 10;
                    }
                    yield -1000;
                }
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> CrucibleBlockEntity.this.progress = pValue;
                case 1 -> CrucibleBlockEntity.this.maxProgress = pValue;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    public CrucibleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CRUCIBLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(ValueOutput pOutput) {
        super.saveAdditional(pOutput);
        pOutput.putFloat("heat", this.currentHeat);
        pOutput.putInt("progress", this.progress);
        pOutput.putInt("maxProgress", this.maxProgress);
        net.minecraft.world.ContainerHelper.saveAllItems(pOutput, this.inventory.getItems());
    }

    @Override
    protected void loadAdditional(ValueInput pInput) {
        super.loadAdditional(pInput);
        this.currentHeat = pInput.getFloatOr("heat", 0.0f);
        this.progress = pInput.getIntOr("progress", 0);
        this.maxProgress = pInput.getIntOr("maxProgress", 0);
        net.minecraft.world.ContainerHelper.loadAllItems(pInput, this.inventory.getItems());
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
            if (blockEntity.getLevel() != null && !level.isClientSide()) {
                level.sendBlockUpdated(pos, state, state, net.minecraft.world.level.block.Block.UPDATE_ALL);
            }
        }

        if (!level.isClientSide()) {
            java.util.List<net.minecraft.world.item.ItemStack> inputStacks = new java.util.ArrayList<>();
            for (int i = 0; i < 5; i++) {
                inputStacks.add(blockEntity.inventory.getItem(i));
            }
            net.yigitguven.chymistry.recipe.CrucibleRecipeInput recipeInput = new net.yigitguven.chymistry.recipe.CrucibleRecipeInput(inputStacks);
            
            java.util.Optional<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CrucibleRecipe>> match = blockEntity.quickCheck.getRecipeFor(recipeInput, (net.minecraft.server.level.ServerLevel) level);

            if (match.isPresent()) {
                net.yigitguven.chymistry.recipe.CrucibleRecipe recipe = match.get().value();
                if (blockEntity.currentHeat >= recipe.minHeat() && blockEntity.currentHeat <= recipe.maxHeat()) {
                    blockEntity.maxProgress = recipe.processingTime();
                    blockEntity.progress++;
                    if (blockEntity.progress >= blockEntity.maxProgress) {
                        // Consume inputs
                        for (net.yigitguven.chymistry.recipe.SizedIngredient ingredient : recipe.inputs()) {
                            for (int i = 0; i < 4; i++) { // Only consume from first 4 slots for materials
                                net.minecraft.world.item.ItemStack stack = blockEntity.inventory.getItem(i);
                                if (ingredient.test(stack)) {
                                    if (stack.getCount() == ingredient.count() && stack.getItem().getCraftingRemainder() != null) {
                                        blockEntity.inventory.setItem(i, stack.getItem().getCraftingRemainder().create());
                                    } else {
                                        stack.shrink(ingredient.count());
                                    }
                                    break;
                                }
                            }
                        }

                        // Consume container
                        if (recipe.container().isPresent()) {
                            net.minecraft.world.item.ItemStack containerStack = blockEntity.inventory.getItem(4);
                            if (recipe.container().get().test(containerStack)) {
                                containerStack.shrink(recipe.container().get().count());
                            }
                        }

                        // Produce output
                        net.minecraft.world.item.ItemStack outputStack = recipe.assemble(recipeInput);
                        net.minecraft.world.item.ItemStack existingOutput = blockEntity.inventory.getItem(5);
                        if (existingOutput.isEmpty()) {
                            blockEntity.inventory.setItem(5, outputStack.copy());
                        } else if (net.minecraft.world.item.ItemStack.isSameItemSameComponents(existingOutput, outputStack)) {
                            existingOutput.grow(outputStack.getCount());
                        }

                        blockEntity.currentHeat -= recipe.heatCost();
                        if (blockEntity.currentHeat < minHeat) blockEntity.currentHeat = minHeat;

                        blockEntity.progress = 0;
                    }
                    blockEntity.setChanged();
                } else {
                    if (blockEntity.progress > 0) {
                        blockEntity.progress = Math.max(0, blockEntity.progress - 2); // Cool down progress
                        blockEntity.setChanged();
                    }
                }
            } else {
                if (blockEntity.progress > 0) {
                    blockEntity.progress = 0;
                    blockEntity.maxProgress = 0;
                    blockEntity.setChanged();
                }
            }
        }
    }
}
