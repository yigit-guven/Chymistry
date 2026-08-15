package net.yigitguven.chymistry.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public record MortarRecipe(List<SizedIngredient> inputs, ClickType clickType, ItemStackTemplate output, int presses) implements Recipe<MortarRecipeInput> {

    public static final MapCodec<MortarRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SizedIngredient.CODEC.listOf().fieldOf("inputs").forGetter(MortarRecipe::inputs),
            ClickType.CODEC.fieldOf("clickType").forGetter(MortarRecipe::clickType),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(MortarRecipe::output),
            Codec.INT.fieldOf("presses").forGetter(MortarRecipe::presses)
    ).apply(inst, MortarRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), MortarRecipe::inputs,
            ClickType.STREAM_CODEC.cast(), MortarRecipe::clickType,
            ItemStackTemplate.STREAM_CODEC, MortarRecipe::output,
            ByteBufCodecs.INT, MortarRecipe::presses,
            MortarRecipe::new
    );

    @Override
    public boolean matches(MortarRecipeInput pInput, Level pLevel) {
        if (this.inputs.size() != pInput.size()) return false;
        for (int i = 0; i < this.inputs.size(); i++) {
            if (!this.inputs.get(i).test(pInput.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(MortarRecipeInput pInput) {
        return this.output.create();
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
    public RecipeSerializer<? extends Recipe<MortarRecipeInput>> getSerializer() {
        return ModRecipes.MORTAR_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<MortarRecipeInput>> getType() {
        return ModRecipes.MORTAR_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        // Just extract ingredients from our sized ingredients
        return PlacementInfo.create(this.inputs.stream().map(SizedIngredient::ingredient).toList());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }
}
