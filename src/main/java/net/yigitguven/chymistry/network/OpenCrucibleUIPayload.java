package net.yigitguven.chymistry.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.yigitguven.chymistry.Chymistry;

public record OpenCrucibleUIPayload() implements CustomPacketPayload {
    public static final Type<OpenCrucibleUIPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Chymistry.MODID, "open_crucible_ui"));

    public static final StreamCodec<ByteBuf, OpenCrucibleUIPayload> STREAM_CODEC = StreamCodec.unit(new OpenCrucibleUIPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
