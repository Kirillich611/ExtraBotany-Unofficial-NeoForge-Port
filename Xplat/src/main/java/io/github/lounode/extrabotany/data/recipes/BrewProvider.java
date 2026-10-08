package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.brew.Brew;

import io.github.lounode.extrabotany.common.brew.ExtraBotanyBrews;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class BrewProvider extends ExtraBotanyRecipeProvider {
	public BrewProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public void buildRecipes(RecipeOutputAdapter consumer) {
		consumer.accept(new FinishedRecipe(idFor("revolution"), ExtraBotanyBrews.revolution,
				Ingredient.of(Items.NETHER_WART),
				Ingredient.of(Items.IRON_PICKAXE),
				Ingredient.of(Items.SUGAR)));
		consumer.accept(new FinishedRecipe(idFor("deadpool"), ExtraBotanyBrews.deadpool,
				Ingredient.of(Items.NETHER_WART),
				Ingredient.of(Items.ROTTEN_FLESH),
				Ingredient.of(Items.BONE),
				Ingredient.of(Items.BLAZE_POWDER)));

		consumer.accept(new FinishedRecipe(idFor("shield"), ExtraBotanyBrews.shield,
				Ingredient.of(Items.NETHER_WART),
				Ingredient.of(Items.GOLDEN_APPLE),
				Ingredient.of(Items.BUCKET),
				Ingredient.of(Blocks.OBSIDIAN)));

		consumer.accept(new FinishedRecipe(idFor("floating"), ExtraBotanyBrews.floating,
				Ingredient.of(Items.NETHER_WART),
				Ingredient.of(Items.CHORUS_FRUIT),
				Ingredient.of(Items.SUGAR)));

		consumer.accept(new FinishedRecipe(idFor("all_in_one"), ExtraBotanyBrews.allInOne,
				Ingredient.of(Items.NETHER_WART),
				Ingredient.of(Items.GOLDEN_CARROT),
				Ingredient.of(Items.GHAST_TEAR),
				Ingredient.of(Items.GLOWSTONE_DUST)));
	}

	private static ResourceLocation idFor(String s) {
		return prefix("brew/" + s);
	}

	protected static class FinishedRecipe implements GeneratedRecipe {
		private final ResourceLocation id;
		private final Brew brew;
		private final Ingredient[] inputs;

		public FinishedRecipe(ResourceLocation id, Brew brew, Ingredient... inputs) {
			this.id = id;
			this.brew = brew;
			this.inputs = inputs;
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public net.minecraft.world.item.crafting.Recipe<?> recipe() {
			return new vazkii.botania.common.crafting.BotanicalBreweryRecipe(brew, inputs);
		}
	}
}
