package net.yigitguven.chymistry.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record MortarRecipeInput(ItemStack input1, ItemStack input2, ItemStack input3) implements RecipeInput {
    @Override
    public ItemStack getItem(int pIndex) {
        return switch (pIndex) {
            case 0 -> input1;
            case 1 -> input2;
            case 2 -> input3;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 3;
    }
}
