package net.yigitguven.chymistry.mixin;

import mezz.jei.gui.ingredients.IListElement;
import mezz.jei.gui.ingredients.ListElementInfo;
import net.minecraft.world.item.ItemStack;
import net.yigitguven.chymistry.wood.TreatedWoodHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ListElementInfo.class, remap = false)
public abstract class ListElementInfoMixin<V> {

    @Shadow
    @Final
    private IListElement<V> element;

    @Inject(method = "getModNameForSorting", at = @At("HEAD"), cancellable = true)
    private void chymistry$getModNameForSorting(CallbackInfoReturnable<String> cir) {
        Object ingredient = this.element.getTypedIngredient().getIngredient();
        if (ingredient instanceof ItemStack stack && TreatedWoodHelper.isTreated(stack)) {
            cir.setReturnValue("Chymistry_Treated");
        }
    }
}
