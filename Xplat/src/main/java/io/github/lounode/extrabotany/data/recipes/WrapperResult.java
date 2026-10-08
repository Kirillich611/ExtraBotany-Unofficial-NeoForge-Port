package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.conditions.ICondition;

import vazkii.botania.common.crafting.recipe.WrappingRecipeSerializer;

import io.github.lounode.extrabotany.common.crafting.recipe.*;

final class WrapperResult {
	private WrapperResult() {}

	static RecipeOutput ofType(RecipeSerializer<?> serializer, RecipeOutput output) {
		return new RecipeOutput() {
			@Override
			public Advancement.Builder advancement() {
				return output.advancement();
			}

			@Override
			public void accept(ResourceLocation id, Recipe<?> recipe, AdvancementHolder advancement, ICondition... conditions) {
				Recipe<?> wrapped;
				if (serializer instanceof WrappingRecipeSerializer<?> wrapper) {
					wrapped = wrapper.wrap(recipe);
				} else if (serializer == CopyBrewFormFlaskRecipe.SERIALIZER) {
					wrapped = new CopyBrewFormFlaskRecipe((ShapelessRecipe) recipe);
				} else if (serializer == CopyBrewFromManaCocktailRecipe.SERIALIZER) {
					wrapped = new CopyBrewFromManaCocktailRecipe((ShapelessRecipe) recipe);
				} else if (serializer == WandOfTheForestExtendRecipe.SERIALIZER) {
					wrapped = new WandOfTheForestExtendRecipe(CraftingBookCategory.MISC);
				} else {
					throw new IllegalArgumentException("Unsupported recipe wrapper: " + serializer);
				}
				output.accept(id, wrapped, advancement, conditions);
			}
		};
	}
}
