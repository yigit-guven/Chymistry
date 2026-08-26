package net.yigitguven.chymistry.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    @Inject(method = "burn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private static void onBurnShrink(NonNullList<ItemStack> inventory, ItemStack ingredient, ItemStack output, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        ItemStack itemstack = inventory.get(0);
        if (itemstack.getCount() == 1 && itemstack.getItem().getCraftingRemainder() != null) {
            inventory.set(0, itemstack.getItem().getCraftingRemainder().create());
        }
    }
}
