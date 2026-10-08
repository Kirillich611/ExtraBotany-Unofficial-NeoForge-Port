package io.github.lounode.extrabotany.network;

import net.minecraft.resources.ResourceLocation;

public interface ExtrabotanyPacket extends net.minecraft.network.protocol.common.custom.CustomPacketPayload {
	@Override
	default Type<ExtrabotanyPacket> type() {
		return new Type<>(getFabricId());
	}

	static <T extends ExtrabotanyPacket> net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, T> codec(
			java.util.function.Function<net.minecraft.network.RegistryFriendlyByteBuf, T> decoder) {
		return net.minecraft.network.codec.StreamCodec.of((buffer, packet) -> packet.encode(buffer), decoder::apply);
	}

	void encode(net.minecraft.network.RegistryFriendlyByteBuf buf);

	ResourceLocation getFabricId();
}
