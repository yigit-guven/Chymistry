package net.yigitguven.chymistry;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.yigitguven.chymistry.block.ModBlocks;
import net.yigitguven.chymistry.item.ModItems;
import net.yigitguven.chymistry.item.ModCreativeModeTabs;
import net.yigitguven.chymistry.block.ModBlockEntities;
import net.yigitguven.chymistry.menu.ModMenus;
import net.yigitguven.chymistry.recipe.ModRecipes;
import net.yigitguven.chymistry.network.ModNetworking;
import net.yigitguven.chymistry.config.ChymistryServerConfig;

@Mod(Chymistry.MODID)
public class Chymistry {
    public static final String MODID = "chymistry";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Chymistry(IEventBus modEventBus, ModContainer modContainer) {

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        ModRecipes.TYPES.register(modEventBus);
        net.yigitguven.chymistry.effect.ModMobEffects.MOB_EFFECTS.register(modEventBus);

        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, ChymistryServerConfig.SPEC);

        modEventBus.addListener(ModNetworking::register);
    }
}
