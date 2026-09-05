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

public final class FreezingPowderRecipeCategory implements IRecipeCategory<FreezingPowderJeiRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public FreezingPowderRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(116, 42);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.FREEZING_POWDER.get()));
        this.arrow = guiHelper.createAnimatedRecipeArrow(40);
    }

    @Override
    public RecipeType<FreezingPowderJeiRecipe> getRecipeType() {
        return ChymistryJeiPlugin.FREEZING_CONVERSION;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.chymistry.freezing_conversion");
    }

    @Override
    public int getWidth() {
        return 116;
    }

    @Override
    public int getHeight() {
        return 42;
    }

    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(FreezingPowderJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 54, 12);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FreezingPowderJeiRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(10, 12)
            .setStandardSlotBackground()
            .addItemStack(recipe.fluidInput());

        builder.addInputSlot(30, 12)
            .setStandardSlotBackground()
            .addItemStack(new ItemStack(ModItems.FREEZING_POWDER.get()));

        builder.addOutputSlot(84, 12)
            .setOutputSlotBackground()
            .addItemStack(recipe.output());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, FreezingPowderJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        tooltip.add(Component.translatable("jei.chymistry.freezing_conversion.desc"));
    }
}
