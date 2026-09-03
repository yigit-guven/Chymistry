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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.yigitguven.chymistry.recipe.CauldronRecipe;

public final class CauldronRecipeCategory implements IRecipeCategory<RecipeHolder<CauldronRecipe>> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public CauldronRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(116, 42);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Blocks.CAULDRON));
        this.arrow = guiHelper.createAnimatedRecipeArrow(60);
    }

    private static ItemStack getVisualCauldron(net.minecraft.world.level.block.Block block) {
        if (block == Blocks.WATER_CAULDRON || block == Blocks.LAVA_CAULDRON || block == Blocks.POWDER_SNOW_CAULDRON || block == Blocks.CAULDRON) {
            return new ItemStack(Blocks.CAULDRON);
        }
        ItemStack stack = new ItemStack(block);
        return stack.isEmpty() ? new ItemStack(Blocks.CAULDRON) : stack;
    }

    @Override
    public RecipeType<RecipeHolder<CauldronRecipe>> getRecipeType() {
        return ChymistryJeiPlugin.CAULDRON;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.chymistry.cauldron");
    }

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
    public void draw(RecipeHolder<CauldronRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 57, 12);

        CauldronRecipe recipe = recipeHolder.value();
        Font font = Minecraft.getInstance().font;
        int seconds = recipe.processingTime() / 20;
        Component timeText = Component.literal(seconds + "s");
        int textWidth = font.width(timeText);
        guiGraphics.text(font, timeText, 69 - textWidth / 2, 30, 0xFF808080, false);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<CauldronRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        CauldronRecipe recipe = recipeHolder.value();
        if (recipe.levelCost() > 0) {
            tooltip.add(Component.translatable("jei.chymistry.cauldron.water_cost", recipe.levelCost()).withStyle(net.minecraft.ChatFormatting.GRAY));
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CauldronRecipe> recipeHolder, IFocusGroup focuses) {
        CauldronRecipe recipe = recipeHolder.value();

        builder.addInputSlot(11, 12)
                .setStandardSlotBackground()
                .addIngredients(recipe.ingredient());

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 34, 12)
                .setStandardSlotBackground()
                .addItemStack(getVisualCauldron(recipe.cauldronBlock()));

        builder.addOutputSlot(89, 12)
                .setOutputSlotBackground()
                .addItemStack(recipe.output().create());
    }
}
