package net.yigitguven.chymistry.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.yigitguven.chymistry.Chymistry;

public class ModNetworking {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Chymistry.MODID);
        registrar.playToServer(
                MeshButtonPressedPayload.TYPE,
                MeshButtonPressedPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        if (context.player().containerMenu instanceof net.yigitguven.chymistry.menu.MortarMenu menu) {
                            menu.handleMeshPress();
                        }
                    });
                }
        );

        registrar.playToServer(
                OpenCrucibleUIPayload.TYPE,
                OpenCrucibleUIPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        net.minecraft.world.entity.player.Player player = context.player();
                        net.minecraft.world.item.ItemStack mainHand = player.getMainHandItem();
                        net.minecraft.world.item.ItemStack offHand = player.getOffhandItem();
                        net.minecraft.world.item.ItemStack tongsTemp = net.minecraft.world.item.ItemStack.EMPTY;

                        if (mainHand.is(net.yigitguven.chymistry.item.ModItems.IRON_TONGS.get())) {
                            tongsTemp = mainHand;
                        } else if (offHand.is(net.yigitguven.chymistry.item.ModItems.IRON_TONGS.get())) {
                            tongsTemp = offHand;
                        }
                        final net.minecraft.world.item.ItemStack tongs = tongsTemp;

                        if (!tongs.isEmpty()) {
                            net.minecraft.world.item.component.CustomData customData = tongs.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
                            net.minecraft.nbt.CompoundTag tag = customData.copyTag();

                            if (tag.contains("CrucibleType")) {
                                player.openMenu(new net.minecraft.world.MenuProvider() {
                                    @Override
                                    public net.minecraft.network.chat.Component getDisplayName() {
                                        return net.minecraft.network.chat.Component.translatable("container.chymistry.crucible");
                                    }

                                    @Override
                                    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int pContainerId, net.minecraft.world.entity.player.Inventory pInventory, net.minecraft.world.entity.player.Player pPlayer) {
                                        net.minecraft.nbt.CompoundTag crucibleData = tag.getCompound("CrucibleData").orElse(new net.minecraft.nbt.CompoundTag());
                                        crucibleData.putString("id", "chymistry:crucible");
                                        
                                        net.minecraft.world.level.block.state.BlockState dummyState = net.minecraft.core.registries.BuiltInRegistries.BLOCK.get(net.minecraft.resources.Identifier.parse(tag.getString("CrucibleType").orElse(""))).get().value().defaultBlockState();
                                        net.minecraft.world.level.block.entity.BlockEntity dummyBe = net.minecraft.world.level.block.entity.BlockEntity.loadStatic(net.minecraft.core.BlockPos.ZERO, dummyState, crucibleData, player.level().registryAccess());
                                        
                                        net.yigitguven.chymistry.block.CrucibleBlockEntity dummyCrucible;
                                        if (dummyBe instanceof net.yigitguven.chymistry.block.CrucibleBlockEntity) {
                                            dummyCrucible = (net.yigitguven.chymistry.block.CrucibleBlockEntity) dummyBe;
                                        } else {
                                            dummyCrucible = new net.yigitguven.chymistry.block.CrucibleBlockEntity(net.minecraft.core.BlockPos.ZERO, dummyState);
                                        }

                                        boolean[] initializing = {true};
                                        net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(6) {
                                            @Override
                                            public void setChanged() {
                                                super.setChanged();
                                                if (initializing[0]) return;
                                                net.minecraft.world.item.ItemStack currentTongs = pPlayer.getItemInHand(pPlayer.getUsedItemHand());
                                                net.minecraft.nbt.CompoundTag latestTag = currentTongs.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                                                net.minecraft.nbt.CompoundTag crucibleData = latestTag.getCompound("CrucibleData").orElse(new net.minecraft.nbt.CompoundTag());
                                                crucibleData.putString("id", "chymistry:crucible");
                                                
                                                net.minecraft.world.level.block.entity.BlockEntity dummyBeLatest = net.minecraft.world.level.block.entity.BlockEntity.loadStatic(net.minecraft.core.BlockPos.ZERO, dummyState, crucibleData, pPlayer.level().registryAccess());
                                                if (dummyBeLatest instanceof net.yigitguven.chymistry.block.CrucibleBlockEntity dummyCrucibleLatest) {
                                                    for (int i = 0; i < 6; i++) {
                                                        dummyCrucibleLatest.inventory.setItem(i, this.getItem(i));
                                                    }
                                                    net.minecraft.nbt.CompoundTag updatedData = dummyCrucibleLatest.saveCustomOnly(pPlayer.level().registryAccess());
                                                    latestTag.put("CrucibleData", updatedData);
                                                    currentTongs.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(latestTag));
                                                }
                                            }
                                        };
                                        
                                        for (int i = 0; i < 6; i++) {
                                            inventory.setItem(i, dummyCrucible.inventory.getItem(i).copy());
                                        }
                                        initializing[0] = false;

                                        net.minecraft.world.inventory.ContainerData data = new net.minecraft.world.inventory.ContainerData() {
                                            @Override
                                            public int get(int pIndex) {
                                                net.minecraft.world.item.ItemStack currentTongs = pPlayer.getItemInHand(pPlayer.getUsedItemHand());
                                                net.minecraft.nbt.CompoundTag latestTag = currentTongs.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                                                
                                                return switch (pIndex) {
                                                    case 0 -> latestTag.getCompound("CrucibleData").flatMap(t -> java.util.Optional.of(t.getInt("progress").orElse(0))).orElse(0);
                                                    case 1 -> latestTag.getCompound("CrucibleData").flatMap(t -> java.util.Optional.of(t.getInt("maxProgress").orElse(0))).orElse(0);
                                                    case 2 -> (int) (latestTag.getFloat("CrucibleHeat").orElse(0.0f) * 10);
                                                    case 3 -> {
                                                        if (dummyState.getBlock() instanceof net.yigitguven.chymistry.block.CrucibleBlock crucible) {
                                                            yield crucible.getMaxHeat() * 10;
                                                        }
                                                        yield 1000;
                                                    }
                                                    case 4 -> {
                                                        if (dummyState.getBlock() instanceof net.yigitguven.chymistry.block.CrucibleBlock crucible) {
                                                            yield crucible.getMinHeat() * 10;
                                                        }
                                                        yield -1000;
                                                    }
                                                    default -> 0;
                                                };
                                            }

                                            @Override
                                            public void set(int pIndex, int pValue) {
                                                net.minecraft.nbt.CompoundTag currentTag = tongs.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                                                net.minecraft.nbt.CompoundTag crucibleData = currentTag.getCompound("CrucibleData").orElse(new net.minecraft.nbt.CompoundTag());
                                                switch (pIndex) {
                                                    case 0 -> crucibleData.putInt("progress", pValue);
                                                    case 1 -> crucibleData.putInt("maxProgress", pValue);
                                                }
                                                currentTag.put("CrucibleData", crucibleData);
                                                tongs.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(currentTag));
                                            }

                                            @Override
                                            public int getCount() {
                                                return 5;
                                            }
                                        };

                                        return new net.yigitguven.chymistry.menu.CrucibleMenu(pContainerId, pInventory, inventory, data, net.minecraft.world.inventory.ContainerLevelAccess.NULL) {
                                            @Override
                                            public void broadcastChanges() {
                                                net.minecraft.world.item.ItemStack currentTongs = pPlayer.getItemInHand(pPlayer.getUsedItemHand());
                                                net.minecraft.nbt.CompoundTag latestTag = currentTongs.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                                                net.minecraft.nbt.CompoundTag crucibleData = latestTag.getCompound("CrucibleData").orElse(new net.minecraft.nbt.CompoundTag());
                                                
                                                net.minecraft.core.NonNullList<net.minecraft.world.item.ItemStack> items = net.minecraft.core.NonNullList.withSize(6, net.minecraft.world.item.ItemStack.EMPTY);
                                                crucibleData.putString("id", "chymistry:crucible");
                                                net.minecraft.world.level.block.entity.BlockEntity dummyBe2 = net.minecraft.world.level.block.entity.BlockEntity.loadStatic(net.minecraft.core.BlockPos.ZERO, dummyState, crucibleData, player.level().registryAccess());
                                                if (dummyBe2 instanceof net.yigitguven.chymistry.block.CrucibleBlockEntity dummyCrucible2) {
                                                    for (int i = 0; i < 6; i++) {
                                                        items.set(i, dummyCrucible2.inventory.getItem(i).copy());
                                                    }
                                                }
                                                
                                                initializing[0] = true;
                                                for(int i = 0; i < 6; i++) {
                                                    inventory.setItem(i, items.get(i));
                                                }
                                                initializing[0] = false;
                                                
                                                super.broadcastChanges();
                                            }
                                        };
                                    }
                                });
                            }
                        }
                    });
                }
        );
    }
}
