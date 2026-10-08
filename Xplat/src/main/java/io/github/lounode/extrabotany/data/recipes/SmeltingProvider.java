package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

import io.github.lounode.extrabotany.common.block.ExtraBotanyBlocks;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class SmeltingProvider extends ExtraBotanyRecipeProvider {
	public SmeltingProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutputAdapter consumer) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ExtraBotanyBlocks.gaiaQuartzBlock), RecipeCategory.BUILDING_BLOCKS,
				ExtraBotanyBlocks.smoothGaiaQuartz, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ExtraBotanyBlocks.gaiaQuartzBlock))
				.save(consumer, id("smooth_gaia_quartz"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ExtraBotanyBlocks.elementiumQuartzBlock), RecipeCategory.BUILDING_BLOCKS,
				ExtraBotanyBlocks.smoothElementiumQuartz, 0.1f, 200)
				.unlockedBy("has_item", conditionsFromItem(ExtraBotanyBlocks.elementiumQuartzBlock))
				.save(consumer, id("smooth_elementium_quartz"));
	}

	protected static String id(String id) {
		return prefix("smelting/" + id).toString();
	}
}
