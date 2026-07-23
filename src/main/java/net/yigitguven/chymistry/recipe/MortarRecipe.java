package net.yigitguven.chymistry.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public record MortarRecipe(Ingredient input, ItemStack output, int presses) implements Recipe<SingleRecipeInput> {

    public static final MapCodec<MortarRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(MortarRecipe::input),
            ItemStack.CODEC.fieldOf("output").forGetter(MortarRecipe::output),
            Codec.INT.fieldOf("presses").forGetter(MortarRecipe::presses)
    ).apply(inst, MortarRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, MortarRecipe::input,
            ItemStack.STREAM_CODEC, MortarRecipe::output,
            ByteBufCodecs.INT, MortarRecipe::presses,
            MortarRecipe::new
    );

    @Override
    public boolean matches(SingleRecipeInput pInput, Level pLevel) {
        return this.input.test(pInput.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput pInput) {
        return this.output.copy();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return ModRecipes.MORTAR_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return ModRecipes.MORTAR_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.input);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }
}
