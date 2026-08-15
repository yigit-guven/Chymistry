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
import net.yigitguven.chymistry.recipe.SizedIngredient;

import java.util.List;

public final class MortarRecipeCategory implements IRecipeCategory<MortarRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public MortarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(111, 58);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.MORTAR.get()));
        this.arrow = guiHelper.drawableBuilder(net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/grey_arrow.png"), 0, 0, 45, 39).setTextureSize(45, 39).build();
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
        return 58;
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
    public void draw(MortarRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 33, 9);
        
        net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;
        
        // Draw press count in bottom right
        net.minecraft.network.chat.Component pressesText = net.minecraft.network.chat.Component.literal(recipe.presses() + " Presses");
        int textWidth = font.width(pressesText);
        guiGraphics.text(font, pressesText, 111 - textWidth - 2, 48, 0xFF808080, false);
        
        // Draw speed indicator above the arrow
        String speedStr = recipe.clickType().name(); // FAST, SLOW, ANY
        speedStr = speedStr.substring(0, 1).toUpperCase() + speedStr.substring(1).toLowerCase();
        net.minecraft.network.chat.Component speedText = net.minecraft.network.chat.Component.literal(speedStr);
        int speedWidth = font.width(speedText);
        
        // Top right corner
        int speedX = 111 - speedWidth - 2;
        guiGraphics.text(font, speedText, speedX, 1, 0xFF808080, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MortarRecipe recipe, IFocusGroup focuses) {
        // According to MortarMenu slot 0: 26, 17
        if (recipe.inputs().size() > 0) {
            builder.addInputSlot(5, 2)
              .setStandardSlotBackground()
              .addItemStacks(getStacks(recipe.inputs().get(0)));
        }

        if (recipe.inputs().size() > 1) {
            builder.addInputSlot(5, 20)
              .setStandardSlotBackground()
              .addItemStacks(getStacks(recipe.inputs().get(1)));
        }

        if (recipe.inputs().size() > 2) {
            builder.addInputSlot(5, 38)
              .setStandardSlotBackground()
              .addItemStacks(getStacks(recipe.inputs().get(2)));
        }

        builder.addOutputSlot(88, 16)
          .setOutputSlotBackground()
          .addItemStack(recipe.output().create());

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
          .addItemStack(new ItemStack(ModBlocks.MORTAR.get()));
    }
}
