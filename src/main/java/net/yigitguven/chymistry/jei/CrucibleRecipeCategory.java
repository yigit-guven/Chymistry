package net.yigitguven.chymistry.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
    private final IDrawable emptyArrow1;
    private final IDrawable emptyArrow2;
    private final IDrawable emptyArrow3;
    private final IDrawable emptyArrow4;

    public CrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
            net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/crucible_gui.png"),
            18, 17, 149, 54
        ).setTextureSize(176, 166).build();
        this.emptyArrow1 = guiHelper.drawableBuilder(
            net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/crucibleemptyarrow1.png"),
            0, 0, 109, 14
        ).setTextureSize(109, 14).build();
        this.emptyArrow2 = guiHelper.drawableBuilder(
            net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/crucibleemptyarrow2.png"),
            0, 0, 109, 14
        ).setTextureSize(109, 14).build();
        this.emptyArrow3 = guiHelper.drawableBuilder(
            net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/crucibleemptyarrow3.png"),
            0, 0, 109, 14
        ).setTextureSize(109, 14).build();
        this.emptyArrow4 = guiHelper.drawableBuilder(
            net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/cruciblearrowempty.png"),
            0, 0, 109, 14
        ).setTextureSize(109, 14).build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.BRICK_CRUCIBLE.get()));
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
        return 149;
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

    private static final net.minecraft.resources.Identifier FIRE_EMPTY = net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/empty_fire.png");
    private static final net.minecraft.resources.Identifier FIRE_FULL = net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/full_fire.png");
    private static final net.minecraft.resources.Identifier SNOW_EMPTY = net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/empty_snow.png");
    private static final net.minecraft.resources.Identifier SNOW_FULL = net.minecraft.resources.Identifier.fromNamespaceAndPath(net.yigitguven.chymistry.Chymistry.MODID, "textures/gui/full_snow.png");

    @Override
    public void draw(net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe> recipeHolder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        int inputs = recipeHolder.value().inputs().size();
        if (inputs == 1) {
            emptyArrow1.draw(guiGraphics, 8, 20);
        } else if (inputs == 2) {
            emptyArrow2.draw(guiGraphics, 8, 20);
        } else if (inputs == 3) {
            emptyArrow3.draw(guiGraphics, 8, 20);
        } else {
            emptyArrow4.draw(guiGraphics, 8, 20);
        }

        int minHeat = recipeHolder.value().minHeat();
        int maxHeat = recipeHolder.value().maxHeat();

        long time = System.currentTimeMillis();
        double cycle = (time % 4000L) / 4000.0;
        double progress = (Math.sin(cycle * Math.PI * 2) + 1.0) / 2.0;
        float currentHeat = (float) (minHeat + (maxHeat - minHeat) * progress);

        int heatIndicatorX = 80;
        int heatIndicatorY = 20;
        int HEAT_WIDTH = 14;
        int HEAT_HEIGHT = 14;

        if (maxHeat >= 0) {
            guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, FIRE_EMPTY, heatIndicatorX, heatIndicatorY, 0f, 0f, HEAT_WIDTH, HEAT_HEIGHT, HEAT_WIDTH, HEAT_HEIGHT);
            if (currentHeat > 0) {
                float fillRatio = Math.min(1.0f, currentHeat / 999.0f);
                int steps = (int) Math.ceil((HEAT_HEIGHT - 1) * fillRatio);
                if (steps > 0) {
                    int fillHeight = steps + 1;
                    guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, FIRE_FULL, heatIndicatorX,
                            heatIndicatorY + HEAT_HEIGHT - fillHeight,
                            0f, (float) (HEAT_HEIGHT - fillHeight),
                            HEAT_WIDTH, fillHeight, HEAT_WIDTH, HEAT_HEIGHT);
                }
            }
        } else {
            guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, SNOW_EMPTY, heatIndicatorX, heatIndicatorY, 0f, 0f, HEAT_WIDTH, HEAT_HEIGHT, HEAT_WIDTH, HEAT_HEIGHT);
            float fillRatio = Math.min(1.0f, Math.abs(currentHeat) / 999.0f);
            int steps = (int) Math.ceil((HEAT_HEIGHT - 1) * fillRatio);
            if (steps > 0) {
                int fillHeight = steps + 1;
                guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, SNOW_FULL, heatIndicatorX,
                        heatIndicatorY + HEAT_HEIGHT - fillHeight,
                        0f, (float) (HEAT_HEIGHT - fillHeight),
                        HEAT_WIDTH, fillHeight, HEAT_WIDTH, HEAT_HEIGHT);
            }
        }

        int processingTime = recipeHolder.value().processingTime();
        if (processingTime > 0) {
            int seconds = processingTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", seconds);
            net.minecraft.client.gui.Font font = net.minecraft.client.Minecraft.getInstance().font;
            int stringWidth = font.width(timeString);
            guiGraphics.text(font, timeString, getWidth() - stringWidth - 2, getHeight() - font.lineHeight, 0xFF808080, false);
        }
    }

    @Override
    public void getTooltip(mezz.jei.api.gui.builder.ITooltipBuilder tooltip, net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe> recipeHolder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        int heatIndicatorX = 80;
        int heatIndicatorY = 20;
        int HEAT_WIDTH = 14;
        int HEAT_HEIGHT = 14;

        if (mouseX >= heatIndicatorX && mouseX < heatIndicatorX + HEAT_WIDTH &&
                mouseY >= heatIndicatorY && mouseY < heatIndicatorY + HEAT_HEIGHT) {

            int minHeat = recipeHolder.value().minHeat();
            int maxHeat = recipeHolder.value().maxHeat();

            tooltip.add(Component.translatable("tooltip.chymistry.crucible.min_heat", String.valueOf(minHeat))
                    .withStyle(net.minecraft.ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.chymistry.crucible.max_heat", String.valueOf(maxHeat))
                    .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, net.minecraft.world.item.crafting.RecipeHolder<CrucibleRecipe> recipeHolder, IFocusGroup focuses) {
        CrucibleRecipe recipe = recipeHolder.value();
        
        int[][] slotPositions = {
                {2, 2}, {38, 2}, {2, 36}, {38, 36}
        };

        for (int i = 0; i < recipe.inputs().size() && i < 4; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, slotPositions[i][0], slotPositions[i][1])
              .setStandardSlotBackground()
              .addIngredients(recipe.inputs().get(i).ingredient());
        }

        if (recipe.container().isPresent()) {
            builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 79, 36)
              .setStandardSlotBackground()
              .addIngredients(recipe.container().get().ingredient());
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 126, 19)
          .setOutputSlotBackground()
          .addItemStack(recipe.output().create());
    }
}
