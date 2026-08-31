package net.yigitguven.chymistry.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AlembicMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    public final BlockEntity blockEntity;
    public final net.minecraft.world.level.Level level;

    public AlembicMenu(int containerId, Inventory playerInventory, net.minecraft.network.RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, new SimpleContainer(6), new SimpleContainerData(5));
    }

    public AlembicMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(6), new SimpleContainerData(5));
    }

    public AlembicMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        this(containerId, playerInventory, null, container, data);
    }

    public AlembicMenu(int containerId, Inventory playerInventory, BlockEntity blockEntity, Container container, ContainerData data) {
        super(ModMenus.ALEMBIC_MENU.get(), containerId);
        this.container = container;
        this.data = data;
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();

        checkContainerSize(container, 6);
        checkContainerDataCount(data, 5);

        // Input Slot 1
        this.addSlot(new Slot(container, 0, 26, 25));
        // Input Slot 2
        this.addSlot(new Slot(container, 1, 44, 25));
        // Input Slot 3
        this.addSlot(new Slot(container, 2, 26, 43));
        // Input Slot 4
        this.addSlot(new Slot(container, 3, 44, 43));
        // Fuel Input Slot
        this.addSlot(new Slot(container, 4, 89, 54) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return AlembicMenu.this.level.fuelValues().burnDuration(stack) > 0;
            }
        });
        // Output Slot
        this.addSlot(new Slot(container, 5, 135, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        this.addDataSlots(data);
    }

    public int getProgress() {
        return this.data.get(0);
    }

    public int getMaxProgress() {
        return this.data.get(1);
    }

    public int getFuelTime() {
        return this.data.get(2);
    }

    public int getMaxFuelTime() {
        return this.data.get(3);
    }

    public boolean hasConnectedBottle() {
        return this.data.get(4) == 1;
    }


    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            // 0-5 are Alembic slots (6 slots total)
            if (index < 6) {
                if (!this.moveItemStackTo(itemstack1, 6, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // If it's fuel, try to put it in the fuel slot first
                if (this.level.fuelValues().burnDuration(itemstack1) > 0) {
                    if (!this.moveItemStackTo(itemstack1, 4, 5, false)) {
                        // Fallback to inputs
                        if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                            if (index < 33) {
                                if (!this.moveItemStackTo(itemstack1, 33, this.slots.size(), false)) {
                                    return ItemStack.EMPTY;
                                }
                            } else if (!this.moveItemStackTo(itemstack1, 6, 33, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                }
                // Try putting into input slots (0, 1, 2, 3)
                else if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                    if (index < 33) {
                        if (!this.moveItemStackTo(itemstack1, 33, this.slots.size(), false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 6, 33, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
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
}
