package net.yigitguven.chymistry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Chymistry.MODID);

    public static final Supplier<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("crucible", () ->
                    new BlockEntityType<>(CrucibleBlockEntity::new,
                            ModBlocks.BRICK_CRUCIBLE.get(),
                            ModBlocks.DEEPSLATE_CRUCIBLE.get(),
                            ModBlocks.NETHERITE_CRUCIBLE.get()));

}
