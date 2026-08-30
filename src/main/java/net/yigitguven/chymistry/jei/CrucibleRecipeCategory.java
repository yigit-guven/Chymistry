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
import net.yigitguven.chymistry.recipe.CrucibleRecipe;
import net.yigitguven.chymistry.recipe.SizedIngredient;

import java.util.List;

public final class CrucibleRecipeCategory implements IRecipeCategory<net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe>> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public CrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(160, 80);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(net.yigitguven.chymistry.item.ModItems.BRICK_CRUCIBLE.get()));
        this.arrow = guiHelper.drawableBuilder(net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/grey_arrow.png"), 0, 0, 45, 39).setTextureSize(45, 39).build();
    }

    @Override
    public RecipeType<net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe>> getRecipeType() {
        return ChymistryJeiPlugin.CRUCIBLE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.chymistry.crucible");
    }

    public int getWidth() {
        return 160;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe> recipeHolder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 70, 20);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe> recipeHolder, IFocusGroup focuses) {
        CrucibleRecipe recipe = recipeHolder.value();
        
        int[][] slotPositions = {
                {45, 26}, {65, 26}, {55, 43}, {75, 43}
        };

        for (int i = 0; i < recipe.inputs().size() && i < 4; i++) {
            builder.addInputSlot(slotPositions[i][0], slotPositions[i][1])
              .setStandardSlotBackground()
              .add(recipe.inputs().get(i).ingredient());
        }

        if (recipe.container().isPresent()) {
            builder.addInputSlot(87, 43)
              .setStandardSlotBackground()
              .add(recipe.container().get().ingredient());
        }

        builder.addOutputSlot(129, 26)
          .setOutputSlotBackground()
          .add(recipe.output().create());
    }
}
