package net.yigitguven.chymistry.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.yigitguven.chymistry.block.MortarBlockEntity;
import net.yigitguven.chymistry.block.ModBlocks;

public class MortarMenu extends AbstractContainerMenu {
    public final MortarBlockEntity blockEntity;
    private final ContainerData data;

    public MortarMenu(int pContainerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public MortarMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenus.MORTAR_MENU.get(), pContainerId);
        blockEntity = (MortarBlockEntity) entity;
        this.data = data;

        this.addSlot(new Slot(blockEntity, 0, 44, 35));
        this.addSlot(new Slot(blockEntity, 1, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        
        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (pIndex < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(net.minecraft.world.inventory.ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                pPlayer, ModBlocks.MORTAR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 21;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public boolean hasError() {
        return this.data.get(1) == -1; 
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.clearContainer(pPlayer, this.blockEntity);
    }
}
