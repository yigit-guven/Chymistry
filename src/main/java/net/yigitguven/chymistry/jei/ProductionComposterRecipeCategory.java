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
import net.minecraft.world.level.block.Blocks;
import net.yigitguven.chymistry.Chymistry;

public final class ProductionComposterRecipeCategory implements IRecipeCategory<ProductionComposterJeiRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public ProductionComposterRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(110, 40);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Blocks.COMPOSTER));
        this.arrow = guiHelper.createAnimatedRecipeArrow(40);
    }

    @Override
    public RecipeType<ProductionComposterJeiRecipe> getRecipeType() {
        return ChymistryJeiPlugin.PRODUCTION_COMPOSTING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.chymistry.production_composting");
    }

    public int getWidth() {
        return 110;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(ProductionComposterJeiRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 55, 12);
        
        net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;
        Component levelText = Component.translatable("jei.chymistry.production_composting.level");
        int textWidth = font.width(levelText);
        int textX = 41 - textWidth / 2; // Center under the composter (x=33 + 8)
        guiGraphics.text(font, levelText, textX, 30, 0xFF808080, false);
    }

    @Override
    public void getTooltip(mezz.jei.api.gui.builder.ITooltipBuilder tooltip, ProductionComposterJeiRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;
        Component levelText = Component.translatable("jei.chymistry.production_composting.level");
        int textWidth = font.width(levelText);
        int textX = 41 - textWidth / 2;
        
        if (mouseX >= textX && mouseX <= textX + textWidth && mouseY >= 30 && mouseY <= 39) {
            tooltip.add(Component.translatable("jei.chymistry.production_composting.tooltip").withStyle(net.minecraft.ChatFormatting.GRAY));
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ProductionComposterJeiRecipe recipe, IFocusGroup focuses) {
        // Ash input
        builder.addInputSlot(11, 12)
          .setStandardSlotBackground()
          .addItemStack(recipe.input());

        // Composter as a visual catalyst next to the input
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 33, 12)
          .setStandardSlotBackground()
          .addItemStack(new ItemStack(Blocks.COMPOSTER));

        // Output
        builder.addOutputSlot(85, 12)
          .setOutputSlotBackground()
          .addItemStack(recipe.output());
    }
}
