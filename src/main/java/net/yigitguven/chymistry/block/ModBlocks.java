package net.yigitguven.chymistry.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Chymistry.MODID);

    public static final DeferredBlock<Block> MORTAR = BLOCKS.registerBlock("mortar", MortarBlock::new, () -> BlockBehaviour.Properties.of().noOcclusion());
    
    public static final DeferredBlock<Block> QUICKLIME = BLOCKS.registerBlock("quicklime", Block::new, () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.STONE).strength(1.2f, 6.0f).requiresCorrectToolForDrops());
    public static final DeferredBlock<Block> QUICKLIME_STAIRS = BLOCKS.registerBlock("quicklime_stairs", properties -> new net.minecraft.world.level.block.StairBlock(QUICKLIME.get().defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.STONE).strength(1.2f, 6.0f).requiresCorrectToolForDrops());
    public static final DeferredBlock<Block> QUICKLIME_SLAB = BLOCKS.registerBlock("quicklime_slab", net.minecraft.world.level.block.SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.STONE).strength(1.2f, 6.0f).requiresCorrectToolForDrops());
    public static final DeferredBlock<Block> QUICKLIME_WALL = BLOCKS.registerBlock("quicklime_wall", net.minecraft.world.level.block.WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.STONE).strength(1.2f, 6.0f).requiresCorrectToolForDrops());
}
