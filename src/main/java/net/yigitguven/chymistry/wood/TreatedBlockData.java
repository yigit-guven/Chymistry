package net.yigitguven.chymistry.wood;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.yigitguven.chymistry.Chymistry;

import java.util.HashSet;
import java.util.Set;

public class TreatedBlockData extends SavedData {
    public static final Codec<TreatedBlockData> CODEC = Codec.LONG.listOf().xmap(
            longList -> {
                TreatedBlockData data = new TreatedBlockData();
                for (Long l : longList) {
                    data.treatedPositions.add(BlockPos.of(l));
                }
                return data;
            },
            data -> data.treatedPositions.stream().map(BlockPos::asLong).toList()
    );

    public static final SavedDataType<TreatedBlockData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(Chymistry.MODID, "treated_blocks"),
            TreatedBlockData::new,
            CODEC,
            null
    );

    private final Set<BlockPos> treatedPositions = new HashSet<>();

    public TreatedBlockData() {
    }

    public static TreatedBlockData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public static boolean isTreated(ServerLevel level, BlockPos pos) {
        return get(level).contains(pos);
    }

    public boolean contains(BlockPos pos) {
        return treatedPositions.contains(pos);
    }

    public void add(BlockPos pos) {
        if (treatedPositions.add(pos.immutable())) {
            setDirty();
        }
    }

    public void remove(BlockPos pos) {
        if (treatedPositions.remove(pos)) {
            setDirty();
        }
    }
}
