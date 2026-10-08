package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class OmnivioletProvider extends ExtraBotanyRecipeProvider {

	public OmnivioletProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutputAdapter consumer) {
		consumer.accept(new FinishedRecipe(id("book"), Ingredient.of(Items.BOOK), 50));
		consumer.accept(new FinishedRecipe(id("written_book"), Ingredient.of(Items.WRITTEN_BOOK), 65));
	}

	protected ResourceLocation id(String id) {
		return prefix("omniviolet/" + id);
	}

	protected static class FinishedRecipe implements GeneratedRecipe {
		private final ResourceLocation id;
		private final Ingredient input;
		private final int burnTime;

		public FinishedRecipe(ResourceLocation id, Ingredient input, int burnTime) {
			this.id = id;
			this.input = input;
			this.burnTime = burnTime;
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public net.minecraft.world.item.crafting.Recipe<?> recipe() {
			return new io.github.lounode.extrabotany.common.crafting.OmniVioletsRecipe(input, burnTime);
		}
	}
}
