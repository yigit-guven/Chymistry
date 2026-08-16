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
}
