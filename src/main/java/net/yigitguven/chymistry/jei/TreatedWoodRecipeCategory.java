package net.yigitguven.chymistry.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.yigitguven.chymistry.item.ModItems;

public final class TreatedWoodRecipeCategory implements IRecipeCategory<TreatedWoodJeiRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public TreatedWoodRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(116, 54);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.CREOSOTE_OIL.get()));
        this.arrow = guiHelper.createAnimatedRecipeArrow(40);
    }

    @Override
    public RecipeType<TreatedWoodJeiRecipe> getRecipeType() {
        return ChymistryJeiPlugin.TREATED_WOOD;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.chymistry.treated_wood");
    }

    @Override
    public int getWidth() {
        return 116;
    }

    @Override
    public int getHeight() {
        return 54;
    }

    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(TreatedWoodJeiRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 60, 19);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TreatedWoodJeiRecipe recipe, IFocusGroup focuses) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int x = col * 18;
                int y = row * 18;
                if (row == 1 && col == 1) {
                    builder.addInputSlot(x, y)
                            .setStandardSlotBackground()
                            .addItemStack(new ItemStack(ModItems.CREOSOTE_OIL.get()));
                } else {
                    builder.addInputSlot(x, y)
                            .setStandardSlotBackground()
                            .addItemStack(recipe.woodInput());
                }
            }
        }

        builder.addOutputSlot(94, 18)
                .setOutputSlotBackground()
                .addItemStack(recipe.treatedOutput());
    }
}
