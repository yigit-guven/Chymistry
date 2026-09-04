package net.yigitguven.chymistry.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.yigitguven.chymistry.item.ModItems;

public final class SoapRecipeCategory implements IRecipeCategory<SoapJeiRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public SoapRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(116, 54);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.SOAP.get()));
        this.arrow = guiHelper.createAnimatedRecipeArrow(40);
    }

    @Override
    public RecipeType<SoapJeiRecipe> getRecipeType() {
        return ChymistryJeiPlugin.SOAP_BLEACHING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.chymistry.soap_bleaching");
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
    public void draw(SoapJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 54, 18);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SoapJeiRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(10, 18)
            .setStandardSlotBackground()
            .addItemStack(recipe.dyedInput());

        builder.addInputSlot(30, 18)
            .setStandardSlotBackground()
            .addItemStack(new ItemStack(ModItems.SOAP.get()));

        builder.addOutputSlot(84, 18)
            .setOutputSlotBackground()
            .addItemStack(recipe.whiteOutput());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, SoapJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        tooltip.add(Component.translatable("jei.chymistry.soap_bleaching.desc"));
    }
}
