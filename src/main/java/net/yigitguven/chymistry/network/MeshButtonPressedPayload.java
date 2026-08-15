package net.yigitguven.chymistry.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.yigitguven.chymistry.Chymistry;

public record MeshButtonPressedPayload() implements CustomPacketPayload {
    public static final Type<MeshButtonPressedPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Chymistry.MODID, "mesh_button_pressed"));

    public static final StreamCodec<ByteBuf, MeshButtonPressedPayload> STREAM_CODEC = StreamCodec.unit(new MeshButtonPressedPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
