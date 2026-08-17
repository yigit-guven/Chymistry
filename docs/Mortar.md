# The Mortar

The **Mortar** is an interactive crafting station utilized for processing and pulverizing materials into intermediate components.

![Mortar Crafting Recipe](images/recipe_mortar_crafting.png)

## Mechanics

Unlike standard crafting stations, the Mortar requires manual player interaction over time.

1. **Input:** The Mortar accepts between 1 to 3 specific item inputs depending on the recipe.
2. **Processing:** Players must repeatedly click the block to advance the crafting progress.
3. **Speed Requirements:** Each recipe mandates a specific clicking frequency to succeed:
   *   **Slow:** Requires a deliberately paced click rate.
   *   **Normal:** Requires a standard, steady click rate.
   *   **Fast:** Requires a rapid click rate.
   *   **Any:** The recipe progresses regardless of the click rate.

If the required click speed is not maintained, processing progress will halt until the correct speed is resumed.

## JEI Integration

Mortar recipes are fully integrated with **Just Enough Items (JEI)**. The JEI interface displays:
*   Required input items and quantities.
*   Total number of clicks required (displayed in the bottom right corner).
*   Required clicking speed (displayed as a tooltip above the progress arrow).
*   Dynamic arrow textures that indicate the input complexity.
