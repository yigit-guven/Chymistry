package net.yigitguven.chymistry.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.wood.TreatedWoodHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements IItemExtension {

    @Override
    public String getCreatorModId(HolderLookup.Provider registries, ItemStack itemStack) {
        if (TreatedWoodHelper.isTreated(itemStack)) {
            return Chymistry.MODID;
        }
        return CommonHooks.getDefaultCreatorModId(registries, itemStack);
    }
}
