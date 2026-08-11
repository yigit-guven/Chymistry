package net.yigitguven.chymistry.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Chymistry.MODID);

    public static final DeferredItem<Item> ASH = ITEMS.registerSimpleItem("ash");
    public static final DeferredItem<Item> NITER_DUST = ITEMS.registerSimpleItem("niter_dust");
    public static final DeferredItem<Item> TINTED_GLASS_BOTTLE = ITEMS.registerSimpleItem("tinted_glass_bottle");
    public static final DeferredItem<Item> ELIXIR_OF_VITRIOL = ITEMS.registerItem("elixir_of_vitriol", properties -> new Item(properties) {
        @Override
        public Component getName(ItemStack pStack) {
            return super.getName(pStack).copy().withStyle(style -> style.withColor(0x8B008B));
        }
    });

    public static final DeferredItem<BlockItem> MORTAR = ITEMS.registerSimpleBlockItem("mortar", ModBlocks.MORTAR);
}
