package net.yigitguven.chymistry.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum ClickType implements StringRepresentable {
    FAST("fast"),
    ANY("any"),
    SLOW("slow");

    public static final Codec<ClickType> CODEC = StringRepresentable.fromEnum(ClickType::values);
    public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, ClickType> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.idMapper(id -> values()[id], ClickType::ordinal);
    
    private final String name;

    ClickType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
