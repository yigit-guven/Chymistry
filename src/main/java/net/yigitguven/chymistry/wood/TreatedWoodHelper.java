package net.yigitguven.chymistry.wood;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.SoundType;

public class TreatedWoodHelper {
    public static final String TREATED_TAG = "ChymistryTreated";

    public static boolean isTreated(ItemStack stack) {
        if (stack.isEmpty()) return false;
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBoolean(TREATED_TAG).orElse(false);
    }

    public static ItemStack makeTreated(ItemStack stack, int count) {
        ItemStack result = stack.copyWithCount(count);
        result.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

        Component originalName = stack.getHoverName();
        Component treatedName = Component.translatable("text.chymistry.treated_prefix", originalName)
                .withStyle(style -> style.withItalic(false));
        result.set(DataComponents.CUSTOM_NAME, treatedName);

        CustomData data = result.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = data.copyTag();
        tag.putBoolean(TREATED_TAG, true);
        result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        return result;
    }

    public static boolean isWoodMaterial(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ItemTags.PLANKS) ||
            stack.is(ItemTags.WOODEN_STAIRS) ||
            stack.is(ItemTags.WOODEN_SLABS) ||
            stack.is(ItemTags.WOODEN_FENCES) ||
            stack.is(ItemTags.WOODEN_DOORS) ||
            stack.is(ItemTags.WOODEN_TRAPDOORS) ||
            stack.is(ItemTags.WOODEN_PRESSURE_PLATES) ||
            stack.is(ItemTags.WOODEN_BUTTONS)) {
            return true;
        }
        if (stack.getItem() instanceof BlockItem bi) {
            net.minecraft.world.level.block.Block block = bi.getBlock();
            if (block.defaultBlockState().getSoundType() == SoundType.WOOD) {
                return stack.is(ItemTags.FENCE_GATES) ||
                       block instanceof net.minecraft.world.level.block.StairBlock ||
                       block instanceof net.minecraft.world.level.block.SlabBlock ||
                       block instanceof net.minecraft.world.level.block.FenceBlock ||
                       block instanceof net.minecraft.world.level.block.FenceGateBlock ||
                       block instanceof net.minecraft.world.level.block.DoorBlock ||
                       block instanceof net.minecraft.world.level.block.TrapDoorBlock ||
                       block instanceof net.minecraft.world.level.block.ButtonBlock ||
                       block instanceof net.minecraft.world.level.block.PressurePlateBlock;
            }
        }
        return false;
    }
}
