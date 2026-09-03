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
    public int overheatTicks = 0;

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
        pOutput.putInt("overheatTicks", this.overheatTicks);
        net.minecraft.world.ContainerHelper.saveAllItems(pOutput, this.inventory.getItems());
    }

    @Override
    protected void loadAdditional(ValueInput pInput) {
        super.loadAdditional(pInput);
        this.currentHeat = pInput.getFloatOr("heat", 0.0f);
        this.progress = pInput.getIntOr("progress", 0);
        this.maxProgress = pInput.getIntOr("maxProgress", 0);
        this.overheatTicks = pInput.getIntOr("overheatTicks", 0);
        net.minecraft.world.ContainerHelper.loadAllItems(pInput, this.inventory.getItems());
    }

    public static final java.util.Map<net.minecraft.resources.ResourceKey<Level>, java.util.Set<BlockPos>> ACTIVE_REPELLENTS = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level != null && !this.level.isClientSide()) {
            java.util.Set<BlockPos> set = ACTIVE_REPELLENTS.get(this.level.dimension());
            if (set != null) {
                set.remove(this.worldPosition);
            }
        }
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
            boolean isRepellentBurning = false;
            if (blockEntity.currentHeat >= 20.0f) {
                for (int i = 0; i < 4; i++) {
                    net.minecraft.world.item.ItemStack stack = blockEntity.inventory.getItem(i);
                    if (!stack.isEmpty() && stack.is(net.yigitguven.chymistry.item.ModItems.REPELLENT_BASE.get())) {
                        isRepellentBurning = true;

                        blockEntity.currentHeat = Math.max(0.0f, blockEntity.currentHeat - 0.08f);

                        if (level.getGameTime() % 20 == 0) {
                            int newDamage = stack.getDamageValue() + 1;
                            if (newDamage >= stack.getMaxDamage()) {
                                stack.shrink(1);
                                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH, net.minecraft.sounds.SoundSource.BLOCKS, 0.8f, 1.2f);
                            } else {
                                stack.setDamageValue(newDamage);
                            }
                            blockEntity.inventory.setChanged();
                        }

                        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel && level.getGameTime() % 4 == 0) {
                            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIMSON_SPORE, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, 3, 0.25, 0.2, 0.25, 0.01);
                            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, 2, 0.15, 0.15, 0.15, 0.02);
                        }

                        if (level.getGameTime() % 60 == 0) {
                            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.CANDLE_AMBIENT, net.minecraft.sounds.SoundSource.BLOCKS, 0.5f, 0.8f);
                        }

                        break;
                    }
                }
            }

            java.util.Set<BlockPos> dimensionRepellents = ACTIVE_REPELLENTS.computeIfAbsent(level.dimension(), k -> java.util.concurrent.ConcurrentHashMap.newKeySet());
            if (isRepellentBurning) {
                dimensionRepellents.add(pos.immutable());
            } else {
                dimensionRepellents.remove(pos);
            }

            java.util.List<net.minecraft.world.item.ItemStack> inputStacks = new java.util.ArrayList<>();
            for (int i = 0; i < 5; i++) {
                inputStacks.add(blockEntity.inventory.getItem(i));
            }
            net.yigitguven.chymistry.recipe.CrucibleRecipeInput recipeInput = new net.yigitguven.chymistry.recipe.CrucibleRecipeInput(inputStacks);
            
            java.util.Optional<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CrucibleRecipe>> match = blockEntity.quickCheck.getRecipeFor(recipeInput, (net.minecraft.server.level.ServerLevel) level);

            if (match.isPresent()) {
                net.yigitguven.chymistry.recipe.CrucibleRecipe recipe = match.get().value();
                if (blockEntity.currentHeat >= recipe.minHeat() && blockEntity.currentHeat <= recipe.maxHeat()) {
                    blockEntity.overheatTicks = Math.max(0, blockEntity.overheatTicks - 1);
                    blockEntity.maxProgress = recipe.processingTime();
                    blockEntity.progress++;
                    if (blockEntity.progress >= blockEntity.maxProgress) {
                        // Consume inputs
                        for (net.yigitguven.chymistry.recipe.SizedIngredient ingredient : recipe.inputs()) {
                            int toConsume = ingredient.count();
                            for (int i = 0; i < 4; i++) {
                                net.minecraft.world.item.ItemStack stack = blockEntity.inventory.getItem(i);
                                if (!stack.isEmpty() && ingredient.ingredient().test(stack)) {
                                    if (stack.getCount() <= toConsume) {
                                        int countInSlot = stack.getCount();
                                        if (stack.getItem().getCraftingRemainder() != null) {
                                            blockEntity.inventory.setItem(i, stack.getItem().getCraftingRemainder().create());
                                        } else {
                                            stack.shrink(countInSlot);
                                        }
                                        toConsume -= countInSlot;
                                    } else {
                                        stack.shrink(toConsume);
                                        toConsume = 0;
                                    }
                                    if (toConsume <= 0) break;
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
                    if (blockEntity.currentHeat > recipe.maxHeat() && recipe.overheat().isPresent()) {
                        blockEntity.overheatTicks++;
                        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel && blockEntity.overheatTicks % 5 == 0) {
                            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 4, 0.15, 0.1, 0.15, 0.02);
                        }
                        if (blockEntity.overheatTicks >= 40) {
                            String hazard = recipe.overheat().get();
                            if ("toxic_cloud".equals(hazard)) {
                                net.minecraft.world.entity.AreaEffectCloud cloud = new net.minecraft.world.entity.AreaEffectCloud(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5);
                                cloud.setRadius(3.0F);
                                cloud.setDuration(160);
                                cloud.setWaitTime(0);
                                cloud.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.POISON, 140, 1));
                                cloud.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.NAUSEA, 140, 0));
                                level.addFreshEntity(cloud);

                                if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                                    serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 12, 0.3, 0.2, 0.3, 0.05);
                                }
                                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 0.8F);
                            } else if ("explosion".equals(hazard)) {
                                level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2.0F, Level.ExplosionInteraction.BLOCK);
                            }

                            for (int i = 0; i < 4; i++) {
                                net.minecraft.world.item.ItemStack stack = blockEntity.inventory.getItem(i);
                                if (!stack.isEmpty()) {
                                    if (stack.getItem().getCraftingRemainder() != null) {
                                        blockEntity.inventory.setItem(i, stack.getItem().getCraftingRemainder().create());
                                    } else {
                                        blockEntity.inventory.setItem(i, net.minecraft.world.item.ItemStack.EMPTY);
                                    }
                                }
                            }
                            blockEntity.progress = 0;
                            blockEntity.maxProgress = 0;
                            blockEntity.overheatTicks = 0;
                            blockEntity.setChanged();
                        }
                    } else {
                        blockEntity.overheatTicks = Math.max(0, blockEntity.overheatTicks - 1);
                    }

                    if (blockEntity.progress > 0) {
                        blockEntity.progress = Math.max(0, blockEntity.progress - 2); // Cool down progress
                        blockEntity.setChanged();
                    }
                }
            } else {
                blockEntity.overheatTicks = 0;
                if (blockEntity.progress > 0) {
                    blockEntity.progress = 0;
                    blockEntity.maxProgress = 0;
                    blockEntity.setChanged();
                }
            }
        }
    }
}
