# Dynamite

**Dynamite** is a throwable delayed-fuse demolition projectile.

<div align="center">
  <img src="https://raw.githubusercontent.com/yigit-guven/Chymistry/26.2-neoforge/src/main/resources/assets/chymistry/textures/item/dynamite.png" width="32" style="image-rendering: pixelated;">
</div>

## Mechanics

1. **Throwing**:
   * Stacks up to 16 in inventory.
   * **Right-Click** to ignite the fuse with a sizzle sound and throw the dynamite stick forward.
2. **Physics & Fuse**:
   * Features physical bouncing when colliding with walls, ground, or entities.
   * Emits trailing spark particles and smoke during flight.
   * Detonates after a **2.5-second fuse** (50 ticks).
3. **Explosion**:
   * Creates a blast with block damage and entity knockback, ideal for mining and combat.

---

## Crafting

Dynamite is crafted shapelessly using **[Liquid Explosive](Explosive-Liquid)**:

| Station | Inputs | Output |
| :---: | :--- | :--- |
| **Crafting Table** | 1x String + 1x Any Sand + 1x [Liquid Explosive](Explosive-Liquid) | **2x Dynamite** |
