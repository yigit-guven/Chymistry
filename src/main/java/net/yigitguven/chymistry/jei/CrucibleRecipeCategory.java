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
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.BRICK_CRUCIBLE.get()));
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

    private List<ItemStack> getStacks(SizedIngredient sizedIngredient) {
        return sizedIngredient.ingredient().items()
            .map(holder -> new ItemStack(holder, sizedIngredient.count()))
            .toList();
    }

    @Override
    public void draw(net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe> recipeHolder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        CrucibleRecipe recipe = recipeHolder.value();
        net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;

        // Draw arrow
        arrow.draw(guiGraphics, 70, 20);

        // Draw heat info
        String heatText = recipe.minHeat() + " - " + recipe.maxHeat() + " Heat";
        int textWidth = font.width(heatText);
        guiGraphics.text(font, heatText, (160 - textWidth) / 2, 5, 0x404040, false);
        
        if (recipe.heatCost() > 0) {
            String costText = "Cost: " + (int)recipe.heatCost() + " Heat";
            int costWidth = font.width(costText);
            guiGraphics.text(font, costText, (160 - costWidth) / 2, 70, 0x8b0000, false);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe> recipeHolder, IFocusGroup focuses) {
        CrucibleRecipe recipe = recipeHolder.value();
        
        int[][] slotPositions = {
            {10, 15}, {46, 15},
            {10, 43}, {46, 43}
        };

        for (int i = 0; i < recipe.inputs().size() && i < 4; i++) {
            builder.addInputSlot(slotPositions[i][0], slotPositions[i][1])
              .setStandardSlotBackground()
              .addItemStacks(getStacks(recipe.inputs().get(i)));
        }

        if (recipe.container().isPresent()) {
            builder.addInputSlot(87, 43)
              .setStandardSlotBackground()
              .addItemStacks(getStacks(recipe.container().get()));
        }

        builder.addOutputSlot(129, 26)
          .setOutputSlotBackground()
          .add(recipe.output().create());

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
          .add(new ItemStack(ModBlocks.BRICK_CRUCIBLE.get()))
          .add(new ItemStack(ModBlocks.DEEPSLATE_CRUCIBLE.get()))
          .add(new ItemStack(ModBlocks.NETHERITE_CRUCIBLE.get()));
    }
}
