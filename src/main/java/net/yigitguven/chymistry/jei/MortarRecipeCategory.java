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

public final class MortarRecipeCategory implements IRecipeCategory<net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe>> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable meshIcon;

    public MortarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(111, 58);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.MORTAR.get()));
        this.arrow = guiHelper.drawableBuilder(net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/grey_arrow.png"), 0, 0, 45, 39).setTextureSize(45, 39).build();
        this.meshIcon = guiHelper.drawableBuilder(net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/mesh_icon.png"), 0, 0, 18, 20).setTextureSize(18, 20).build();
    }

    @Override
    public RecipeType<net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe>> getRecipeType() {
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
    public void draw(net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe> recipeHolder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        MortarRecipe recipe = recipeHolder.value();
        this.arrow.draw(guiGraphics, 33, 9);
        
        net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;
        
        // Draw press count in bottom right
        net.minecraft.network.chat.Component pressesText = net.minecraft.network.chat.Component.translatable("jei.chymistry.mortar.clicks", recipe.presses());
        int textWidth = font.width(pressesText);
        
        int iconWidth = 9;
        int spacing = 2;
        int iconX = 111 - 2 - iconWidth;
        int textX = iconX - spacing - textWidth;
        
        guiGraphics.text(font, pressesText, textX, 48, 0xFF808080, false);
        
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(0.5f, 0.5f);
        this.meshIcon.draw(guiGraphics, iconX * 2, 47 * 2);
        guiGraphics.pose().popMatrix();
        
        // Draw speed indicator above the arrow
        String speedKey = "jei.chymistry.mortar.speed." + recipe.clickType().name().toLowerCase(java.util.Locale.ROOT);
        net.minecraft.network.chat.Component speedText = net.minecraft.network.chat.Component.translatable(speedKey);
        int speedWidth = font.width(speedText);
        
        // Top right corner
        int speedX = 111 - speedWidth - 2;
        guiGraphics.text(font, speedText, speedX, 1, 0xFF808080, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, net.minecraft.world.item.crafting.RecipeHolder<MortarRecipe> recipeHolder, IFocusGroup focuses) {
        MortarRecipe recipe = recipeHolder.value();
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
          .add(recipe.output().create());

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
          .add(new ItemStack(ModBlocks.MORTAR.get()));
    }
}
