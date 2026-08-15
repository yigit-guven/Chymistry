package net.yigitguven.chymistry.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.yigitguven.chymistry.block.ModBlocks;
import net.yigitguven.chymistry.recipe.MortarRecipe;

public final class MortarRecipeCategory implements IRecipeCategory<MortarRecipe> {

    private final IDrawable background;
    private final IDrawable icon;

    public MortarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(116, 75); // Sized to fit 3 input slots and 1 output slot
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.MORTAR.get()));
    }

    @Override
    public RecipeType<MortarRecipe> getRecipeType() {
        return ChymistryJeiPlugin.MORTAR;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.chymistry.mortar");
    }

    public int getWidth() {
        return 116;
    }

    @Override
    public int getHeight() {
        return 75;
    }

    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MortarRecipe recipe, IFocusGroup focuses) {
        // According to MortarMenu slot 0: 26, 17
        if (recipe.inputs().size() > 0) {
            builder.addInputSlot(26, 17)
              .setStandardSlotBackground()
              .addIngredients(recipe.inputs().get(0).ingredient());
        }

        // According to MortarMenu slot 1: 26, 35
        if (recipe.inputs().size() > 1) {
            builder.addInputSlot(26, 35)
              .setStandardSlotBackground()
              .addIngredients(recipe.inputs().get(1).ingredient());
        }

        // According to MortarMenu slot 2: 26, 53
        if (recipe.inputs().size() > 2) {
            builder.addInputSlot(26, 53)
              .setStandardSlotBackground()
              .addIngredients(recipe.inputs().get(2).ingredient());
        }

        // According to MortarMenu slot 3 (output): 95, 35
        builder.addOutputSlot(95, 35)
          .setOutputSlotBackground()
          .addItemStack(recipe.output().create());

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
          .addItemStack(new ItemStack(ModBlocks.MORTAR.get()));
    }
}
