package net.yigitguven.chymistry.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.block.ModBlocks;
import net.yigitguven.chymistry.recipe.AlembicRecipe;
import net.yigitguven.chymistry.recipe.SizedIngredient;

import java.util.List;

public final class AlembicRecipeCategory implements IRecipeCategory<RecipeHolder<AlembicRecipe>> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/alembicui.png");
    private static final Identifier EMPTY_ARROW_TEX = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/alembicemptyarrow.png");
    private static final Identifier EMPTY_GAS_TEX = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/alembicemptygasindicator.png");
    private static final Identifier EMPTY_FIRE_TEX = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/empty_fire.png");
    private static final Identifier FUEL_ICON_TEX = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/fuelinputicon.png");

    private static final Identifier FULL_ARROW = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/alembicfullarrow.png");
    private static final Identifier FULL_GAS = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/gasfull.png");
    private static final Identifier FULL_FIRE = Identifier.fromNamespaceAndPath(Chymistry.MODID, "textures/gui/full_fire.png");

    private static final int ARROW_WIDTH = 68;
    private static final int ARROW_HEIGHT = 14;
    private static final int GAP_START = 25;
    private static final int GAP_END = 42;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable emptyArrow;
    private final IDrawable emptyGasIndicator;
    private final IDrawable emptyFire;
    private List<ItemStack> fuelStacks;

    public AlembicRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, 20, 2, 140, 74).setTextureSize(176, 166).build();
        this.emptyArrow = guiHelper.drawableBuilder(EMPTY_ARROW_TEX, 0, 0, 68, 14).setTextureSize(68, 14).build();
        this.emptyGasIndicator = guiHelper.drawableBuilder(EMPTY_GAS_TEX, 0, 0, 12, 29).setTextureSize(12, 29).build();
        this.emptyFire = guiHelper.drawableBuilder(EMPTY_FIRE_TEX, 0, 0, 14, 14).setTextureSize(14, 14).build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.ALEMBIC.get()));
    }

    @Override
    public RecipeType<RecipeHolder<AlembicRecipe>> getRecipeType() {
        return ChymistryJeiPlugin.ALEMBIC;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.chymistry.alembic");
    }

    public int getWidth() {
        return 140;
    }

    @Override
    public int getHeight() {
        return 74;
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

    private List<ItemStack> getFuelStacks() {
        if (this.fuelStacks == null) {
            if (Minecraft.getInstance().level != null) {
                var fuelValues = Minecraft.getInstance().level.fuelValues();
                this.fuelStacks = net.minecraft.core.registries.BuiltInRegistries.ITEM.stream()
                        .map(ItemStack::new)
                        .filter(stack -> fuelValues.burnDuration(stack) > 0)
                        .toList();
            } else {
                return List.of(new ItemStack(net.minecraft.world.item.Items.COAL), new ItemStack(net.minecraft.world.item.Items.CHARCOAL));
            }
        }
        return this.fuelStacks;
    }

    @Override
    public void draw(RecipeHolder<AlembicRecipe> recipeHolder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.emptyArrow.draw(guiGraphics, 43, 33);
        this.emptyGasIndicator.draw(guiGraphics, 71, 4);
        this.emptyFire.draw(guiGraphics, 70, 34);

        long time = System.currentTimeMillis();
        double cycle = (time % 4000L) / 4000.0;

        double fireCycle = (time % 2000L) / 2000.0;
        int fireHeight = (int) (14.0 * (1.0 - fireCycle));
        if (fireHeight > 0) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, FULL_FIRE, 70, 34 + (14 - fireHeight), 0f, 14f - fireHeight, 14, fireHeight, 14, 14);
        }

        int gapSize = GAP_END - GAP_START;
        int activeWidth = ARROW_WIDTH - gapSize;
        int activePixels = (int) (cycle * activeWidth);

        if (activePixels > 0) {
            if (activePixels <= GAP_START) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, FULL_ARROW, 43, 33, 0f, 0f,
                        activePixels, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
            } else {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, FULL_ARROW, 43, 33, 0f, 0f, GAP_START,
                        ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);

                int remainingPixels = activePixels - GAP_START;
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, FULL_ARROW, 43 + GAP_END, 33,
                        (float) GAP_END, 0f, remainingPixels, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
            }
        }

        if (recipeHolder.value().secondaryOutput().isPresent()) {
            int gasHeight = (int) (29.0 * cycle);
            if (gasHeight > 0) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, FULL_GAS, 71, 4 + (29 - gasHeight), 0f, 29f - gasHeight, 12, gasHeight, 12, 29);
            }
        }

        int processingTime = recipeHolder.value().processingTime();
        if (processingTime > 0) {
            int seconds = processingTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", seconds);
            Font font = Minecraft.getInstance().font;
            int stringWidth = font.width(timeString);
            guiGraphics.text(font, timeString, getWidth() - stringWidth - 2, getHeight() - font.lineHeight, 0xFF808080, false);
        }
    }

    @Override
    public void getTooltip(mezz.jei.api.gui.builder.ITooltipBuilder tooltip, RecipeHolder<AlembicRecipe> recipeHolder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (recipeHolder.value().secondaryOutput().isPresent()) {
            if (mouseX >= 71 && mouseX < 83 && mouseY >= 4 && mouseY < 33) {
                tooltip.add(Component.translatable("jei.chymistry.alembic.bottle_tooltip").withStyle(net.minecraft.ChatFormatting.AQUA));
            }
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<AlembicRecipe> recipeHolder, IFocusGroup focuses) {
        AlembicRecipe recipe = recipeHolder.value();

        int[][] slotPositions = {
                {5, 22}, {23, 22}, {5, 40}, {23, 40}
        };

        for (int i = 0; i < 4; i++) {
            var slot = builder.addSlot(RecipeIngredientRole.INPUT, slotPositions[i][0], slotPositions[i][1])
                    .setStandardSlotBackground();
            if (i < recipe.inputs().size()) {
                slot.addItemStacks(getStacks(recipe.inputs().get(i)));
            }
        }

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 68, 51)
                .setStandardSlotBackground()
                .addItemStacks(getFuelStacks());

        if (recipe.output().isPresent()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 34)
                    .setOutputSlotBackground()
                    .addItemStack(recipe.output().get().create());
        }

        if (recipe.secondaryOutput().isPresent()) {
            if (recipe.bottle().isPresent()) {
                builder.addSlot(RecipeIngredientRole.INPUT, 90, 10)
                        .setStandardSlotBackground()
                        .addItemStacks(getStacks(recipe.bottle().get()));
            } else {
                builder.addSlot(RecipeIngredientRole.INPUT, 90, 10)
                        .setStandardSlotBackground()
                        .addItemStack(new ItemStack(net.minecraft.world.item.Items.GLASS_BOTTLE));
            }

            builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 6)
                    .setOutputSlotBackground()
                    .addItemStack(recipe.secondaryOutput().get().create());
        }

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
                .add(new ItemStack(ModBlocks.ALEMBIC.get()));
    }
}
