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

    public static final Supplier<RecipeSerializer<BurningRecipe>> BURNING_SERIALIZER =
            SERIALIZERS.register("burning", () -> new RecipeSerializer<>(BurningRecipe.CODEC, BurningRecipe.STREAM_CODEC));

    public static final Supplier<RecipeType<BurningRecipe>> BURNING_TYPE =
            TYPES.register("burning", () -> new RecipeType<BurningRecipe>() {
                @Override
                public String toString() {
                    return "burning";
                }
            });

    public static final Supplier<RecipeSerializer<CrucibleRecipe>> CRUCIBLE_SERIALIZER =
            SERIALIZERS.register("crucible", () -> new RecipeSerializer<>(CrucibleRecipe.CODEC, CrucibleRecipe.STREAM_CODEC));

    public static final Supplier<RecipeType<CrucibleRecipe>> CRUCIBLE_TYPE =
            TYPES.register("crucible", () -> new RecipeType<CrucibleRecipe>() {
                @Override
                public String toString() {
                    return "crucible";
                }
            });

    public static final Supplier<RecipeSerializer<AlembicRecipe>> ALEMBIC_SERIALIZER =
            SERIALIZERS.register("alembic", () -> new RecipeSerializer<>(AlembicRecipe.CODEC, AlembicRecipe.STREAM_CODEC));

    public static final Supplier<RecipeType<AlembicRecipe>> ALEMBIC_TYPE =
            TYPES.register("alembic", () -> new RecipeType<AlembicRecipe>() {
                @Override
                public String toString() {
                    return "alembic";
                }
            });

    public static final Supplier<RecipeSerializer<net.yigitguven.chymistry.wood.TreatedWoodRecipe>> TREATED_WOOD_SERIALIZER =
            SERIALIZERS.register("crafting_special_treated_wood", () -> new RecipeSerializer<>(net.yigitguven.chymistry.wood.TreatedWoodRecipe.CODEC, net.yigitguven.chymistry.wood.TreatedWoodRecipe.STREAM_CODEC));
}
