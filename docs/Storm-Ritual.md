# Storm Ritual

The **Storm Ritual** is an atmospheric alchemical process that exploits thermal imbalance to condense local moisture and summon precipitation.

<div align="center">
  <img src="https://raw.githubusercontent.com/yigit-guven/Chymistry/26.2-neoforge/src/main/resources/assets/chymistry/textures/item/storm_powder.png" width="48" style="image-rendering: pixelated;">
</div>

## Mechanics

To activate the ritual, place **1x [Storm Powder](Storm-Powder)** and **1x [Creosote Oil](Creosote-Oil)** into any **[Crucible](Crucible)**.

The crucible must achieve an inverted thermal state contrasting with the surrounding biome's ambient climate:

| Biome Climate | Ambient Condition | Required Crucible Temperature | Thermal Challenge |
| :--- | :--- | :---: | :--- |
| **Arid / Hot** (Desert, Badlands, Nether) | Biome Temp > 0.95 | **Cold: Heat ≤ -15.0** (Resting on Blue Ice) | Hot ambient air rapidly drains cold! |
| **Frigid / Cold** (Snowy Plains, Taiga, Peaks) | Biome Temp < 0.25 | **Hot: Heat ≥ 15.0** (Resting on Lava / Fire) | Freezing ambient air rapidly drains heat! |
| **Temperate** (Plains, Forest, Ocean) | 0.25 ≤ Biome Temp ≤ 0.95 | **Heat ≥ 15.0 OR Heat ≤ -15.0** | Moderate thermal consumption. |

---

## Ritual Effects

Once the thermal threshold is satisfied:
1. **Activation**: A lightning rumble and thunderclap echo across the area as storm cloud particles erupt from the crucible. The Storm Powder is consumed, and Creosote Oil reverts to an empty Glass Bottle.
2. **Precipitation**: For 60 seconds (1200 ticks), a localized downpour envelopes the crucible within a **14-block radius**:
   * **Fire Suppression**: Automatically extinguishes any entities on fire within 14 blocks.
   * **Water-Sensitive Entity Damage**: Inflicts continuous damage to water-sensitive monsters (such as Endermen and Blazes).
   * **Visuals**: Dense low-altitude clouds and water vapor billow continuously.
