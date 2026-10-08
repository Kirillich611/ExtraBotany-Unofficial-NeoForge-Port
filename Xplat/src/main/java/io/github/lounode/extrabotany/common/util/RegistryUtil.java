package io.github.lounode.extrabotany.common.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class RegistryUtil {
	private RegistryUtil() {}

	public static Item getItemOrThrow(ResourceLocation id) {
		return BuiltInRegistries.ITEM.getOptional(id).orElseThrow(() -> new IllegalArgumentException("Unknown item " + id));
	}

	public static Block getBlockOrThrow(ResourceLocation id) {
		return BuiltInRegistries.BLOCK.getOptional(id).orElseThrow(() -> new IllegalArgumentException("Unknown block " + id));
	}
}
