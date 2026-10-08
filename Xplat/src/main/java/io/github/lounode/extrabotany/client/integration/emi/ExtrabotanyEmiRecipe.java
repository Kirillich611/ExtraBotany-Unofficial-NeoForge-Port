package io.github.lounode.extrabotany.client.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;

import vazkii.botania.client.integration.emi.BotaniaEmiRecipe;

public abstract class ExtrabotanyEmiRecipe extends BotaniaEmiRecipe {
	public ExtrabotanyEmiRecipe(EmiRecipeCategory category, net.minecraft.world.item.crafting.RecipeHolder<?> recipe) {
		super(category, recipe);
	}
}
