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

import java.util.List;
import java.util.Optional;

public record CrucibleRecipe(List<SizedIngredient> inputs, Optional<SizedIngredient> container, ItemStackTemplate output, int processingTime, int minHeat, int maxHeat, float heatCost) implements Recipe<CrucibleRecipeInput> {

    public static final MapCodec<CrucibleRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SizedIngredient.CODEC.listOf().fieldOf("inputs").forGetter(CrucibleRecipe::inputs),
            SizedIngredient.CODEC.optionalFieldOf("container").forGetter(CrucibleRecipe::container),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(CrucibleRecipe::output),
            Codec.INT.fieldOf("processingTime").forGetter(CrucibleRecipe::processingTime),
            Codec.INT.fieldOf("minHeat").forGetter(CrucibleRecipe::minHeat),
            Codec.INT.fieldOf("maxHeat").forGetter(CrucibleRecipe::maxHeat),
            Codec.FLOAT.fieldOf("heatCost").forGetter(CrucibleRecipe::heatCost)
    ).apply(inst, CrucibleRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CrucibleRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), CrucibleRecipe::inputs,
            ByteBufCodecs.optional(SizedIngredient.STREAM_CODEC), CrucibleRecipe::container,
            ItemStackTemplate.STREAM_CODEC, CrucibleRecipe::output,
            ByteBufCodecs.INT, CrucibleRecipe::processingTime,
            ByteBufCodecs.INT, CrucibleRecipe::minHeat,
            ByteBufCodecs.INT, CrucibleRecipe::maxHeat,
            ByteBufCodecs.FLOAT, CrucibleRecipe::heatCost,
            CrucibleRecipe::new
    );

    @Override
    public boolean matches(CrucibleRecipeInput pInput, Level pLevel) {
        // pInput indices 0-3 are main materials, index 4 is container.
        ItemStack containerStack = pInput.getItem(4);
        if (this.container.isPresent()) {
            if (!this.container.get().test(containerStack)) {
                return false;
            }
        } else {
            if (!containerStack.isEmpty()) {
                return false;
            }
        }

        ItemStack[] available = new ItemStack[4];
        boolean[] hasItem = new boolean[4];
        for (int i = 0; i < 4; i++) {
            ItemStack stack = pInput.getItem(i);
            if (!stack.isEmpty()) {
                available[i] = stack.copy();
                hasItem[i] = true;
            } else {
                available[i] = ItemStack.EMPTY;
            }
        }

        boolean[] slotContributed = new boolean[4];
        for (SizedIngredient ingredient : this.inputs) {
            int needed = ingredient.count();
            for (int i = 0; i < 4; i++) {
                if (!available[i].isEmpty() && ingredient.ingredient().test(available[i])) {
                    int take = Math.min(needed, available[i].getCount());
                    available[i].shrink(take);
                    needed -= take;
                    slotContributed[i] = true;
                    if (needed <= 0) {
                        break;
                    }
                }
            }
            if (needed > 0) {
                return false;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (hasItem[i] && !slotContributed[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CrucibleRecipeInput pInput) {
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
    public RecipeSerializer<? extends Recipe<CrucibleRecipeInput>> getSerializer() {
        return ModRecipes.CRUCIBLE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<CrucibleRecipeInput>> getType() {
        return ModRecipes.CRUCIBLE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.inputs.stream().map(SizedIngredient::ingredient).toList());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }
}
