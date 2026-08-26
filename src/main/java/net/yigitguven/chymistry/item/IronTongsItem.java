package net.yigitguven.chymistry.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.yigitguven.chymistry.block.CrucibleBlock;

import java.util.Optional;

public class IronTongsItem extends Item {
    public IronTongsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (tag.contains("CrucibleType")) {
            // Place crucible
            BlockPos placePos = pos.relative(context.getClickedFace());
            if (!level.getBlockState(placePos).canBeReplaced()) {
                return InteractionResult.PASS;
            }

            Identifier blockId = Identifier.parse(tag.getString("CrucibleType").orElse(""));
            Block block = BuiltInRegistries.BLOCK.get(blockId).get().value();
            if (block instanceof CrucibleBlock crucibleBlock) {
                BlockState stateToPlace = block.defaultBlockState();
                // Match waterlogging if applicable
                net.minecraft.world.level.material.FluidState fluidstate = level.getFluidState(placePos);
                stateToPlace = stateToPlace.setValue(CrucibleBlock.WATERLOGGED, fluidstate.getType() == net.minecraft.world.level.material.Fluids.WATER);

                if (level.setBlock(placePos, stateToPlace, 3)) {
                    BlockEntity be = level.getBlockEntity(placePos);
                    if (be instanceof net.yigitguven.chymistry.block.CrucibleBlockEntity crucibleBE && tag.contains("CrucibleHeat")) {
                        crucibleBE.currentHeat = tag.getFloat("CrucibleHeat").orElse(0.0f);
                        crucibleBE.setChanged();
                    }
                    level.playSound(player, placePos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                    tag.remove("CrucibleType");
                    tag.remove("CrucibleHeat");
                    
                    if (tag.isEmpty()) {
                        stack.remove(DataComponents.CUSTOM_DATA);
                    } else {
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    }
                    stack.remove(DataComponents.CUSTOM_MODEL_DATA);

                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            // Pick up crucible
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof CrucibleBlock crucible) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof net.yigitguven.chymistry.block.CrucibleBlockEntity crucibleBE) {
                    tag.putString("CrucibleType", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());
                    tag.putFloat("CrucibleHeat", crucibleBE.currentHeat);

                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    stack.set(DataComponents.CUSTOM_MODEL_DATA, new net.minecraft.world.item.component.CustomModelData(
                            java.util.List.of(), java.util.List.of(), java.util.List.of(BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString()), java.util.List.of()
                    ));

                    level.removeBlock(pos, false);
                    level.playSound(player, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useOn(context);
    }
}
