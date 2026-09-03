package net.yigitguven.chymistry.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.yigitguven.chymistry.block.ModBlocks;

import java.util.List;
import java.util.Optional;

public record AlembicRecipe(List<SizedIngredient> inputs, Optional<SizedIngredient> bottle, Optional<ItemStackTemplate> output, Optional<ItemStackTemplate> secondaryOutput, int processingTime) implements Recipe<AlembicRecipeInput> {

    public static final MapCodec<AlembicRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SizedIngredient.CODEC.listOf().fieldOf("inputs").forGetter(AlembicRecipe::inputs),
            SizedIngredient.CODEC.optionalFieldOf("bottle").forGetter(AlembicRecipe::bottle),
            ItemStackTemplate.CODEC.optionalFieldOf("output").forGetter(AlembicRecipe::output),
            ItemStackTemplate.CODEC.optionalFieldOf("secondaryOutput").forGetter(AlembicRecipe::secondaryOutput),
            Codec.INT.fieldOf("processingTime").forGetter(AlembicRecipe::processingTime)
    ).apply(inst, AlembicRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlembicRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), AlembicRecipe::inputs,
            ByteBufCodecs.optional(SizedIngredient.STREAM_CODEC), AlembicRecipe::bottle,
            ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC), AlembicRecipe::output,
            ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC), AlembicRecipe::secondaryOutput,
            ByteBufCodecs.INT, AlembicRecipe::processingTime,
            AlembicRecipe::new
    );

    @Override
    public boolean matches(AlembicRecipeInput pInput, Level pLevel) {
        java.util.List<ItemStack> inputItems = new java.util.ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ItemStack stack = pInput.getItem(i);
            if (!stack.isEmpty()) {
                inputItems.add(stack.copy());
            }
        }

        if (this.inputs.size() != inputItems.size()) {
            return false;
        }

        for (SizedIngredient ingredient : this.inputs) {
            boolean found = false;
            for (int i = 0; i < inputItems.size(); i++) {
                ItemStack stack = inputItems.get(i);
                if (ingredient.ingredient().test(stack) && stack.getCount() >= ingredient.count()) {
                    inputItems.remove(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(AlembicRecipeInput pInput) {
        return this.output.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);
    }

    @Override
    public RecipeSerializer<? extends Recipe<AlembicRecipeInput>> getSerializer() {
        return ModRecipes.ALEMBIC_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlembicRecipeInput>> getType() {
        return ModRecipes.ALEMBIC_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.inputs.stream().map(SizedIngredient::ingredient).toList());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }
}
