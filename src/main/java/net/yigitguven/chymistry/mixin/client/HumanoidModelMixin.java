package net.yigitguven.chymistry.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.yigitguven.chymistry.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    public void chymistry$setupAnimTail(HumanoidRenderState state, CallbackInfo ci) {
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        ItemStack mainHandItem = state.mainArm == HumanoidArm.RIGHT ? state.rightHandItemStack : state.leftHandItemStack;
        
        if (mainHandItem != null && mainHandItem.is(ModTags.Items.TWO_HANDED)) {
            // Fixed base position
            float fixedXRot = -0.5f;
            
            // Add punch animation if attacking
            if (state.attackTime > 0.0F) {
                // Mth.sin(attackTime * PI) goes 0 -> 1 -> 0
                // We use Math.sin so we don't have to import Mth, or we can just cast to float
                float swingProgress = (float) Math.sin(state.attackTime * Math.PI);
                // "slightly go up and down" -> -0.4f moves the arms up during the swing
                fixedXRot -= swingProgress * 0.4f;
            }
            
            model.leftArm.xRot = fixedXRot;
            model.rightArm.xRot = fixedXRot;
            
            model.leftArm.yRot = 0.1f;
            model.rightArm.yRot = -0.1f;
            
            model.leftArm.zRot = 0.0f;
            model.rightArm.zRot = 0.0f;
        }
    }
}
