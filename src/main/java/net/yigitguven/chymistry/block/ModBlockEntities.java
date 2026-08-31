package net.yigitguven.chymistry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<net.minecraft.world.level.block.entity.BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE, Chymistry.MODID);

    public static final java.util.function.Supplier<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("crucible", () -> new BlockEntityType<>(
                    CrucibleBlockEntity::new,
                    java.util.Set.of(ModBlocks.BRICK_CRUCIBLE.get(), ModBlocks.DEEPSLATE_CRUCIBLE.get(), ModBlocks.NETHERITE_CRUCIBLE.get())
            ));

    public static final java.util.function.Supplier<BlockEntityType<AlembicBlockEntity>> ALEMBIC =
            BLOCK_ENTITIES.register("alembic", () -> new BlockEntityType<>(
                    AlembicBlockEntity::new,
                    java.util.Set.of(ModBlocks.ALEMBIC.get())
            ));

    public static final java.util.function.Supplier<BlockEntityType<PlacedBottleBlockEntity>> PLACED_BOTTLE_BE =
            BLOCK_ENTITIES.register("placed_bottle", () -> new BlockEntityType<>(
                    PlacedBottleBlockEntity::new,
                    java.util.Set.of(ModBlocks.PLACED_BOTTLE.get(), ModBlocks.PLACED_TINTED_BOTTLE.get())
            ));
}
