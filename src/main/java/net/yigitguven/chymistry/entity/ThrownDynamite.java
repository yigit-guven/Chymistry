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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.yigitguven.chymistry.item.ModItems;

public class ThrownDynamite extends ThrowableItemProjectile {

    private int fuse = 50;

    public ThrownDynamite(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownDynamite(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.THROWN_DYNAMITE.get(), shooter, level, stack);
    }

    public ThrownDynamite(Level level, double x, double y, double z, ItemStack stack) {
        super(ModEntities.THROWN_DYNAMITE.get(), x, y, z, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DYNAMITE.get();
    }

    @Override
    public void tick() {
        super.tick();

        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.15, this.getZ(), 2, 0.05, 0.05, 0.05, 0.01);
            serverLevel.sendParticles(ParticleTypes.SMALL_FLAME, this.getX(), this.getY() + 0.15, this.getZ(), 1, 0.02, 0.02, 0.02, 0.01);
        }

        if (this.fuse % 10 == 0) {
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TNT_PRIMED, SoundSource.NEUTRAL, 0.5F, 1.4F);
        }

        if (!level.isClientSide()) {
            this.fuse--;
            if (this.fuse <= 0) {
                level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 4.0F, Level.ExplosionInteraction.BLOCK);
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, -0.2, 0.3));
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WOOD_STEP, SoundSource.NEUTRAL, 0.6F, 1.2F);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.3, 0.1, -0.3));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Fuse", this.fuse);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.fuse = input.getIntOr("Fuse", 50);
    }
}
