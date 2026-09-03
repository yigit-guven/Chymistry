package net.yigitguven.chymistry.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.yigitguven.chymistry.recipe.BurningRecipe;
import net.yigitguven.chymistry.recipe.ModRecipes;
import net.yigitguven.chymistry.config.ChymistryServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public class FireBlockMixin {

    @Inject(
            method = "checkBurnOut(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/util/RandomSource;ILnet/minecraft/core/Direction;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chymistry$preventTreatedBurnOut(Level level, BlockPos pos, int chance, RandomSource random, int age, Direction face, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel && net.yigitguven.chymistry.wood.TreatedBlockData.isTreated(serverLevel, pos)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "canCatchFire(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chymistry$preventTreatedCanCatchFire(net.minecraft.world.level.BlockGetter level, BlockPos pos, Direction direction, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof ServerLevel serverLevel && net.yigitguven.chymistry.wood.TreatedBlockData.isTreated(serverLevel, pos)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "getIgniteOdds(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chymistry$preventTreatedIgniteOdds(net.minecraft.world.level.LevelReader level, BlockPos pos, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Integer> cir) {
        if (level instanceof ServerLevel serverLevel && net.yigitguven.chymistry.wood.TreatedBlockData.isTreated(serverLevel, pos)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(
            method = "checkBurnOut(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/util/RandomSource;ILnet/minecraft/core/Direction;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
            )
    )
    private void chymistry$onRemoveBlock(Level level, BlockPos pos, int chance, RandomSource random, int age, Direction face, CallbackInfo ci) {
        chymistry$handleBurnOut(level, pos, random);
    }

    @Inject(
            method = "checkBurnOut(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/util/RandomSource;ILnet/minecraft/core/Direction;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            )
    )
    private void chymistry$onSetBlock(Level level, BlockPos pos, int chance, RandomSource random, int age, Direction face, CallbackInfo ci) {
        chymistry$handleBurnOut(level, pos, random);
    }

    private void chymistry$handleBurnOut(Level level, BlockPos pos, RandomSource random) {
        if (level instanceof ServerLevel serverLevel) {
            // Only process if a player is within 64 blocks, if enabled in config
            if (ChymistryServerConfig.REQUIRE_PLAYER_FOR_ASH.get() && !serverLevel.hasNearbyAlivePlayer(pos.getX(), pos.getY(), pos.getZ(), 64.0D)) {
                return;
            }

            BlockState state = level.getBlockState(pos);
            ItemStack stack = new ItemStack(state.getBlock());

            if (!stack.isEmpty()) {
                SingleRecipeInput input = new SingleRecipeInput(stack);

                serverLevel.getServer().getRecipeManager().getRecipeFor(ModRecipes.BURNING_TYPE.get(), input, serverLevel).ifPresent(recipeHolder -> {
                    BurningRecipe recipe = recipeHolder.value();
                    if (random.nextInt(100) < recipe.chance()) {
                        ItemStack result = recipe.assemble(input);
                        if (!result.isEmpty()) {
                            ItemEntity itemEntity = new ItemEntity(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, result);
                            itemEntity.setDeltaMovement(0, 0.2, 0);
                            itemEntity.setInvulnerable(true);
                            // Configurable despawn time
                            itemEntity.lifespan = ChymistryServerConfig.ASH_DESPAWN_TICKS.get();
                            serverLevel.addFreshEntity(itemEntity);
                        }
                    }
                });
            }
        }
    }
}
