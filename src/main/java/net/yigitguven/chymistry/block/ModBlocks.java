package net.yigitguven.chymistry.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Chymistry.MODID);

    public static final DeferredBlock<Block> MORTAR = BLOCKS.register("mortar", () -> new Block(BlockBehaviour.Properties.of()));
}
