package io.github.lounode.extrabotany.common.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredients;

public final class StateIngredientHelper {
	private StateIngredientHelper() {}

	public static StateIngredient of(Block block) {
		return StateIngredients.of(block);
	}

	public static StateIngredient of(BlockState state) {
		return StateIngredients.of(state);
	}

	public static StateIngredient of(TagKey<Block> tag) {
		return StateIngredients.of(tag);
	}

	public static StateIngredient of(ResourceLocation tag) {
		return of(TagKey.create(Registries.BLOCK, tag));
	}

	public static StateIngredient combine(StateIngredient... ingredients) {
		return StateIngredients.anyOf(ingredients);
	}

	public static StateIngredient deserialize(JsonElement json) {
		return StateIngredients.TYPED_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
	}

	public static StateIngredient tryDeserialize(JsonElement json) {
		return StateIngredients.TYPED_CODEC.parse(JsonOps.INSTANCE, json).result().orElse(null);
	}

	public static StateIngredient read(FriendlyByteBuf buffer) {
		return StateIngredients.TYPED_STREAM_CODEC.decode((RegistryFriendlyByteBuf) buffer);
	}

	public static JsonElement serialize(StateIngredient ingredient) {
		return StateIngredients.TYPED_CODEC.encodeStart(JsonOps.INSTANCE, ingredient).getOrThrow();
	}
}
