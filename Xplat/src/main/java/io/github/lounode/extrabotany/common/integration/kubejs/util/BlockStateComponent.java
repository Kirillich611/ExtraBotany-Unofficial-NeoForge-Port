package io.github.lounode.extrabotany.common.integration.kubejs.util;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;

import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.rhino.type.TypeInfo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredients;

import io.github.lounode.extrabotany.common.util.StateIngredientHelper;

public record BlockStateComponent(RecipeComponentType<?> type) implements RecipeComponent<StateIngredient> {
	public static final RecipeComponentType<StateIngredient> TYPE = RecipeComponentType.unit(
			ResourceLocation.fromNamespaceAndPath("extrabotany", "state_ingredient"), BlockStateComponent::new);
	public static final RecipeComponent<StateIngredient> INPUT = TYPE.instance();
	public static final RecipeComponent<StateIngredient> OUTPUT = TYPE.instance();
	public static final RecipeComponent<StateIngredient> BLOCK = TYPE.instance();

	@Override
	public Codec<StateIngredient> codec() {
		return StateIngredients.TYPED_CODEC;
	}

	@Override
	public TypeInfo typeInfo() {
		return TypeInfo.of(StateIngredient.class);
	}

	@Override
	public StateIngredient wrap(RecipeScriptContext context, Object from) {
		if (from instanceof StateIngredient ingredient) {
			return ingredient;
		}
		if (from instanceof Block block) {
			return StateIngredients.of(block);
		}
		if (from instanceof BlockState state) {
			return StateIngredients.of(state);
		}
		if (from instanceof JsonObject json) {
			return StateIngredientHelper.deserialize(json);
		}
		if (from instanceof CharSequence chars && chars.toString().startsWith("#")) {
			return StateIngredientHelper.of(ResourceLocation.parse(chars.toString().substring(1)));
		}
		return StateIngredients.of(dev.latvian.mods.kubejs.recipe.component.BlockStateComponent.BLOCK.instance().wrap(context, from));
	}

	@Override
	public boolean isEmpty(StateIngredient ingredient) {
		return ingredient.getDisplayed().isEmpty();
	}
}
