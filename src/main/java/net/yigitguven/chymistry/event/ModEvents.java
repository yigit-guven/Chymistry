package net.yigitguven.chymistry.event;

import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.effect.ModMobEffects;

@net.neoforged.fml.common.EventBusSubscriber(modid = Chymistry.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect().equals(MobEffects.POISON) || 
            event.getEffectInstance().getEffect().equals(MobEffects.WITHER)) {
            
            if (event.getEntity().hasEffect(ModMobEffects.VITRIOL_IMMUNITY)) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect().equals(ModMobEffects.VITRIOL_IMMUNITY)) {
            event.getEntity().removeEffect(MobEffects.POISON);
            event.getEntity().removeEffect(MobEffects.WITHER);
        }
    }
}
