package net.yigitguven.chymistry.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.Container;

public class CrucibleMenu extends AbstractContainerMenu {
    private final Container crucible;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public CrucibleMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(6), new SimpleContainerData(5), ContainerLevelAccess.NULL);
    }

    public CrucibleMenu(int pContainerId, Inventory pPlayerInventory, Container pContainer, ContainerData pData, ContainerLevelAccess pAccess) {
        super(ModMenus.CRUCIBLE_MENU.get(), pContainerId);
        checkContainerSize(pContainer, 6);
        checkContainerDataCount(pData, 5);
        this.crucible = pContainer;
        this.data = pData;
        this.access = pAccess;

        // Add 5 input slots
        this.addSlot(new Slot(pContainer, 0, 20, 19));
        this.addSlot(new Slot(pContainer, 1, 56, 19));
        this.addSlot(new Slot(pContainer, 2, 20, 53));
        this.addSlot(new Slot(pContainer, 3, 56, 53));
        this.addSlot(new Slot(pContainer, 4, 97, 53));

        // Add 1 output slot
        this.addSlot(new Slot(pContainer, 5, 139, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // Player Inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Player Hotbar
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(pData);
    }

    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((level, pos) -> {
            return level.getBlockState(pos).getBlock() instanceof net.yigitguven.chymistry.block.CrucibleBlock
                    && player.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
        }, true);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            if (index < 6) {
                if (!this.moveItemStackTo(itemstack1, 6, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 5, false)) {
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

    public int getProgress() {
        return this.data.get(0);
    }

    public int getMaxProgress() {
        return this.data.get(1);
    }

    public float getCurrentHeat() {
        return this.data.get(2) / 10.0f;
    }

    public float getMaxHeat() {
        return this.data.get(3) / 10.0f;
    }

    public float getMinHeat() {
        return this.data.get(4) / 10.0f;
    }
}
