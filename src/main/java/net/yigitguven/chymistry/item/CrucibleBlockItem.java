package net.yigitguven.chymistry.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.yigitguven.chymistry.block.CrucibleBlock;

import java.util.function.Consumer;

public class CrucibleBlockItem extends BlockItem {
    public CrucibleBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, net.minecraft.world.item.component.TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (this.getBlock() instanceof CrucibleBlock crucibleBlock) {
            pTooltipComponents.accept(
                Component.translatable("tooltip.chymistry.crucible.heat",
                    Component.literal(String.valueOf(crucibleBlock.getMinHeat())).withStyle(net.minecraft.ChatFormatting.AQUA),
                    Component.literal(String.valueOf(crucibleBlock.getMaxHeat())).withStyle(net.minecraft.ChatFormatting.RED)
                ).withStyle(net.minecraft.ChatFormatting.GRAY)
            );
        }
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
    }
}
