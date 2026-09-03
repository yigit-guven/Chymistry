package net.yigitguven.chymistry.jei;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public record ProductionComposterJeiRecipe(List<ItemStack> inputs, ItemStack output, String tooltipKey) {
    public ProductionComposterJeiRecipe(ItemStack input, ItemStack output) {
        this(List.of(input), output, "jei.chymistry.production_composting.tooltip");
    }
}
