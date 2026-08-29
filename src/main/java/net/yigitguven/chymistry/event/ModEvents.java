package net.yigitguven.chymistry.event;

import net.minecraft.world.effect.MobEffects;
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
                    }
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
}
