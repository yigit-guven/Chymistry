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
                // "completely full but the bone meal is not ready to collect" -> Level 7
                if (composterLevel == 7) {
                    net.minecraft.world.item.ItemStack stack = event.getItemStack();
                    if (stack.is(net.yigitguven.chymistry.item.ModItems.ASH.get())) {
                        if (!level.isClientSide()) {
                            if (!event.getEntity().isCreative()) {
                                stack.shrink(1);
                            }
                            level.setBlock(pos, net.yigitguven.chymistry.block.ModBlocks.NITER_SOIL_COMPOSTER.get().defaultBlockState(), 3);
                            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.SAND_PLACE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                        }
                        event.setCanceled(true);
                        event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                        return;
                    }
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
}
