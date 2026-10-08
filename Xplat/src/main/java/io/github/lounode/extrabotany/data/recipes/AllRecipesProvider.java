package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AllRecipesProvider extends ExtraBotanyRecipeProvider {
	private final List<ExtraBotanyRecipeProvider> providers;

	public AllRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
		providers = List.of(
				new PedestalRecipeProvider(output, registries),
				new CraftingRecipeProvider(output, registries),
				new SmeltingProvider(output, registries),
				new SmithingRecipeProvider(output, registries),
				new ElvenTradeProvider(output, registries),
				new ManaInfusionProvider(output, registries),
				new BrewProvider(output, registries),
				new PetalApothecaryProvider(output, registries),
				new RunicAltarProvider(output, registries),
				new TerrestrialAgglomerationProvider(output, registries),
				new StonesiaProvider(output, registries),
				new EdelweissRecipeProvider(output, registries),
				new OmnivioletProvider(output, registries));
	}

	@Override
	protected void buildRecipes(RecipeOutputAdapter output) {
		providers.forEach(provider -> provider.buildRecipes(output));
	}
}
