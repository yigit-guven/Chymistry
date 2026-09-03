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
import net.minecraft.world.item.Items;
import net.yigitguven.chymistry.recipe.BurningRecipe;
import net.minecraft.resources.Identifier;
import net.yigitguven.chymistry.Chymistry;

public final class BurningRecipeCategory implements IRecipeCategory<net.minecraft.world.item.crafting.RecipeHolder<BurningRecipe>> {

    private static final Identifier FIRE_ARROW = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/fire_arrow.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable emptyArrow;

    public BurningRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(100, 50);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Items.FLINT_AND_STEEL));
        this.emptyArrow = guiHelper.drawableBuilder(Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/empty_fire_arrow.png"), 0, 0, 21, 16).setTextureSize(21, 16).build();
    }

    @Override
    public RecipeType<net.minecraft.world.item.crafting.RecipeHolder<BurningRecipe>> getRecipeType() {
        return ChymistryJeiPlugin.BURNING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.chymistry.burning");
    }

    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(net.minecraft.world.item.crafting.RecipeHolder<BurningRecipe> recipeHolder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        BurningRecipe recipe = recipeHolder.value();
        this.emptyArrow.draw(guiGraphics, 36, 12);

        long time = System.currentTimeMillis();
        double cycle = (time % 2000L) / 2000.0;
        int activePixels = (int) (cycle * 21);
        if (activePixels > 0) {
            guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, FIRE_ARROW, 36, 12, 0f, 0f, activePixels, 16, 21, 16);
        }
        
        net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;
        net.minecraft.network.chat.Component chanceText = net.minecraft.network.chat.Component.translatable("jei.chymistry.burning.chance", recipe.chance());
        int textWidth = font.width(chanceText);
        guiGraphics.text(font, chanceText, 50 - textWidth / 2, 37, 0xFF808080, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, net.minecraft.world.item.crafting.RecipeHolder<BurningRecipe> recipeHolder, IFocusGroup focuses) {
        BurningRecipe recipe = recipeHolder.value();
        
        builder.addInputSlot(11, 12)
          .setStandardSlotBackground()
          .addIngredients(recipe.ingredient());

        builder.addOutputSlot(70, 12)
          .setOutputSlotBackground()
          .add(recipe.output().create());

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
          .add(new ItemStack(Items.FLINT_AND_STEEL));
    }
}
