package net.yigitguven.chymistry.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Chymistry.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Chymistry.MODID);

    public static final Supplier<RecipeSerializer<MortarRecipe>> MORTAR_SERIALIZER =
            SERIALIZERS.register("mortar", () -> new RecipeSerializer<>(MortarRecipe.CODEC, MortarRecipe.STREAM_CODEC));

    public static final Supplier<RecipeType<MortarRecipe>> MORTAR_TYPE =
            TYPES.register("mortar", () -> new RecipeType<MortarRecipe>() {
                @Override
                public String toString() {
                    return "mortar";
                }
            });
}
