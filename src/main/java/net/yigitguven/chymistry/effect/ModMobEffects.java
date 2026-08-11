package net.yigitguven.chymistry.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yigitguven.chymistry.Chymistry;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Chymistry.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> VITRIOL_IMMUNITY = MOB_EFFECTS.register("vitriol_immunity",
            () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x8B008B) {});
}
