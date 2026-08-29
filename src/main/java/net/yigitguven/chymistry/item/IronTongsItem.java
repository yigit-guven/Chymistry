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

    private static class ClientTooltipHandler {
        public static boolean hasThermometer() {
            try {
                net.minecraft.client.player.LocalPlayer player = net.minecraft.client.Minecraft.getInstance().player;
                if (player == null) return false;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    if (player.getInventory().getItem(i).is(ModItems.THERMOMETER.get())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                // ignored
            }
            return false;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, net.minecraft.world.item.component.TooltipDisplay pTooltipDisplay, java.util.function.Consumer<net.minecraft.network.chat.Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
        pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.iron_tongs.desc").withStyle(net.minecraft.ChatFormatting.GRAY));

        CustomData customData = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (tag.contains("CrucibleType")) {
            String crucibleType = tag.getString("CrucibleType").orElse("");
            float heat = tag.contains("CrucibleHeat") ? tag.getFloat("CrucibleHeat").orElse(0.0f) : 0.0f;

            String blockKey = crucibleType.replace(":", ".");
            net.minecraft.network.chat.Component crucibleName = net.minecraft.network.chat.Component.translatable("block." + blockKey).withStyle(net.minecraft.ChatFormatting.GOLD);

            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.iron_tongs.carrying", crucibleName).withStyle(net.minecraft.ChatFormatting.GRAY));
            
            try {
                if (ClientTooltipHandler.hasThermometer()) {
                    if (heat >= 0) {
                        pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.iron_tongs.heat", net.minecraft.network.chat.Component.literal(String.format("%.1f", heat)).withStyle(net.minecraft.ChatFormatting.RED)).withStyle(net.minecraft.ChatFormatting.GRAY));
                    } else {
                        pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.iron_tongs.heat", net.minecraft.network.chat.Component.literal(String.format("%.1f", heat)).withStyle(net.minecraft.ChatFormatting.AQUA)).withStyle(net.minecraft.ChatFormatting.GRAY));
                    }
                }
            } catch (Throwable e) {
                // Ignore
            }
        } else {
            pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.iron_tongs.empty").withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
    }



    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        BlockState clickedState = level.getBlockState(pos);
        if (clickedState.getBlock() instanceof CrucibleBlock && !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

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
                        if (tag.contains("CrucibleData")) {
                            BlockEntity loadedBe = BlockEntity.loadStatic(placePos, stateToPlace, tag.getCompound("CrucibleData").orElse(new net.minecraft.nbt.CompoundTag()), level.registryAccess());
                            if (loadedBe instanceof net.yigitguven.chymistry.block.CrucibleBlockEntity loadedCrucible) {
                                crucibleBE.inventory.clearContent();
                                for (int i = 0; i < loadedCrucible.inventory.getContainerSize(); i++) {
                                    crucibleBE.inventory.setItem(i, loadedCrucible.inventory.getItem(i));
                                }
                                crucibleBE.progress = loadedCrucible.progress;
                                crucibleBE.maxProgress = loadedCrucible.maxProgress;
                            }
                        }
                        crucibleBE.currentHeat = tag.getFloat("CrucibleHeat").orElse(0.0f);
                        crucibleBE.setChanged();
                    }
                    level.playSound(player, placePos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                    tag.remove("CrucibleType");
                    tag.remove("CrucibleHeat");
                    tag.remove("CrucibleData");
                    
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
                    tag.put("CrucibleData", crucibleBE.saveCustomOnly(level.registryAccess()));

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

    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        if (!(entity instanceof Player player)) return;


        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (tag.contains("CrucibleType") && tag.contains("CrucibleHeat")) {
            CompoundTag crucibleData = tag.getCompound("CrucibleData").orElse(new CompoundTag());
            Identifier blockId = Identifier.parse(tag.getString("CrucibleType").orElse(""));
            Block block = BuiltInRegistries.BLOCK.get(blockId).get().value();
            BlockState dummyState = block.defaultBlockState();
            
            // To ensure CrucibleBlockEntity.tick checks the block AT targetPos for heat,
            // we pass targetPos.above() as the block entity's position, because tick() checks pos.below()
            BlockPos legPos = player.blockPosition();
            BlockPos targetPos = legPos.relative(player.getDirection());
            BlockPos dummyPos = targetPos.above();

            BlockEntity dummyBe = BlockEntity.loadStatic(dummyPos, dummyState, crucibleData, level.registryAccess());
            net.yigitguven.chymistry.block.CrucibleBlockEntity dummyCrucible;
            if (dummyBe instanceof net.yigitguven.chymistry.block.CrucibleBlockEntity) {
                dummyCrucible = (net.yigitguven.chymistry.block.CrucibleBlockEntity) dummyBe;
            } else {
                dummyCrucible = new net.yigitguven.chymistry.block.CrucibleBlockEntity(dummyPos, dummyState);
            }
            dummyCrucible.currentHeat = tag.getFloat("CrucibleHeat").orElse(0.0f);

            float oldHeat = dummyCrucible.currentHeat;

            // Handle waterlogged state overriding if player is in water
            if (player.isInWater()) {
                dummyState = dummyState.setValue(CrucibleBlock.WATERLOGGED, true);
            }

            net.yigitguven.chymistry.block.CrucibleBlockEntity.tick(level, dummyPos, dummyState, dummyCrucible);

            CompoundTag newData = dummyCrucible.saveCustomOnly(level.registryAccess());
            if (oldHeat != dummyCrucible.currentHeat || !newData.equals(crucibleData)) {
                tag.putFloat("CrucibleHeat", dummyCrucible.currentHeat);
                tag.put("CrucibleData", newData);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
        }
    }
}
