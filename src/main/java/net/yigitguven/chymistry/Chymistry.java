package net.yigitguven.chymistry;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(Chymistry.MODID)
public class Chymistry {
    public static final String MODID = "chymistry";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Chymistry(IEventBus modEventBus, ModContainer modContainer) {
        // Initialization code goes here
    }
}
