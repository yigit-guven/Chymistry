package net.yigitguven.chymistry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;

import java.util.function.Supplier;

import java.util.Set;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Chymistry.MODID);

    public static final Supplier<BlockEntityType<MortarBlockEntity>> MORTAR_BE =
            BLOCK_ENTITIES.register("mortar", () -> new BlockEntityType<>(MortarBlockEntity::new, Set.of(ModBlocks.MORTAR.get())));
}
