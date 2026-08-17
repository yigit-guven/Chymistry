# Incineration

Incineration is the process of destroying blocks with fire to yield specific item drops, defined by the `chymistry:burning` recipe type.

## Process

When a block in the world is destroyed naturally by fire, the mod's custom `checkBurnOut` logic verifies if the block is a valid input for a `chymistry:burning` recipe. If a match is found, there is a chance (defined by the recipe) that the block will drop the recipe's specified output item as an entity.

## Entity Optimization

To mitigate server performance issues caused by mass block burning, item entities generated via this process utilize custom lifecycle logic:
*   **Despawn Rate:** The dropped items despawn after exactly **30 seconds** (600 ticks), significantly faster than the vanilla 5-minute timer. This value is configurable via `chymistry-server.toml` under `ash_despawn_ticks`.
*   **Proximity Checks:** The fire will only drop the items if a player is detected within a 64-block radius at the time the block burns out. If no player is present, the output is discarded. This can be toggled via the `require_player_for_ash` config option.
*   **Invulnerability:** Items dropped by this process are marked as invulnerable so they are not immediately destroyed by the fire that generated them.
