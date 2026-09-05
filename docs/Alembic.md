# The Alembic

The **Alembic** is an advanced two-block tall distillation and condensation apparatus utilized for fractional distillation, acid synthesis, and essence extraction.

<div align="center">
  <img src="https://raw.githubusercontent.com/yigit-guven/Chymistry/26.2-neoforge/src/main/resources/assets/chymistry/textures/item/alcohol_bottle.png" width="64" style="image-rendering: pixelated;">
</div>

## Structure & Mechanics

The Alembic occupies a 1x2x1 vertical volume (lower furnace base and upper glass alembic cucurbit).

1. **Fuel Source**: The lower furnace base consumes standard furnace fuel (Coal, Charcoal, [Creosote Oil](Creosote-Oil), etc.) to boil reactants.
2. **Reagents**: Up to 4 reactant items are inserted into the distillation retort via the interactive GUI.
3. **Condensation & Placed Bottles**:
   * Volatile distillates cannot be collected directly inside the inventory.
   * Instead, players must **place a bottle block directly adjacent** to the Alembic on the ground:
     * **Glass Bottle**: For organic and mild distillates (Creosote Oil, Berry Essence, Immunity Potion, Disinfectant).
     * **[Tinted Glass Bottle](Tinted-Glass-Bottle)**: For photo-sensitive or volatile brews (Vigor Potion, Liquid Explosive).
     * **[Reinforced Glass Bottle](Reinforced-Glass-Bottle)**: For highly corrosive mineral acids (Hydrochloric, Nitric, Sulfuric) and pure Alcohol.
4. **Dynamic Connection & Visuals**:
   * The Alembic automatically detects adjacent placed bottles, rotates its delivery spout, and establishes a fluid conduit connection.
   * When actively distilling, the retort boils with animated bubble particles and glowing illumination.
   * As distillation progresses, the placed bottle visually fills with colored fluid.
5. **Collection**: Once condensation finishes, right-click the filled bottle block to collect your bottled chemical or elixir!

---

## Distillation Recipes

| Product | Ingredients | Condenser Bottle Required | Burn Time |
| :--- | :--- | :---: | :---: |
| **[Berry Essence](Berry-Essence)** | 4x Sweet Berries | Glass Bottle | 10s (200t) |
| **[Creosote Oil](Creosote-Oil)** | 1x Coal + 1x Any Log | Glass Bottle | 15s (300t) |
| **[Alcohol Bottle](Alcohol-Bottle)** | 3x Apple + 1x Sugar | [Reinforced Bottle](Reinforced-Glass-Bottle) | 12s (240t) |
| **[Hydrochloric Acid](Hydrochloric-Acid)** | 1x [Sea Salt](Sea-Salt) + 1x [Elixir of Vitriol](Elixir-of-Vitriol) | [Reinforced Bottle](Reinforced-Glass-Bottle) | 10s (200t) |
| **[Nitric Acid](Nitric-Acid)** | 1x [Niter Dust](Niter-Dust) + 1x [Elixir of Vitriol](Elixir-of-Vitriol) | [Reinforced Bottle](Reinforced-Glass-Bottle) | 10s (200t) |
| **[Sulfuric Acid](Sulfuric-Acid)** | 2x [Elixir of Vitriol](Elixir-of-Vitriol) | [Reinforced Bottle](Reinforced-Glass-Bottle) | 10s (200t) |
| **[Liquid Explosive](Explosive-Liquid)** | 1x Gunpowder + 1x [Nitric Acid](Nitric-Acid) + 1x [Sulfuric Acid](Sulfuric-Acid) | [Tinted Bottle](Tinted-Glass-Bottle) | 12s (240t) |
| **[Disinfectant](Disinfectant)** | 1x [Alcohol Bottle](Alcohol-Bottle) + 1x [Quicklime](Quicklime) + 1x [Sea Salt](Sea-Salt) | Glass Bottle | 10s (200t) |
| **[Vigor Potion](Vigor-Potion)** | 1x [Berry Essence](Berry-Essence) + 1x Honey Bottle + 1x Golden Carrot | [Tinted Bottle](Tinted-Glass-Bottle) | 10s (200t) |
| **[Potion of Immunity](Potion-of-Immunity)** | 1x [Elixir of Vitriol](Elixir-of-Vitriol) + 1x Glistering Melon + 1x Golden Apple | Glass Bottle | 15s (300t) |
| **[Defoliant Potion](Defoliant-Potion)** | 1x [Ash](Ash) + 1x [Sulfuric Acid](Sulfuric-Acid) + 1x Poisonous Potato | Glass Bottle | 10s (200t) |
| **[Phosphorus](Phosphorus)** | 4x Bone Meal + 1x Sand | Glass Bottle | 10s (200t) |
| **[Repellent Base](Repellent-Base)** | 1x Rotten Flesh + 1x Spider Eye + 1x [Niter Dust](Niter-Dust) + 1x Gunpowder | None (Solid Output) | 15s (300t) |
