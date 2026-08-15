package net.yigitguven.chymistry.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.yigitguven.chymistry.block.ModBlocks;
import net.yigitguven.chymistry.recipe.ModRecipes;
import net.yigitguven.chymistry.recipe.MortarRecipe;
import net.yigitguven.chymistry.recipe.MortarRecipeInput;

import java.util.Optional;

public class MortarMenu extends AbstractContainerMenu {
    private final SimpleContainer container;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final Player player;
    
    private int currentPresses = 0;
    private int maxPresses = 0;
    private long lastClickTime = 0;

    public MortarMenu(int pContainerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(pContainerId, inv, ContainerLevelAccess.NULL);
    }

    public MortarMenu(int pContainerId, Inventory inv) {
        this(pContainerId, inv, ContainerLevelAccess.NULL);
    }

    public MortarMenu(int pContainerId, Inventory inv, ContainerLevelAccess access) {
        super(ModMenus.MORTAR_MENU.get(), pContainerId);
        this.access = access;
        this.player = inv.player;
        this.container = new SimpleContainer(4);
        
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> MortarMenu.this.currentPresses;
                    case 1 -> MortarMenu.this.maxPresses;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> MortarMenu.this.currentPresses = pValue;
                    case 1 -> MortarMenu.this.maxPresses = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        this.addSlot(new Slot(container, 0, 26, 17));
        this.addSlot(new Slot(container, 1, 26, 35));
        this.addSlot(new Slot(container, 2, 26, 53));
        this.addSlot(new Slot(container, 3, 95, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }

    public void handleMeshPress() {
        access.execute((level, pos) -> {
            if (level.isClientSide()) return;

            long currentTime = level.getGameTime();
            long timeSinceLastClick = this.lastClickTime > 0 ? (currentTime - this.lastClickTime) : -1;
            this.lastClickTime = currentTime;

            MortarRecipeInput input = new MortarRecipeInput(container.getItem(0), container.getItem(1), container.getItem(2));
            Optional<RecipeHolder<MortarRecipe>> recipe = ((net.minecraft.server.level.ServerLevel)level).recipeAccess().getRecipeFor(ModRecipes.MORTAR_TYPE.get(), input, level);

            if (container.getItem(0).isEmpty() && container.getItem(1).isEmpty() && container.getItem(2).isEmpty()) {
                this.maxPresses = 0;
                this.currentPresses = 0;
            } else if (recipe.isEmpty()) {
                this.maxPresses = -1; // -1 indicates error
                this.currentPresses = 0;
            } else {
                MortarRecipe activeRecipe = recipe.get().value();
                boolean timingValid = true;

                if (this.currentPresses > 0 && timeSinceLastClick >= 0) {
                    switch (activeRecipe.clickType()) {
                        case FAST:
                            if (timeSinceLastClick > 10) timingValid = false;
                            break;
                        case SLOW:
                            if (timeSinceLastClick < 20) timingValid = false;
                            break;
                        case ANY:
                        default:
                            break;
                    }
                }

                if (!timingValid) {
                    if (activeRecipe.output().create().is(net.minecraft.world.item.Items.GUNPOWDER)) {
                        int gunpowderCount = container.getItem(3).is(net.minecraft.world.item.Items.GUNPOWDER) ? container.getItem(3).getCount() : 0;
                        float radius = 1.0f + (gunpowderCount / 64.0f) * 3.0f;
                        
                        level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, radius, Level.ExplosionInteraction.BLOCK);
                        
                        container.clearContent();
                        this.currentPresses = 0;
                        this.maxPresses = 0;
                    }
                    return;
                }

                this.maxPresses = activeRecipe.presses();
                this.currentPresses++;
                
                level.playSound(null, pos, SoundEvents.GRAVEL_STEP, SoundSource.BLOCKS, 1.0f, 1.0f);

                if (this.currentPresses >= this.maxPresses) {
                    ItemStack result = activeRecipe.assemble(input);
                    ItemStack outputSlot = container.getItem(3);

                    if (outputSlot.isEmpty() || (ItemStack.isSameItemSameComponents(outputSlot, result) && outputSlot.getCount() + result.getCount() <= container.getMaxStackSize())) {
                        for (int i = 0; i < activeRecipe.inputs().size(); i++) {
                            container.getItem(i).shrink(activeRecipe.inputs().get(i).count());
                        }
                        if (outputSlot.isEmpty()) {
                            container.setItem(3, result.copy());
                        } else {
                            outputSlot.grow(result.getCount());
                        }
                        this.currentPresses = 0;
                        
                        if (container.getItem(0).isEmpty() && container.getItem(1).isEmpty() && container.getItem(2).isEmpty()) {
                            this.maxPresses = 0;
                        }
                    } else {
                        this.currentPresses--;
                    }
                }
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (pIndex < 4) {
                if (!this.moveItemStackTo(itemstack1, 4, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
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
        return stillValid(this.access, pPlayer, ModBlocks.MORTAR.get());
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
        int progressArrowSize = 50; // New texture width

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.clearContainer(pPlayer, this.container);
    }
}
