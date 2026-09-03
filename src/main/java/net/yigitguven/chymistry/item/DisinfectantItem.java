package net.yigitguven.chymistry.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Consumer;

public class DisinfectantItem extends Item {

    public DisinfectantItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return super.getName(pStack).copy().withStyle(style -> style.withColor(0xF0C830));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide() && pLivingEntity instanceof Player player) {
            if (player.hasEffect(MobEffects.POISON)) {
                player.removeEffect(MobEffects.POISON);
            }
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof ZombieVillager zombieVillager) {
            if (!zombieVillager.isConverting()) {
                Level level = target.level();
                if (!level.isClientSide()) {
                    int conversionTime = zombieVillager.getRandom().nextInt(2401) + 3600;
                    startConverting(zombieVillager, player.getUUID(), conversionTime);

                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                        ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                        if (stack.isEmpty()) {
                            player.setItemInHand(hand, bottle);
                        } else if (!player.getInventory().add(bottle)) {
                            player.drop(bottle, false);
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    private static void startConverting(ZombieVillager zombieVillager, UUID starter, int conversionTime) {
        try {
            Method method = ZombieVillager.class.getDeclaredMethod("startConverting", UUID.class, int.class);
            method.setAccessible(true);
            method.invoke(zombieVillager, starter, conversionTime);
        } catch (Exception e) {
            for (Method m : ZombieVillager.class.getDeclaredMethods()) {
                if (m.getParameterCount() == 2 && m.getParameterTypes()[0] == UUID.class && m.getParameterTypes()[1] == int.class) {
                    try {
                        m.setAccessible(true);
                        m.invoke(zombieVillager, starter, conversionTime);
                        return;
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.accept(Component.empty());
        pTooltipComponents.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.disinfectant.cure_poison").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.accept(Component.empty());
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.disinfectant.when_used").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.accept(Component.translatable("tooltip.chymistry.disinfectant.cure_zombie").withStyle(ChatFormatting.GOLD));
    }
}
