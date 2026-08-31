package net.yigitguven.chymistry.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import java.util.List;

public record AlembicRecipeInput(List<ItemStack> inputs, ItemStack fuelSlot, ItemStack outputSlot) implements RecipeInput {
    @Override
    public ItemStack getItem(int pIndex) {
        if (pIndex < 4) {
            return inputs.size() > pIndex ? inputs.get(pIndex) : ItemStack.EMPTY;
        } else if (pIndex == 4) {
            return fuelSlot;
        } else if (pIndex == 5) {
            return outputSlot;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 6;
    }
}
