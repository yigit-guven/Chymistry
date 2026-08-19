package net.yigitguven.chymistry.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.yigitguven.chymistry.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(method = "applyItemArmAttackTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V", at = @At("HEAD"), cancellable = true)
    private void chymistry$onApplyItemArmAttackTransform(PoseStack poseStack, HumanoidArm hand, float swingProgress, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        
        ItemStack itemStack = hand == player.getMainArm() ? player.getMainHandItem() : player.getOffhandItem();
        
        if (itemStack != null && itemStack.is(ModTags.Items.TWO_HANDED)) {
            ci.cancel();
            
            if (swingProgress > 0.0F) {
                float f = (float) Math.sin(swingProgress * Math.PI);
                // Pitch down (degree change)
                poseStack.mulPose(Axis.XP.rotationDegrees(f * 25.0f));
                // Instead of twisting the item (which causes the back to swing out),
                // we simply translate the whole item slightly to the right to visually counter the leftward arc
                poseStack.translate(f * 0.08f, 0.0, 0.0);
            }
        }
    }
}
