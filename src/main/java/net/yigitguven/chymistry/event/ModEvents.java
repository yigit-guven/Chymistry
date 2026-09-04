package net.yigitguven.chymistry.event;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.effect.ModMobEffects;

@net.neoforged.fml.common.EventBusSubscriber(modid = Chymistry.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect().equals(MobEffects.POISON) || 
            event.getEffectInstance().getEffect().equals(MobEffects.WITHER)) {
            
            if (event.getEntity().hasEffect(ModMobEffects.VITRIOL_IMMUNITY)) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect().equals(ModMobEffects.VITRIOL_IMMUNITY)) {
            event.getEntity().removeEffect(MobEffects.POISON);
            event.getEntity().removeEffect(MobEffects.WITHER);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().is(net.minecraft.world.item.Items.BUCKET)) {
            net.minecraft.world.phys.BlockHitResult hitResult = net.minecraft.world.item.Item.getPlayerPOVHitResult(event.getLevel(), event.getEntity(), net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);
            if (hitResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                net.minecraft.core.BlockPos pos = hitResult.getBlockPos();
                net.minecraft.world.level.Level level = event.getLevel();
                if (level.getFluidState(pos).is(net.minecraft.tags.FluidTags.WATER)) {
                    if (level.getBiome(pos).is(net.minecraft.tags.BiomeTags.IS_OCEAN)) {
                        net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
                        if (state.getBlock() instanceof net.minecraft.world.level.block.BucketPickup bucketPickup) {
                            net.minecraft.world.item.ItemStack pickup = bucketPickup.pickupBlock(event.getEntity(), level, pos, state);
                            if (!pickup.isEmpty()) {
                                net.minecraft.world.item.ItemStack newBucket = new net.minecraft.world.item.ItemStack(net.yigitguven.chymistry.item.ModItems.SEA_WATER_BUCKET.get());
                                event.getEntity().setItemInHand(event.getHand(), net.minecraft.world.item.ItemUtils.createFilledResult(event.getItemStack(), event.getEntity(), newBucket));
                                event.setCanceled(true);
                                event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
        net.minecraft.world.level.Level level = event.getLevel();
        net.minecraft.core.BlockPos pos = event.getPos();
        net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
        
        if (state.is(net.minecraft.world.level.block.Blocks.COMPOSTER)) {
            if (state.hasProperty(net.minecraft.world.level.block.ComposterBlock.LEVEL)) {
                int composterLevel = state.getValue(net.minecraft.world.level.block.ComposterBlock.LEVEL);
                if (composterLevel == 7 || composterLevel == 8) {
                    net.minecraft.world.item.ItemStack stack = event.getItemStack();
                    boolean hasAsh = stack.is(net.yigitguven.chymistry.item.ModItems.ASH.get());
                    boolean hasNiter = stack.is(net.yigitguven.chymistry.item.ModItems.NITER_DUST.get());
                    boolean hasBoneMeal = stack.is(net.minecraft.world.item.Items.BONE_MEAL);

                    if (hasAsh || hasNiter || hasBoneMeal) {
                        if (!level.isClientSide()) {
                            if (!event.getEntity().isCreative()) {
                                stack.shrink(1);
                            }
                            boolean isBoneMealReady = composterLevel == 8;
                            net.minecraft.world.level.block.state.BlockState composterState = net.yigitguven.chymistry.block.ModBlocks.NITER_SOIL_COMPOSTER.get().defaultBlockState()
                                    .setValue(net.yigitguven.chymistry.block.NiterSoilComposterBlock.ASH, hasAsh)
                                    .setValue(net.yigitguven.chymistry.block.NiterSoilComposterBlock.NITER, hasNiter)
                                    .setValue(net.yigitguven.chymistry.block.NiterSoilComposterBlock.BONEMEAL, hasBoneMeal || isBoneMealReady);

                            level.setBlock(pos, composterState, 3);
                            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.SAND_PLACE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                        }
                        event.setCanceled(true);
                        event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                        return;
                    }

                    boolean hasAlcohol = stack.is(net.yigitguven.chymistry.item.ModItems.ALCOHOL_BOTTLE.get());
                    boolean hasRawCopper = stack.is(net.minecraft.world.item.Items.RAW_COPPER);

                    if (hasAlcohol || hasRawCopper) {
                        if (!level.isClientSide()) {
                            if (!event.getEntity().isCreative()) {
                                stack.shrink(1);
                                if (hasAlcohol) {
                                    net.minecraft.world.item.ItemStack bottle = new net.minecraft.world.item.ItemStack(net.yigitguven.chymistry.item.ModItems.REINFORCED_GLASS_BOTTLE.get());
                                    if (!event.getEntity().getInventory().add(bottle)) {
                                        event.getEntity().drop(bottle, false);
                                    }
                                }
                            }
                            net.minecraft.world.level.block.state.BlockState composterState = net.yigitguven.chymistry.block.ModBlocks.CYAN_DYE_COMPOSTER.get().defaultBlockState()
                                    .setValue(net.yigitguven.chymistry.block.CyanDyeComposterBlock.ALCOHOL, hasAlcohol)
                                    .setValue(net.yigitguven.chymistry.block.CyanDyeComposterBlock.RAW_COPPER, hasRawCopper);

                            level.setBlock(pos, composterState, 3);
                            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.SAND_PLACE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                        }
                        event.setCanceled(true);
                        event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                        return;
                    }
                }
            }
        }

        if (state.getBlock() instanceof net.minecraft.world.level.block.AbstractCauldronBlock) {
            net.minecraft.world.item.ItemStack held = event.getItemStack();
            if (!held.isEmpty()) {
                net.yigitguven.chymistry.recipe.CauldronRecipeInput input = new net.yigitguven.chymistry.recipe.CauldronRecipeInput(held, state);
                if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    if (serverLevel.getServer().getRecipeManager().getRecipeFor(net.yigitguven.chymistry.recipe.ModRecipes.CAULDRON_TYPE.get(), input, serverLevel).isPresent()) {
                        net.minecraft.world.item.ItemStack dropped = held.split(1);
                        net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
                                level, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, dropped
                        );
                        itemEntity.setDeltaMovement(0, 0, 0);
                        level.addFreshEntity(itemEntity);
                        level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ITEM_PICKUP, net.minecraft.sounds.SoundSource.PLAYERS, 0.4F, 1.0F);
                        event.setCanceled(true);
                        event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                        return;
                    }
                } else {
                    event.setCanceled(true);
                    event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                    return;
                }
            }
        }

        if (event.getItemStack().is(net.minecraft.world.item.Items.GLASS_BOTTLE) || event.getItemStack().is(net.yigitguven.chymistry.item.ModItems.TINTED_GLASS_BOTTLE.get()) || event.getItemStack().is(net.yigitguven.chymistry.item.ModItems.REINFORCED_GLASS_BOTTLE.get())) {
            net.minecraft.world.phys.BlockHitResult hitResult = event.getHitVec();
            net.minecraft.world.item.context.UseOnContext useOnContext = new net.minecraft.world.item.context.UseOnContext(event.getEntity(), event.getHand(), hitResult);
            net.minecraft.world.item.context.BlockPlaceContext placeContext = new net.minecraft.world.item.context.BlockPlaceContext(useOnContext);
            if (placeContext.canPlace()) {
                net.minecraft.world.level.block.state.BlockState placementState = null;
                if (event.getItemStack().is(net.minecraft.world.item.Items.GLASS_BOTTLE)) {
                    placementState = net.yigitguven.chymistry.block.ModBlocks.PLACED_BOTTLE.get().getStateForPlacement(placeContext);
                } else if (event.getItemStack().is(net.yigitguven.chymistry.item.ModItems.TINTED_GLASS_BOTTLE.get())) {
                    placementState = net.yigitguven.chymistry.block.ModBlocks.PLACED_TINTED_BOTTLE.get().getStateForPlacement(placeContext);
                } else if (event.getItemStack().is(net.yigitguven.chymistry.item.ModItems.REINFORCED_GLASS_BOTTLE.get())) {
                    placementState = net.yigitguven.chymistry.block.ModBlocks.PLACED_REINFORCED_BOTTLE.get().getStateForPlacement(placeContext);
                }

                if (placementState != null && placementState.canSurvive(level, placeContext.getClickedPos())) {
                    if (!level.isClientSide()) {
                        level.setBlock(placeContext.getClickedPos(), placementState, 11);
                        level.playSound(null, placeContext.getClickedPos(), net.minecraft.sounds.SoundEvents.GLASS_PLACE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!event.getEntity().isCreative()) {
                            event.getItemStack().shrink(1);
                        }
                    }
                    event.setCanceled(true);
                    event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(net.neoforged.neoforge.event.tick.EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof net.minecraft.world.entity.item.ItemEntity itemEntity) || itemEntity.level().isClientSide()) {
            return;
        }
        if (itemEntity.isRemoved() || itemEntity.getItem().isEmpty()) {
            return;
        }

        net.minecraft.world.level.Level level = itemEntity.level();
        net.minecraft.core.BlockPos pos = itemEntity.blockPosition();
        net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof net.minecraft.world.level.block.AbstractCauldronBlock)) {
            net.minecraft.core.BlockPos below = pos.below();
            net.minecraft.world.level.block.state.BlockState belowState = level.getBlockState(below);
            if (belowState.getBlock() instanceof net.minecraft.world.level.block.AbstractCauldronBlock && itemEntity.getY() - below.getY() <= 1.25) {
                pos = below;
                state = belowState;
            } else {
                if (itemEntity.getPersistentData().contains("chymistry_cauldron_ticks")) {
                    itemEntity.getPersistentData().remove("chymistry_cauldron_ticks");
                }
                return;
            }
        }

        double relX = itemEntity.getX() - pos.getX();
        double relZ = itemEntity.getZ() - pos.getZ();
        if (relX < 0.05 || relX > 0.95 || relZ < 0.05 || relZ > 0.95) {
            if (itemEntity.getPersistentData().contains("chymistry_cauldron_ticks")) {
                itemEntity.getPersistentData().remove("chymistry_cauldron_ticks");
            }
            return;
        }

        net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) level;
        net.yigitguven.chymistry.recipe.CauldronRecipeInput input = new net.yigitguven.chymistry.recipe.CauldronRecipeInput(itemEntity.getItem(), state);
        var optionalRecipe = serverLevel.getServer().getRecipeManager().getRecipeFor(net.yigitguven.chymistry.recipe.ModRecipes.CAULDRON_TYPE.get(), input, serverLevel);
        if (optionalRecipe.isEmpty()) {
            if (itemEntity.getPersistentData().contains("chymistry_cauldron_ticks")) {
                itemEntity.getPersistentData().remove("chymistry_cauldron_ticks");
            }
            return;
        }

        net.yigitguven.chymistry.recipe.CauldronRecipe recipe = optionalRecipe.get().value();
        int ticks = itemEntity.getPersistentData().getInt("chymistry_cauldron_ticks").orElse(0) + 1;

        if (ticks % 10 == 0) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.BUBBLE, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 4, 0.15, 0.1, 0.15, 0.02);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 2, 0.15, 0.1, 0.15, 0.02);
        }

        if (ticks >= recipe.processingTime()) {
            itemEntity.getPersistentData().remove("chymistry_cauldron_ticks");
            net.minecraft.world.item.ItemStack resultStack = recipe.assemble(input);

            itemEntity.getItem().shrink(1);
            if (itemEntity.getItem().isEmpty()) {
                itemEntity.discard();
            }

            net.minecraft.world.entity.item.ItemEntity resultEntity = new net.minecraft.world.entity.item.ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, resultStack);
            resultEntity.setDeltaMovement(0, 0.1, 0);
            level.addFreshEntity(resultEntity);

            if (recipe.levelCost() > 0 && state.hasProperty(net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL)) {
                int currentLevel = state.getValue(net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL);
                int newLevel = currentLevel - recipe.levelCost();
                if (newLevel <= 0) {
                    level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.CAULDRON.defaultBlockState());
                } else {
                    level.setBlockAndUpdate(pos, state.setValue(net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL, newLevel));
                }
            }

            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BREWING_STAND_BREW, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 20, 0.2, 0.2, 0.2, 0.1);
        } else {
            itemEntity.getPersistentData().putInt("chymistry_cauldron_ticks", ticks);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        net.minecraft.world.entity.player.Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        net.minecraft.world.item.ItemStack mainHand = player.getMainHandItem();
        net.minecraft.world.item.ItemStack offHand = player.getOffhandItem();

        // 1. Cannot be transferred to offhand
        if (offHand.is(net.yigitguven.chymistry.util.ModTags.Items.TWO_HANDED)) {
            net.minecraft.world.item.ItemStack offHandCopy = offHand.copy();
            offHand.shrink(offHand.getCount());
            if (!player.getInventory().add(offHandCopy)) {
                player.drop(offHandCopy, false);
            }
        }

        // 2. Cannot hold in main hand if offhand is not empty. Unequip offhand instead of main hand.
        offHand = player.getOffhandItem();
        if (mainHand.is(net.yigitguven.chymistry.util.ModTags.Items.TWO_HANDED) && !offHand.isEmpty()) {
            net.minecraft.world.item.ItemStack offHandCopy = offHand.copy();
            offHand.shrink(offHand.getCount());
            if (!player.getInventory().add(offHandCopy)) {
                player.drop(offHandCopy, false);
            }
        }

        // 3. Tick Iron Tongs
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            net.minecraft.world.item.ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(net.yigitguven.chymistry.item.ModItems.IRON_TONGS.get())) {
                net.yigitguven.chymistry.item.IronTongsItem.tickCustom(stack, player.level(), player);
            }
        }

        // 4. Mobile Match Lighting & Ticking
        boolean hasLitMatch = false;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            net.minecraft.world.item.ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof net.yigitguven.chymistry.item.MatchItem matchItem && net.yigitguven.chymistry.item.MatchItem.isLit(stack)) {
                hasLitMatch = true;
                matchItem.tickCustom(stack, player.level(), player);
            }
        }

        net.minecraft.world.level.Level level = player.level();
        java.util.UUID playerId = player.getUUID();
        net.minecraft.core.BlockPos currentLightPos = PLAYER_LIGHTS.get(playerId);

        if (hasLitMatch && player.isAlive()) {
            net.minecraft.core.BlockPos headPos = player.blockPosition().above();
            net.minecraft.core.BlockPos feetPos = player.blockPosition();
            net.minecraft.core.BlockPos targetPos = null;

            if (level.getBlockState(headPos).isAir() || level.getBlockState(headPos).is(net.minecraft.world.level.block.Blocks.LIGHT)) {
                targetPos = headPos;
            } else if (level.getBlockState(feetPos).isAir() || level.getBlockState(feetPos).is(net.minecraft.world.level.block.Blocks.LIGHT)) {
                targetPos = feetPos;
            } else if (level.getBlockState(headPos).is(net.minecraft.world.level.block.Blocks.WATER)) {
                targetPos = headPos;
            } else if (level.getBlockState(feetPos).is(net.minecraft.world.level.block.Blocks.WATER)) {
                targetPos = feetPos;
            }

            if (targetPos != null) {
                if (currentLightPos != null && !currentLightPos.equals(targetPos)) {
                    removeLight(level, currentLightPos);
                }

                net.minecraft.world.level.block.state.BlockState targetState = level.getBlockState(targetPos);
                if (targetState.isAir()) {
                    level.setBlockAndUpdate(targetPos, net.minecraft.world.level.block.Blocks.LIGHT.defaultBlockState()
                            .setValue(net.minecraft.world.level.block.LightBlock.LEVEL, 15));
                    PLAYER_LIGHTS.put(playerId, targetPos);
                } else if (targetState.is(net.minecraft.world.level.block.Blocks.WATER)) {
                    level.setBlockAndUpdate(targetPos, net.minecraft.world.level.block.Blocks.LIGHT.defaultBlockState()
                            .setValue(net.minecraft.world.level.block.LightBlock.LEVEL, 15)
                            .setValue(net.minecraft.world.level.block.LightBlock.WATERLOGGED, true));
                    PLAYER_LIGHTS.put(playerId, targetPos);
                } else if (targetState.is(net.minecraft.world.level.block.Blocks.LIGHT)) {
                    PLAYER_LIGHTS.put(playerId, targetPos);
                }
            } else if (currentLightPos != null) {
                removeLight(level, currentLightPos);
                PLAYER_LIGHTS.remove(playerId);
            }
        } else if (currentLightPos != null) {
            removeLight(level, currentLightPos);
            PLAYER_LIGHTS.remove(playerId);
        }
    }

    private static final java.util.Map<java.util.UUID, net.minecraft.core.BlockPos> PLAYER_LIGHTS = new java.util.concurrent.ConcurrentHashMap<>();

    private static void removeLight(net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos) {
        net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
        if (state.is(net.minecraft.world.level.block.Blocks.LIGHT)) {
            if (state.hasProperty(net.minecraft.world.level.block.LightBlock.WATERLOGGED) && state.getValue(net.minecraft.world.level.block.LightBlock.WATERLOGGED)) {
                level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.WATER.defaultBlockState());
            } else {
                level.removeBlock(pos, false);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        net.minecraft.world.entity.player.Player player = event.getEntity();
        net.minecraft.core.BlockPos pos = PLAYER_LIGHTS.remove(player.getUUID());
        if (pos != null) {
            removeLight(player.level(), pos);
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof net.minecraft.world.entity.player.Player player) {
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                if (net.yigitguven.chymistry.wood.TreatedWoodHelper.isTreated(main) || net.yigitguven.chymistry.wood.TreatedWoodHelper.isTreated(off)) {
                    net.yigitguven.chymistry.wood.TreatedBlockData.get(serverLevel).add(event.getPos());
                } else {
                    net.yigitguven.chymistry.wood.TreatedBlockData.get(serverLevel).remove(event.getPos());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockDrops(net.neoforged.neoforge.event.level.BlockDropsEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            net.minecraft.core.BlockPos pos = event.getPos();
            if (net.yigitguven.chymistry.wood.TreatedBlockData.isTreated(serverLevel, pos)) {
                net.yigitguven.chymistry.wood.TreatedBlockData.get(serverLevel).remove(pos);
                for (net.minecraft.world.entity.item.ItemEntity drop : event.getDrops()) {
                    ItemStack dropStack = drop.getItem();
                    if (net.yigitguven.chymistry.wood.TreatedWoodHelper.isWoodMaterial(dropStack)) {
                        drop.setItem(net.yigitguven.chymistry.wood.TreatedWoodHelper.makeTreated(dropStack, dropStack.getCount()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCheckSpawn(net.neoforged.neoforge.event.entity.living.MobSpawnEvent.PositionCheck event) {
        if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            java.util.Set<net.minecraft.core.BlockPos> repellents = net.yigitguven.chymistry.block.CrucibleBlockEntity.ACTIVE_REPELLENTS.get(serverLevel.dimension());
            if (repellents != null && !repellents.isEmpty()) {
                double x = event.getX();
                double y = event.getY();
                double z = event.getZ();
                for (net.minecraft.core.BlockPos cruciblePos : repellents) {
                    if (cruciblePos.distToCenterSqr(x, y, z) <= 1024.0) { // 32 blocks radius (32^2 = 1024)
                        event.setResult(net.neoforged.neoforge.event.entity.living.MobSpawnEvent.PositionCheck.Result.FAIL);
                        return;
                    }
                }
            }
        }
    }
}
