package net.yigitguven.chymistry.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ChymistryServerConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue REQUIRE_PLAYER_FOR_ASH;
    public static final ModConfigSpec.IntValue ASH_DESPAWN_TICKS;

    static {
        BUILDER.push("Optimizations");

        REQUIRE_PLAYER_FOR_ASH = BUILDER
                .comment("Whether a player needs to be within 64 blocks of a fire for ash to drop.",
                         "Setting this to true prevents massive natural forest fires from generating lag.",
                         "If you have a remote chunk-loaded auto farm, you may want to set this to false.")
                .define("requirePlayerForAsh", true);

        ASH_DESPAWN_TICKS = BUILDER
                .comment("How many ticks (20 ticks = 1 second) before ash dropped by fire despawns.",
                         "Default is 600 ticks (30 seconds). Vanilla items default to 6000 ticks (5 minutes).",
                         "Lowering this prevents massive lag from forest fires.")
                .defineInRange("ashDespawnTicks", 600, 1, 6000);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
