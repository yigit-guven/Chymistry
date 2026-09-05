# The Crucible

The **Crucible** is a high-temperature metallurgical and biochemical vessel used for alloy smelting, chemical synthesis, and atmospheric rituals.

<div align="center">
  <img src="images/blocks/irontongs_carry_brickcrucible.png" width="96" style="image-rendering: pixelated;">
</div>

## Crucible Variants

Crucibles come in three material tiers, each engineered to withstand distinct thermal limits:

| Variant | Max Heat | Min Cold | Blast Resistance | Notes |
| :--- | :---: | :---: | :---: | :--- |
| **Brick Crucible** | 64 | -64 | Low | Entry-tier crucible for basic alloys, leather, and soap. |
| **Deepslate Crucible** | 256 | -256 | Medium | Mid-tier crucible for solvents and refined metals. |
| **Netherite Crucible** | 999 | -999 | Immune | Extreme-temperature vessel capable of handling extreme reactions. |

---

## Thermal Mechanics

Crucibles adjust their temperature dynamically depending on the heat or cold source placed directly beneath them:

### Heating Sources (Below Block)
* **Lava**: +0.1665 heat/tick (Rapid heating)
* **Campfire / Soul Campfire / Fire / Soul Fire**: +0.08 heat/tick (Steady heating)
* **Magma Block**: +0.04 heat/tick (Gentle heating)

### Cooling Sources (Below Block)
* **Blue Ice**: -0.1665 cold/tick (Rapid freezing)
* **Packed Ice**: -0.08 cold/tick (Steady cooling)
* **Ice / Snow Block**: -0.04 cold/tick (Gentle cooling)

### Normalization
* If no thermal source is present below, the crucible gradually returns towards **0 Heat** at 0.1 heat/tick.
* **Waterlogged Crucibles**: Being submerged in water forcefully normalizes temperature towards 0 at an accelerated rate (0.5 heat/tick).

---

## Manipulation & Inspection

### [Iron Tongs](Iron-Tongs)
Crucibles cannot be safely moved by hand when hot.
* **Picking Up**: Right-click a Crucible with **Iron Tongs** to lift it. The crucible's contents, temperature, and progress are stored inside the tongs.
* **Portable GUI**: Right-click while holding the Crucible in tongs to open its container interface on the go.
* **Placing Down**: Crouch (Shift) + Right-click a surface to place the crucible back down.

### [Thermometer](Thermometer)
* Right-click any crucible with a Thermometer to read its exact heat level.
* Holding a Thermometer displays a real-time thermal HUD overlay on your screen.

---

## Overheat Hazards

Recipes require heat to fall strictly within their designated `[minHeat, maxHeat]` operating window.

> [!WARNING]
> If a Crucible exceeds the maximum heat of an active volatile recipe, it begins to smoke. If overheated for more than 40 continuous ticks (2 seconds), it triggers an **Overheat Hazard**:
> * **Toxic Cloud**: Discharges a lingering cloud of Poison and Nausea, destroying uncompleted inputs.
> * **Explosion**: Catastrophically detonates, destroying surrounding blocks and evaporating ingredients.

---

## Recipes

| Product | Inputs | Temp Range | Time | Notes |
| :--- | :--- | :---: | :---: | :--- |
| **[Soap](Soap)** x1 | 1x [Animal Fat](Animal-Fat) + 1x [Quicklime](Quicklime) + 1x Water Bucket | 10 to 60 | 5s (100t) | Returns empty bucket |
| **Leather** x1 | 1x Rotten Flesh + 1x [Ash](Ash) | 15 to 80 | 5s (100t) | Smelt/tan rotten flesh |
| **[Brass Ingot](Brass-Ingot)** x2 | 1x [Copper Dust](Copper-Dust) + 1x Raw Iron | 20 to 120 | 10s (200t) | Alloy synthesis |
| **[Steel Ingot](Steel-Ingot)** x1 | 1x Iron Ingot + 1x Coal + 1x [Ash](Ash) | 35 to 200 | 15s (300t) | Carbon steel forging |
| **[Silver Sludge](Silver-Sludge)** x1 | 1x Raw Iron + 1x [Nitric Acid](Nitric-Acid) | 20 to 100 | 10s (200t) | Returns Reinforced Bottle |
| **[Silver Ingot](Silver-Ingot)** x1 | 1x [Silver Sludge](Silver-Sludge) + 1x [Ash](Ash) | 30 to 150 | 10s (200t) | Smeltable precipitate |
| **[Purified Gold Dust](Purified-Gold-Dust)** x2 | 1x Raw Gold + 1x [Gold Solvent](Gold-Solvent) | 20 to 1500 | 10s (200t) | Highly purified dust |
| **[Gold Solvent](Gold-Solvent)** x1 | 1x [Nitric Acid](Nitric-Acid) + 1x [Hydrochloric Acid](Hydrochloric-Acid) + 1x Reinforced Bottle | 20 to 80 | 10s (200t) | Aqua-regia compound |
| **[Plastic Pellets](Plastic-Pellets)** x2 | 1x Coal + 1x [Sulfuric Acid](Sulfuric-Acid) | 25 to 120 | 10s (200t) | Returns Reinforced Bottle |
| **[Reinforced Glass](Reinforced-Glass)** x1 | 1x Glass + 1x [Iron Dust](Iron-Dust) | 30 to 180 | 10s (200t) | Blast-resistant glass |
| **[Blast-Proof Cement](Blast-Proof-Cement)** x2 | 1x [Quicklime](Quicklime) + 1x Sand + 1x Gravel + 1x Water Bucket | 15 to 90 | 10s (200t) | Returns empty bucket |
| **[Elixir of Vitriol](Elixir-of-Vitriol)** x1 | 1x [Ash](Ash) + 1x [Niter Dust](Niter-Dust) + 1x [Tinted Glass Bottle](Tinted-Glass-Bottle) | 15 to 60 | 8s (160t) | Volatile elixir |

---

## Special Rituals

* **[Storm Ritual](Storm-Ritual)**: Placing **[Storm Powder](Storm-Powder)** and **[Creosote Oil](Creosote-Oil)** into a Crucible under specific hot/cold ambient conditions triggers a local thunderstorm.
* **[Repellent Base](Repellent-Base)**: Placing a Repellent Base inside a Crucible heated above 20 Heat slowly vaporizes the base (consuming durability), generating a monster-spawn suppression field across a 32-block radius.
