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

    public static final DeferredBlock<Block> BRICK_CRUCIBLE = BLOCKS.registerBlock("brick_crucible", properties -> new CrucibleBlock(properties, 64, -64), () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.BRICKS).noOcclusion());
    public static final DeferredBlock<Block> DEEPSLATE_CRUCIBLE = BLOCKS.registerBlock("deepslate_crucible", properties -> new CrucibleBlock(properties, 256, -256), () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.DEEPSLATE_BRICKS).noOcclusion());
    public static final DeferredBlock<Block> NETHERITE_CRUCIBLE = BLOCKS.registerBlock("netherite_crucible", properties -> new CrucibleBlock(properties, 999, -999), () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.NETHERITE_BLOCK).noOcclusion());

    public static final DeferredBlock<Block> NITER_SOIL_COMPOSTER = BLOCKS.registerBlock("niter_soil_composter", NiterSoilComposterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.COMPOSTER));
    
    public static final DeferredBlock<Block> PLACED_BOTTLE = BLOCKS.registerBlock("placed_bottle", properties -> new PlacedBottleBlock(properties, () -> net.minecraft.world.item.Items.GLASS_BOTTLE), () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.GLASS).noOcclusion().strength(0.3F).sound(net.minecraft.world.level.block.SoundType.GLASS));
    public static final DeferredBlock<Block> PLACED_TINTED_BOTTLE = BLOCKS.registerBlock("placed_tinted_bottle", properties -> new PlacedBottleBlock(properties, net.yigitguven.chymistry.item.ModItems.TINTED_GLASS_BOTTLE), () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.GLASS).noOcclusion().strength(0.3F).sound(net.minecraft.world.level.block.SoundType.GLASS));

    public static final DeferredBlock<Block> ALEMBIC = BLOCKS.registerBlock("alembic", AlembicBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.IRON_BLOCK).noOcclusion().strength(1.5F).sound(net.minecraft.world.level.block.SoundType.COPPER).lightLevel(state -> state.hasProperty(AlembicBlock.LIT) && state.getValue(AlembicBlock.LIT) && state.hasProperty(AlembicBlock.HALF) && state.getValue(AlembicBlock.HALF) == net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER ? 13 : 0));

}
