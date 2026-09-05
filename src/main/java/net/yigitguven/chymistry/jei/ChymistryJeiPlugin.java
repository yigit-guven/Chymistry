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
import mezz.jei.api.registration.ISubtypeRegistration;
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe>> MORTAR = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "mortar", (Class) net.minecraft.world.item.crafting.RecipeHolder.class);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.BurningRecipe>> BURNING = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "burning", (Class) net.minecraft.world.item.crafting.RecipeHolder.class);

    public static final mezz.jei.api.recipe.RecipeType<ProductionComposterJeiRecipe> PRODUCTION_COMPOSTING = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "production_composting", ProductionComposterJeiRecipe.class);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CrucibleRecipe>> CRUCIBLE = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "crucible", (Class) net.minecraft.world.item.crafting.RecipeHolder.class);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.AlembicRecipe>> ALEMBIC = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "alembic", (Class) net.minecraft.world.item.crafting.RecipeHolder.class);

    public static final mezz.jei.api.recipe.RecipeType<TreatedWoodJeiRecipe> TREATED_WOOD = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "treated_wood", TreatedWoodJeiRecipe.class);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final mezz.jei.api.recipe.RecipeType<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CauldronRecipe>> CAULDRON = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "cauldron", (Class) net.minecraft.world.item.crafting.RecipeHolder.class);

    public static final mezz.jei.api.recipe.RecipeType<SoapJeiRecipe> SOAP_BLEACHING = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "soap_bleaching", SoapJeiRecipe.class);

    public static final mezz.jei.api.recipe.RecipeType<FreezingPowderJeiRecipe> FREEZING_CONVERSION = mezz.jei.api.recipe.RecipeType
            .create(Chymistry.MODID, "freezing_conversion", FreezingPowderJeiRecipe.class);

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
        registration.addRecipeCategories(new AlembicRecipeCategory(guiHelper));
        registration.addRecipeCategories(new TreatedWoodRecipeCategory(guiHelper));
        registration.addRecipeCategories(new CauldronRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SoapRecipeCategory(guiHelper));
        registration.addRecipeCategories(new FreezingPowderRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().getSingleplayerServer() != null) {
            RecipeManager manager = Minecraft.getInstance().getSingleplayerServer().getRecipeManager();
            java.util.List<net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe>> recipes = manager.getRecipes()
                    .stream()
                    .filter(holder -> holder.value() instanceof MortarRecipe)
                    .map(holder -> (net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe>) (Object) holder)
                    .toList();
            registration.addRecipes(MORTAR, recipes);

            java.util.List<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.BurningRecipe>> burningRecipes = manager
                    .getRecipes().stream()
                    .filter(holder -> holder.value() instanceof net.yigitguven.chymistry.recipe.BurningRecipe)
                    .map(holder -> (net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.BurningRecipe>) (Object) holder)
                    .toList();
            registration.addRecipes(BURNING, burningRecipes);

            registration.addRecipes(PRODUCTION_COMPOSTING, java.util.List.of(
                    new ProductionComposterJeiRecipe(new ItemStack(net.yigitguven.chymistry.item.ModItems.ASH.get()),
                            new ItemStack(net.yigitguven.chymistry.item.ModItems.NITER_DUST.get())),
                    new ProductionComposterJeiRecipe(
                            java.util.List.of(
                                    new ItemStack(net.minecraft.world.item.Items.BONE_MEAL),
                                    new ItemStack(net.yigitguven.chymistry.item.ModItems.ASH.get()),
                                    new ItemStack(net.yigitguven.chymistry.item.ModItems.NITER_DUST.get())
                            ),
                            new ItemStack(net.yigitguven.chymistry.item.ModItems.SUPER_FERTILIZER.get())
                    ),
                    new ProductionComposterJeiRecipe(
                            java.util.List.of(
                                    new ItemStack(net.yigitguven.chymistry.item.ModItems.ALCOHOL_BOTTLE.get()),
                                    new ItemStack(net.minecraft.world.item.Items.RAW_COPPER)
                            ),
                            new ItemStack(net.minecraft.world.item.Items.DYE.cyan(), 2)
                    )
            ));

            java.util.List<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CrucibleRecipe>> crucibleRecipes = manager
                    .getRecipes().stream()
                    .filter(holder -> holder.value() instanceof net.yigitguven.chymistry.recipe.CrucibleRecipe)
                    .map(holder -> (net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CrucibleRecipe>) (Object) holder)
                    .toList();
            registration.addRecipes(CRUCIBLE, crucibleRecipes);

            java.util.List<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.AlembicRecipe>> alembicRecipes = manager
                    .getRecipes().stream()
                    .filter(holder -> holder.value() instanceof net.yigitguven.chymistry.recipe.AlembicRecipe)
                    .map(holder -> (net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.AlembicRecipe>) (Object) holder)
                    .toList();
            registration.addRecipes(ALEMBIC, alembicRecipes);

            java.util.List<net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CauldronRecipe>> cauldronRecipes = manager
                    .getRecipes().stream()
                    .filter(holder -> holder.value() instanceof net.yigitguven.chymistry.recipe.CauldronRecipe)
                    .map(holder -> (net.minecraft.world.item.crafting.RecipeHolder<net.yigitguven.chymistry.recipe.CauldronRecipe>) (Object) holder)
                    .toList();
            registration.addRecipes(CAULDRON, cauldronRecipes);
        }

        java.util.List<TreatedWoodJeiRecipe> treatedWoodRecipes = net.minecraft.core.registries.BuiltInRegistries.ITEM
                .stream()
                .map(ItemStack::new)
                .filter(net.yigitguven.chymistry.wood.TreatedWoodHelper::isWoodMaterial)
                .map(woodStack -> new TreatedWoodJeiRecipe(woodStack,
                        net.yigitguven.chymistry.wood.TreatedWoodHelper.makeTreated(woodStack, 8)))
                .toList();
        registration.addRecipes(TREATED_WOOD, treatedWoodRecipes);

        registration.addRecipes(SOAP_BLEACHING, net.yigitguven.chymistry.util.SoapCleaningHelper.getJeiRecipes());

        registration.addRecipes(FREEZING_CONVERSION, java.util.List.of(
                new FreezingPowderJeiRecipe(new ItemStack(net.minecraft.world.item.Items.WATER_BUCKET), new ItemStack(net.minecraft.world.level.block.Blocks.BLUE_ICE)),
                new FreezingPowderJeiRecipe(new ItemStack(net.minecraft.world.item.Items.LAVA_BUCKET), new ItemStack(net.minecraft.world.level.block.Blocks.OBSIDIAN))
        ));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MORTAR, new ItemStack(ModBlocks.MORTAR.get()));
        registration.addCraftingStation(BURNING, new ItemStack(net.minecraft.world.item.Items.FLINT_AND_STEEL));
        registration.addCraftingStation(BURNING, new ItemStack(net.minecraft.world.item.Items.FIRE_CHARGE));
        registration.addCraftingStation(PRODUCTION_COMPOSTING,
                new ItemStack(net.minecraft.world.level.block.Blocks.COMPOSTER));
        registration.addCraftingStation(CRUCIBLE, new ItemStack(ModBlocks.BRICK_CRUCIBLE.get()));
        registration.addCraftingStation(CRUCIBLE, new ItemStack(ModBlocks.DEEPSLATE_CRUCIBLE.get()));
        registration.addCraftingStation(CRUCIBLE, new ItemStack(ModBlocks.NETHERITE_CRUCIBLE.get()));
        registration.addCraftingStation(ALEMBIC, new ItemStack(ModBlocks.ALEMBIC.get()));
        registration.addCraftingStation(TREATED_WOOD,
                new ItemStack(net.minecraft.world.level.block.Blocks.CRAFTING_TABLE));
        registration.addCraftingStation(TREATED_WOOD,
                new ItemStack(net.yigitguven.chymistry.item.ModItems.CREOSOTE_OIL.get()));
        registration.addCraftingStation(CAULDRON,
                new ItemStack(net.minecraft.world.level.block.Blocks.CAULDRON));
        registration.addCraftingStation(CAULDRON,
                new ItemStack(net.minecraft.world.item.Items.WATER_BUCKET));
        registration.addCraftingStation(SOAP_BLEACHING,
                new ItemStack(net.yigitguven.chymistry.item.ModItems.SOAP.get()));
        registration.addCraftingStation(SOAP_BLEACHING,
                new ItemStack(net.minecraft.world.level.block.Blocks.CRAFTING_TABLE));
        registration.addCraftingStation(FREEZING_CONVERSION,
                new ItemStack(net.yigitguven.chymistry.item.ModItems.FREEZING_POWDER.get()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MortarMenu.class, ModMenus.MORTAR_MENU.get(), MORTAR, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(net.yigitguven.chymistry.menu.CrucibleMenu.class,
                ModMenus.CRUCIBLE_MENU.get(), CRUCIBLE, 0, 5, 6, 36);
        registration.addRecipeTransferHandler(net.yigitguven.chymistry.menu.AlembicMenu.class,
                ModMenus.ALEMBIC_MENU.get(), ALEMBIC, 0, 4, 6, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MortarScreen.class, 43, 16, 50, 52, MORTAR);
        registration.addRecipeClickArea(net.yigitguven.chymistry.screen.CrucibleScreen.class, 122, 53, 14, 14,
                CRUCIBLE);
        registration.addRecipeClickArea(net.yigitguven.chymistry.screen.AlembicScreen.class, 63, 35, 68, 14, ALEMBIC);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        net.minecraft.core.registries.BuiltInRegistries.ITEM.stream()
                .filter(item -> net.yigitguven.chymistry.wood.TreatedWoodHelper.isWoodMaterial(new ItemStack(item)))
                .forEach(item -> registration.registerSubtypeInterpreter(item, (stack, context) -> {
                    return net.yigitguven.chymistry.wood.TreatedWoodHelper.isTreated(stack) ? "treated" : "";
                }));
    }
}
