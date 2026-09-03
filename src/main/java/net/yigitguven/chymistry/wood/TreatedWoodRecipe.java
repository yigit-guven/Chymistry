package net.yigitguven.chymistry.wood;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.yigitguven.chymistry.item.ModItems;
import net.yigitguven.chymistry.recipe.ModRecipes;

public class TreatedWoodRecipe extends CustomRecipe {
    public static final MapCodec<TreatedWoodRecipe> CODEC = MapCodec.unit(TreatedWoodRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, TreatedWoodRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {},
            buf -> new TreatedWoodRecipe()
    );

    public TreatedWoodRecipe() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TreatedWoodRecipe;
    }

    @Override
    public int hashCode() {
        return TreatedWoodRecipe.class.hashCode();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 3 || input.height() != 3 || input.size() != 9) {
            return false;
        }

        // Center slot must be Creosote Oil
        ItemStack center = input.getItem(4);
        if (!center.is(ModItems.CREOSOTE_OIL.get())) {
            return false;
        }

        ItemStack woodTemplate = ItemStack.EMPTY;
        for (int i = 0; i < 9; i++) {
            if (i == 4) continue;
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) return false;
            if (!TreatedWoodHelper.isWoodMaterial(stack)) return false;
            if (TreatedWoodHelper.isTreated(stack)) return false;

            if (woodTemplate.isEmpty()) {
                woodTemplate = stack;
            } else if (!ItemStack.isSameItem(woodTemplate, stack)) {
                return false;
            }
        }
        return !woodTemplate.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack woodTemplate = input.getItem(0);
        return TreatedWoodHelper.makeTreated(woodTemplate, 8);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ModItems.CREOSOTE_OIL.get())) {
                remaining.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.TREATED_WOOD_SERIALIZER.get();
    }
}
