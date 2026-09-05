package net.yigitguven.chymistry.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.yigitguven.chymistry.item.ModItems;

public class ThrownExplosiveLiquid extends ThrowableItemProjectile {

    public ThrownExplosiveLiquid(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownExplosiveLiquid(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.THROWN_EXPLOSIVE_LIQUID.get(), shooter, level, stack);
    }

    public ThrownExplosiveLiquid(Level level, double x, double y, double z, ItemStack stack) {
        super(ModEntities.THROWN_EXPLOSIVE_LIQUID.get(), x, y, z, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.EXPLOSIVE_LIQUID.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        Level level = this.level();
        if (!level.isClientSide()) {
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 1.0F, 1.2F);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 10, 0.2, 0.2, 0.2, 0.05);
            }
            level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.BLOCK);
            this.discard();
        }
    }
}
