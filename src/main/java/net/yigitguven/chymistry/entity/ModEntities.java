package net.yigitguven.chymistry.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yigitguven.chymistry.Chymistry;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
        DeferredRegister.create(Registries.ENTITY_TYPE, Chymistry.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownExplosiveLiquid>> THROWN_EXPLOSIVE_LIQUID = 
        ENTITY_TYPES.register("thrown_explosive_liquid", () ->
            EntityType.Builder.<ThrownExplosiveLiquid>of(ThrownExplosiveLiquid::new, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .clientTrackingRange(4)
                .updateInterval(10)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Chymistry.MODID, "thrown_explosive_liquid")))
        );

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownDynamite>> THROWN_DYNAMITE = 
        ENTITY_TYPES.register("thrown_dynamite", () ->
            EntityType.Builder.<ThrownDynamite>of(ThrownDynamite::new, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .clientTrackingRange(4)
                .updateInterval(10)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Chymistry.MODID, "thrown_dynamite")))
        );
}
