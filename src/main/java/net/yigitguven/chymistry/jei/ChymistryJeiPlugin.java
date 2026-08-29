package net.yigitguven.chymistry.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.block.ModBlocks;
import net.yigitguven.chymistry.recipe.MortarRecipe;
import net.yigitguven.chymistry.menu.MortarMenu;
import net.yigitguven.chymistry.menu.ModMenus;
import net.yigitguven.chymistry.screen.MortarScreen;

@JeiPlugin
public class ChymistryJeiPlugin implements IModPlugin {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe>> MORTAR =
            mezz.jei.api.recipe.RecipeType.create(Chymistry.MODID, "mortar", (Class)net.minecraft.world.item.crafting.RecipeHolder.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.BurningRecipe>> BURNING =
            mezz.jei.api.recipe.RecipeType.create(Chymistry.MODID, "burning", (Class)net.minecraft.world.item.crafting.RecipeHolder.class);

    public static final mezz.jei.api.recipe.RecipeType<ProductionComposterJeiRecipe> PRODUCTION_COMPOSTING =
            mezz.jei.api.recipe.RecipeType.create(Chymistry.MODID, "production_composting", ProductionComposterJeiRecipe.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CrucibleRecipe>> CRUCIBLE =
            mezz.jei.api.recipe.RecipeType.create(Chymistry.MODID, "crucible", (Class)net.minecraft.world.item.crafting.RecipeHolder.class);

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(Chymistry.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new MortarRecipeCategory(guiHelper));
        registration.addRecipeCategories(new BurningRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ProductionComposterRecipeCategory(guiHelper));
        registration.addRecipeCategories(new CrucibleRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().getSingleplayerServer() != null) {
            RecipeManager manager = Minecraft.getInstance().getSingleplayerServer().getRecipeManager();
            java.util.List<net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe>> recipes = manager.getRecipes().stream()
                .filter(holder -> holder.value() instanceof MortarRecipe)
                .map(holder -> (net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe>)(Object)holder)
                .toList();
            registration.addRecipes(MORTAR, recipes);

            java.util.List<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.BurningRecipe>> burningRecipes = manager.getRecipes().stream()
                .filter(holder -> holder.value() instanceof net.yigitguven.chymistry.recipe.BurningRecipe)
                .map(holder -> (net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.BurningRecipe>)(Object)holder)
                .toList();
            registration.addRecipes(BURNING, burningRecipes);

            registration.addRecipes(PRODUCTION_COMPOSTING, java.util.List.of(
                new ProductionComposterJeiRecipe(new ItemStack(net.yigitguven.chymistry.item.ModItems.ASH.get()), new ItemStack(net.yigitguven.chymistry.item.ModItems.NITER_DUST.get()))
            ));

            java.util.List<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CrucibleRecipe>> crucibleRecipes = manager.getRecipes().stream()
                .filter(holder -> holder.value() instanceof net.yigitguven.chymistry.recipe.CrucibleRecipe)
                .map(holder -> (net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CrucibleRecipe>)(Object)holder)
                .toList();
            registration.addRecipes(CRUCIBLE, crucibleRecipes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MORTAR, new ItemStack(ModBlocks.MORTAR.get()));
        registration.addCraftingStation(BURNING, new ItemStack(net.minecraft.world.item.Items.FLINT_AND_STEEL));
        registration.addCraftingStation(BURNING, new ItemStack(net.minecraft.world.item.Items.FIRE_CHARGE));
        registration.addCraftingStation(PRODUCTION_COMPOSTING, new ItemStack(net.minecraft.world.level.block.Blocks.COMPOSTER));
        registration.addCraftingStation(PRODUCTION_COMPOSTING, new ItemStack(ModBlocks.NITER_SOIL_COMPOSTER.get()));
        registration.addCraftingStation(CRUCIBLE, new ItemStack(ModBlocks.BRICK_CRUCIBLE.get()));
        registration.addCraftingStation(CRUCIBLE, new ItemStack(ModBlocks.DEEPSLATE_CRUCIBLE.get()));
        registration.addCraftingStation(CRUCIBLE, new ItemStack(ModBlocks.NETHERITE_CRUCIBLE.get()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MortarMenu.class, ModMenus.MORTAR_MENU.get(), MORTAR, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(net.yigitguven.chymistry.menu.CrucibleMenu.class, ModMenus.CRUCIBLE_MENU.get(), CRUCIBLE, 0, 5, 6, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MortarScreen.class, 43, 16, 50, 52, MORTAR);
        registration.addRecipeClickArea(net.yigitguven.chymistry.screen.CrucibleScreen.class, 122, 53, 14, 14, CRUCIBLE);
    }
}
