package net.yigitguven.chymistry.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.yigitguven.chymistry.item.ModItems;
import net.yigitguven.chymistry.util.SoapCleaningHelper;

public class SoapBleachingRecipe extends CustomRecipe {
    public static final SoapBleachingRecipe INSTANCE = new SoapBleachingRecipe();
    public static final MapCodec<SoapBleachingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SoapBleachingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<SoapBleachingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 2) {
            return false;
        }

        boolean hasSoap = false;
        boolean hasDyedItem = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(ModItems.SOAP.get())) {
                    if (hasSoap) return false;
                    hasSoap = true;
                } else if (SoapCleaningHelper.canCleanItem(stack)) {
                    if (hasDyedItem) return false;
                    hasDyedItem = true;
                } else {
                    return false;
                }
            }
        }

        return hasSoap && hasDyedItem;
    }

    public ItemStack assemble(CraftingInput input) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && !stack.is(ModItems.SOAP.get()) && SoapCleaningHelper.canCleanItem(stack)) {
                return SoapCleaningHelper.cleanItem(stack);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && stack.is(ModItems.SOAP.get())) {
                ItemStack remainingSoap = stack.copy();
                remainingSoap.setDamageValue(remainingSoap.getDamageValue() + 1);
                if (remainingSoap.getDamageValue() < remainingSoap.getMaxDamage()) {
                    result.set(i, remainingSoap);
                }
            }
        }

        return result;
    }

    @Override
    public RecipeSerializer<SoapBleachingRecipe> getSerializer() {
        return ModRecipes.SOAP_BLEACHING_SERIALIZER.get();
    }
}
