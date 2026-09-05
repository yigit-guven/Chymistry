# Changelog

All notable changes to the **Chymistry** project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2026-09-05

First official full release of Chymistry, concluding the alpha development cycle.

### Added
- **Alembic Distillation System**:
  - Functional Alembic workstation block, block entity, interactive container menu, and GUI screen.
  - Dynamic adjacent bottle connection and condensation mechanics supporting Glass Bottles, Tinted Bottles, and Reinforced Bottles.
  - Dynamic visual effects including boiling bubble particles, fluid fill animations, and synchronized lit furnace states.
  - Complete JEI recipe category integration with animated condensation arrows.
  - Distillation recipes for Berry Essence, Creosote Oil, Hydrochloric Acid, Nitric Acid, Sulfuric Acid, Gold Solvent, Alcohol, Phosphorus, and Disinfectant.
- **Cauldron Chemistry & Composting**:
  - Cauldron chemistry recipe system and JEI category for fluid-consuming transformations.
  - Soap item with Soap Bleaching mechanics to restore dyed blocks and items to white.
  - Super Fertilizer item and high-efficiency composter recipe.
  - Cyan Dye Composter block and specialized composting workflows.
- **Advanced Materials & Construction**:
  - Treated Wood crafting workflow and fireproofing properties.
  - Blast-Proof Cement block with complete explosion immunity, Wither resistance, and the ability to defuse primed TNT and Creepers.
  - Reinforced Glass block and placeable Reinforced Glass Bottles.
  - Plastic Blocks and Plastic Pellets with bottle remainder preservation.
  - Creosote Oil usable as high-efficiency furnace fuel.
- **Alchemical Brews & Status Effects**:
  - Potion of Immunity and Immunity status effect preventing all negative and external status effects.
  - Vigor Potion and Vigor status effect regenerating maximum health over time.
  - Defoliant Potion for clearing foliage, leaves, and vegetation in an expansive 20x20x20 area.
  - Disinfectant item capable of curing Poison and initiating Zombie Villager conversion.
  - Repellent Base block and Spawn Repellent effect.
- **Weapons, Tools & Utilities**:
  - Delayed-fuse Dynamite with physics bouncing and timed detonation.
  - Thrown Liquid Explosive with instantaneous impact detonation.
  - Freezing Powder item for instant water-to-Blue-Ice and lava-to-Obsidian solidification with JEI conversion support.
  - Storm Powder Crucible ritual for summoning localized rain and thunderstorms based on ambient temperature.
  - 16 colored match variants featuring mobile dynamic hand-held lighting when struck.
  - Waterloggable Phosphorus Torches and Wall Torches.
- **Metallurgy & Crucible Processing**:
  - Brass Ingot smelting, Purified Gold Dust refining, and Crucible leather tanning recipes.
  - Crucible overheat hazard mechanics and herb tags.
  - Support for multi-count ingredient stacks in Crucible recipes.

### Changed
- Overhauled and redesigned JEI Crucible recipe layout with animated heat icons, temperature range indicators, and interactive tooltips.
- Synchronized Iron Tongs portable Crucible UI with real-time block and item entity data.
- Improved container item returns and bottle preservation across all distillation and chemical recipes.
- Target Java toolchain updated to Java 25.

### Removed
- Removed the early Alpha warning popup screen (`AlphaWarningScreen`) displayed upon entering worlds.

---

## [1.0.2-alpha] - 2026-08-29

### Added
- Thermometer item and HUD overlay to inspect real-time temperature of Crucibles and heat sources.
- Overhauled Crucible mechanics with configurable minimum and maximum heat limits.
- Fully functional Iron Tongs with targeting logic, environmental heat calculation, and background inventory ticking.
- Portable Crucible UI accessible directly through Iron Tongs with real-time data synchronization.
- Crucible custom crafting recipe system and JEI support.
- 16 new crafting materials and refreshed item textures.
- Sea Salt smelting recipe from Sea Water Bucket.
- Enhanced tooltips for Tongs, Crucibles, and Thermometer.

---

## [1.0.1-alpha] - 2026-08-19

### Added
- Iron Tongs with custom two-handed holding and swinging animations.
- Crucible block variants (Brick, Deepslate, Netherite).
- Rust Powder item.
- Niter Soil Composter block workflow and JEI category.

### Fixed
- Mortar state reset, button packet dispatching, and output stack limits.
- Mortar JEI recipe category width alignment.
- CI workflows, wiki documentation, and issue templates.

---

## [1.0.0-alpha] - 2026-08-01

### Added
- Initial Alpha release for Chymistry.
- Foundational early-chemistry workstations, item dusts, mortar mechanics, and basic alchemical processing.
