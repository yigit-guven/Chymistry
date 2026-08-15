package net.yigitguven.chymistry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.yigitguven.chymistry.menu.MortarMenu;

public class MortarBlock extends Block {
    public static final MapCodec<MortarBlock> CODEC = simpleCodec(MortarBlock::new);

    public MortarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    private static final Component CONTAINER_TITLE = Component.translatable("block.chymistry.mortar");

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inventory, p) -> new MortarMenu(id, inventory, net.minecraft.world.inventory.ContainerLevelAccess.create(level, pos)),
                    CONTAINER_TITLE
            ));
        }
        return InteractionResult.SUCCESS;
    }
}
