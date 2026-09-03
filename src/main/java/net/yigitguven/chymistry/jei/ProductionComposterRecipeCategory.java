package net.yigitguven.chymistry.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public final class ProductionComposterRecipeCategory implements IRecipeCategory<ProductionComposterJeiRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public ProductionComposterRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(134, 42);
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
        return 134;
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
    public void draw(ProductionComposterJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        int composterX = recipe.inputs().size() > 1 ? 59 : 39;
        int arrowX = recipe.inputs().size() > 1 ? 80 : 64;

        this.arrow.draw(guiGraphics, arrowX, 12);

        Font font = Minecraft.getInstance().font;
        Component levelText = Component.translatable("jei.chymistry.production_composting.level");
        int textWidth = font.width(levelText);
        int textX = (composterX + 8) - textWidth / 2;
        guiGraphics.text(font, levelText, textX, 30, 0xFF808080, false);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, ProductionComposterJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        Component levelText = Component.translatable("jei.chymistry.production_composting.level");
        int textWidth = font.width(levelText);
        int composterX = recipe.inputs().size() > 1 ? 59 : 39;
        int textX = (composterX + 8) - textWidth / 2;

        if (mouseX >= textX && mouseX <= textX + textWidth && mouseY >= 30 && mouseY <= 39) {
            tooltip.add(Component.translatable(recipe.tooltipKey()).withStyle(net.minecraft.ChatFormatting.GRAY));
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ProductionComposterJeiRecipe recipe, IFocusGroup focuses) {
        if (recipe.inputs().size() == 1) {
            builder.addInputSlot(15, 12)
                    .setStandardSlotBackground()
                    .addItemStack(recipe.inputs().get(0));

            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 39, 12)
                    .setStandardSlotBackground()
                    .addItemStack(new ItemStack(Blocks.COMPOSTER));

            builder.addOutputSlot(96, 12)
                    .setOutputSlotBackground()
                    .addItemStack(recipe.output());
        } else {
            int startX = 3;
            for (int i = 0; i < recipe.inputs().size(); i++) {
                builder.addInputSlot(startX + i * 18, 12)
                        .setStandardSlotBackground()
                        .addItemStack(recipe.inputs().get(i));
            }

            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 59, 12)
                    .setStandardSlotBackground()
                    .addItemStack(new ItemStack(Blocks.COMPOSTER));

            builder.addOutputSlot(108, 12)
                    .setOutputSlotBackground()
                    .addItemStack(recipe.output());
        }
    }
}
