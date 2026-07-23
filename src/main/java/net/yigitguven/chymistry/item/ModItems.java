package net.yigitguven.chymistry.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Chymistry.MODID);

    public static final DeferredItem<BlockItem> MORTAR = ITEMS.registerSimpleBlockItem("mortar", ModBlocks.MORTAR);
}
