package net.yigitguven.chymistry.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.Property;
import net.yigitguven.chymistry.Chymistry;
import net.yigitguven.chymistry.jei.SoapJeiRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SoapCleaningHelper {

    private static final Set<String> DYED_FAMILIES = Set.of(
        "wool",
        "carpet",
        "terracotta",
        "glazed_terracotta",
        "concrete",
        "concrete_powder",
        "stained_glass",
        "stained_glass_pane",
        "candle",
        "candle_cake",
        "bed",
        "shulker_box",
        "banner",
        "wall_banner",
        "bundle",
        "match"
    );

    public static Block getWhiteBlock(Block block) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        if (id == null) return null;
        String path = id.getPath();

        for (DyeColor color : DyeColor.values()) {
            if (color == DyeColor.WHITE) continue;
            String prefix = color.getName() + "_";
            if (path.startsWith(prefix)) {
                String suffix = path.substring(prefix.length());
                if (DYED_FAMILIES.contains(suffix)) {
                    Identifier whiteId = Identifier.fromNamespaceAndPath(id.getNamespace(), "white_" + suffix);
                    Block whiteBlock = BuiltInRegistries.BLOCK.getValue(whiteId);
                    if (whiteBlock != null && whiteBlock != Blocks.AIR) {
                        return whiteBlock;
                    }
                }
            }
        }
        return null;
    }

    public static Item getWhiteItem(Item item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null) return null;
        String path = id.getPath();

        for (DyeColor color : DyeColor.values()) {
            if (color == DyeColor.WHITE) continue;
            String prefix = color.getName() + "_";
            if (path.startsWith(prefix)) {
                String suffix = path.substring(prefix.length());
                if (DYED_FAMILIES.contains(suffix)) {
                    Identifier whiteId = Identifier.fromNamespaceAndPath(id.getNamespace(), "white_" + suffix);
                    Item whiteItem = BuiltInRegistries.ITEM.getValue(whiteId);
                    if (whiteItem != null && whiteItem != Items.AIR) {
                        return whiteItem;
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static BlockState copyProperties(BlockState from, BlockState to) {
        BlockState result = to;
        for (Property prop : from.getProperties()) {
            if (result.hasProperty(prop)) {
                result = result.setValue(prop, from.getValue(prop));
            }
        }
        return result;
    }

    public static boolean cleanBlock(Level level, BlockPos pos, BlockState state, Player player, ItemStack soapStack, InteractionHand hand) {
        Block whiteBlock = getWhiteBlock(state.getBlock());
        if (whiteBlock == null || whiteBlock == state.getBlock()) {
            return false;
        }

        if (!level.isClientSide()) {
            if (state.getBlock() instanceof BedBlock) {
                Direction facing = state.getValue(BedBlock.FACING);
                BedPart part = state.getValue(BedBlock.PART);
                BlockPos otherPos = part == BedPart.FOOT ? pos.relative(facing) : pos.relative(facing.getOpposite());
                BlockState otherState = level.getBlockState(otherPos);

                if (otherState.getBlock() == state.getBlock()) {
                    BlockState newOtherState = copyProperties(otherState, whiteBlock.defaultBlockState());
                    level.setBlock(otherPos, newOtherState, 3);
                }
            }

            CompoundTag tag = null;
            BlockEntity oldBe = level.getBlockEntity(pos);
            if (oldBe instanceof ShulkerBoxBlockEntity || oldBe instanceof BannerBlockEntity) {
                tag = oldBe.saveWithFullMetadata(level.registryAccess());
            }

            BlockState targetState = copyProperties(state, whiteBlock.defaultBlockState());
            level.setBlock(pos, targetState, 3);

            if (tag != null) {
                BlockEntity newBe = level.getBlockEntity(pos);
                if (newBe != null) {
                    newBe.loadWithComponents(net.minecraft.world.level.storage.TagValueInput.create(new net.minecraft.util.ProblemReporter.Collector(), level.registryAccess(), tag));
                }
            }

            level.playSound(null, pos, SoundEvents.SLIME_BLOCK_STEP, SoundSource.BLOCKS, 1.0f, 1.2f);
            level.playSound(null, pos, SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.BLOCKS, 0.6f, 1.5f);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.BUBBLE_POP, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12, 0.3, 0.3, 0.3, 0.05);
                serverLevel.sendParticles(ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12, 0.3, 0.3, 0.3, 0.05);
            }

            if (player != null && !player.getAbilities().instabuild) {
                soapStack.hurtAndBreak(1, player, hand);
            }
        }

        return true;
    }

    public static boolean canCleanItem(ItemStack input) {
        if (input.isEmpty()) {
            return false;
        }
        if (input.has(DataComponents.DYED_COLOR)) {
            return true;
        }
        Item white = getWhiteItem(input.getItem());
        return white != null && white != input.getItem();
    }

    public static ItemStack cleanItem(ItemStack input) {
        if (input.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (input.has(DataComponents.DYED_COLOR)) {
            ItemStack clean = input.copyWithCount(1);
            clean.remove(DataComponents.DYED_COLOR);
            return clean;
        }

        Item whiteItem = getWhiteItem(input.getItem());
        if (whiteItem != null && whiteItem != input.getItem()) {
            ItemStack result = new ItemStack(whiteItem, 1);
            result.applyComponents(input.getComponentsPatch());
            return result;
        }

        return ItemStack.EMPTY;
    }

    public static List<SoapJeiRecipe> getJeiRecipes() {
        List<SoapJeiRecipe> recipes = new ArrayList<>();
        for (String family : DYED_FAMILIES) {
            if (family.equals("candle_cake") || family.equals("wall_banner")) continue;
            for (DyeColor color : DyeColor.values()) {
                if (color == DyeColor.WHITE) continue;
                String namespace = family.equals("match") ? Chymistry.MODID : "minecraft";
                Identifier dyedId = Identifier.fromNamespaceAndPath(namespace, color.getName() + "_" + family);
                Identifier whiteId = Identifier.fromNamespaceAndPath(namespace, "white_" + family);

                Item dyedItem = BuiltInRegistries.ITEM.getValue(dyedId);
                Item whiteItem = BuiltInRegistries.ITEM.getValue(whiteId);

                if (dyedItem != null && whiteItem != null && dyedItem != Items.AIR && whiteItem != Items.AIR) {
                    recipes.add(new SoapJeiRecipe(new ItemStack(dyedItem), new ItemStack(whiteItem)));
                }
            }
        }

        ItemStack dyedChest = new ItemStack(Items.LEATHER_CHESTPLATE);
        dyedChest.set(DataComponents.DYED_COLOR, new net.minecraft.world.item.component.DyedItemColor(0xFF0000));
        recipes.add(new SoapJeiRecipe(dyedChest, new ItemStack(Items.LEATHER_CHESTPLATE)));

        return recipes;
    }
}
