package io.github.lounode.extrabotany.common.bossevents;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public final class ComponentCodec {
	private ComponentCodec() {}

	public static final Codec<Component> CODEC = ComponentSerialization.CODEC;

	public static Component fromNetwork(RegistryFriendlyByteBuf buffer) {
		return ComponentSerialization.STREAM_CODEC.decode(buffer);
	}

	public static void toNetwork(RegistryFriendlyByteBuf buffer, Component component) {
		ComponentSerialization.STREAM_CODEC.encode(buffer, component);
	}
}
