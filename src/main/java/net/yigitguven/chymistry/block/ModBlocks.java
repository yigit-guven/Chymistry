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
    public static final DeferredBlock<Block> PLACED_REINFORCED_BOTTLE = BLOCKS.registerBlock("placed_reinforced_bottle", properties -> new PlacedBottleBlock(properties, net.yigitguven.chymistry.item.ModItems.REINFORCED_GLASS_BOTTLE), () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.GLASS).noOcclusion().strength(3.0F, 1200.0F).sound(net.minecraft.world.level.block.SoundType.GLASS));

    public static final DeferredBlock<Block> ALEMBIC = BLOCKS.registerBlock("alembic", AlembicBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.IRON_BLOCK).noOcclusion().strength(1.5F).sound(net.minecraft.world.level.block.SoundType.COPPER).lightLevel(state -> state.hasProperty(AlembicBlock.LIT) && state.getValue(AlembicBlock.LIT) && state.hasProperty(AlembicBlock.HALF) && state.getValue(AlembicBlock.HALF) == net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER ? 13 : 0));
    
    public static final DeferredBlock<Block> REINFORCED_GLASS = BLOCKS.registerBlock("reinforced_glass", net.minecraft.world.level.block.TransparentBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.GLASS).strength(3.0F, 1200.0F));
    public static final DeferredBlock<Block> PLASTIC_BLOCK = BLOCKS.registerBlock("plastic_block", Block::new, () -> BlockBehaviour.Properties.of().strength(1.5F, 1200.0F).sound(net.minecraft.world.level.block.SoundType.WOOD));
    public static final DeferredBlock<Block> REPELLENT_BASE = BLOCKS.registerBlock("repellent_base", Block::new, () -> BlockBehaviour.Properties.of().strength(1.5F).sound(net.minecraft.world.level.block.SoundType.NETHER_WART));
    public static final DeferredBlock<Block> CYAN_DYE_COMPOSTER = BLOCKS.registerBlock("cyan_dye_composter", CyanDyeComposterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.COMPOSTER));
    public static final DeferredBlock<Block> BLAST_PROOF_CEMENT = BLOCKS.registerBlock("blast_proof_cement",
        BlastProofCementBlock::new,
        () -> BlockBehaviour.Properties.of()
            .strength(3.5F, 1200.0F)
            .sound(net.minecraft.world.level.block.SoundType.STONE)
            .requiresCorrectToolForDrops());

    public static final DeferredBlock<Block> PHOSPHORUS_TORCH = BLOCKS.registerBlock("phosphorus_torch",
        PhosphorusTorchBlock::new,
        () -> BlockBehaviour.Properties.of()
            .noCollision()
            .instabreak()
            .lightLevel(state -> state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED) && state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED) ? 15 : 8)
            .sound(net.minecraft.world.level.block.SoundType.WOOD)
            .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY));

    public static final DeferredBlock<Block> PHOSPHORUS_WALL_TORCH = BLOCKS.registerBlock("phosphorus_wall_torch",
        PhosphorusWallTorchBlock::new,
        () -> BlockBehaviour.Properties.of()
            .noCollision()
            .instabreak()
            .lightLevel(state -> state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED) && state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED) ? 15 : 8)
            .sound(net.minecraft.world.level.block.SoundType.WOOD)
            .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY)
            .overrideDescription("block.chymistry.phosphorus_torch"));
}
