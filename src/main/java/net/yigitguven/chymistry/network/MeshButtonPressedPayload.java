package net.yigitguven.chymistry.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.yigitguven.chymistry.Chymistry;

public record MeshButtonPressedPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<MeshButtonPressedPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Chymistry.MODID, "mesh_button_pressed"));

    public static final StreamCodec<FriendlyByteBuf, MeshButtonPressedPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> buf.writeBlockPos(payload.pos()),
            buf -> new MeshButtonPressedPayload(buf.readBlockPos())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
