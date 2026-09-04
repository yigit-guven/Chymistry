package net.yigitguven.chymistry.effect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class ImmunityMobEffect extends MobEffect {

    public ImmunityMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity entity, int amplifier) {
        if (entity.getActiveEffects().size() > 1) {
            List<Holder<MobEffect>> toRemove = new ArrayList<>();
            for (MobEffectInstance inst : entity.getActiveEffects()) {
                if (!inst.is(ModMobEffects.IMMUNITY)) {
                    toRemove.add(inst.getEffect());
                }
            }
            for (Holder<MobEffect> effect : toRemove) {
                entity.removeEffect(effect);
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
