package net.yigitguven.chymistry.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

import java.util.List;

public record CauldronRecipe(
        Ingredient ingredient,
        Block cauldronBlock,
        int minLevel,
        int levelCost,
        int processingTime,
        ItemStackTemplate output
) implements Recipe<CauldronRecipeInput> {

    private static final Codec<Ingredient> LEGACY_ITEM_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(x -> null)
    ).apply(inst, Ingredient::of));

    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.either(Ingredient.CODEC, LEGACY_ITEM_CODEC)
            .xmap(either -> either.map(a -> a, b -> b), com.mojang.datafixers.util.Either::left);

    public static final MapCodec<CauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            INGREDIENT_CODEC.fieldOf("input").forGetter(CauldronRecipe::ingredient),
            BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("cauldron", Blocks.WATER_CAULDRON).forGetter(CauldronRecipe::cauldronBlock),
            Codec.INT.optionalFieldOf("min_level", 1).forGetter(CauldronRecipe::minLevel),
            Codec.INT.optionalFieldOf("level_cost", 1).forGetter(CauldronRecipe::levelCost),
            Codec.INT.optionalFieldOf("processing_time", 200).forGetter(CauldronRecipe::processingTime),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(CauldronRecipe::output)
    ).apply(inst, CauldronRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, CauldronRecipe::ingredient,
            ByteBufCodecs.registry(Registries.BLOCK), CauldronRecipe::cauldronBlock,
            ByteBufCodecs.INT, CauldronRecipe::minLevel,
            ByteBufCodecs.INT, CauldronRecipe::levelCost,
            ByteBufCodecs.INT, CauldronRecipe::processingTime,
            ItemStackTemplate.STREAM_CODEC, CauldronRecipe::output,
            CauldronRecipe::new
    );

    @Override
    public boolean matches(CauldronRecipeInput pInput, Level pLevel) {
        if (!this.ingredient.test(pInput.item())) {
            return false;
        }
        if (!pInput.state().is(this.cauldronBlock)) {
            return false;
        }
        if (pInput.state().hasProperty(LayeredCauldronBlock.LEVEL)) {
            return pInput.state().getValue(LayeredCauldronBlock.LEVEL) >= this.minLevel;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CauldronRecipeInput pInput) {
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
    public RecipeSerializer<? extends Recipe<CauldronRecipeInput>> getSerializer() {
        return ModRecipes.CAULDRON_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<CauldronRecipeInput>> getType() {
        return ModRecipes.CAULDRON_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(List.of(this.ingredient));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }
}
