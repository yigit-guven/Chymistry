package net.yigitguven.chymistry.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.yigitguven.chymistry.entity.ThrownExplosiveLiquid;

public class ExplosiveLiquidItem extends Item implements ProjectileItem {

    public ExplosiveLiquidItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(
                ThrownExplosiveLiquid::new,
                serverLevel, stack, player, 0.0F, 1.5F, 1.0F
            );
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        stack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new ThrownExplosiveLiquid(level, pos.x(), pos.y(), pos.z(), stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, net.minecraft.world.item.component.TooltipDisplay pTooltipDisplay, java.util.function.Consumer<net.minecraft.network.chat.Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
        pTooltipComponents.accept(net.minecraft.network.chat.Component.translatable("tooltip.chymistry.explosive_liquid.desc").withStyle(net.minecraft.ChatFormatting.GRAY));
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
    }
}
