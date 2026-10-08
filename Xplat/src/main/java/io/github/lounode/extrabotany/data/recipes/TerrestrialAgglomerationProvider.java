package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.common.item.BotaniaItems;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class TerrestrialAgglomerationProvider extends ExtraBotanyRecipeProvider {
	public TerrestrialAgglomerationProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public void buildRecipes(RecipeOutputAdapter consumer) {
		consumer.accept(new FinishedRecipe(idFor("aerialite_ingot"), 1_000_000 / 2,
				new ItemStack(ExtraBotanyItems.aerialite),
				Ingredient.of(BotaniaItems.ENDER_AIR_BOTTLE),
				Ingredient.of(BotaniaItems.DRAGONSTONE),
				Ingredient.of(Items.PHANTOM_MEMBRANE)));
		consumer.accept(new FinishedRecipe(idFor("the_universe"), 1_000_000,
				new ItemStack(ExtraBotanyItems.theUniverse),
				Ingredient.of(ExtraBotanyItems.theChaos),
				Ingredient.of(ExtraBotanyItems.theOrigin),
				Ingredient.of(ExtraBotanyItems.theEnd)
		));
		consumer.accept(new FinishedRecipe(idFor(ExtraBotanyItems.rheinHammer), 1_000_000 * 4,
				new ItemStack(ExtraBotanyItems.rheinHammer),
				Ingredient.of(ExtraBotanyItems.manasteelHammer),
				Ingredient.of(ExtraBotanyItems.elementiumHammer),
				Ingredient.of(ExtraBotanyItems.terrasteelHammer),
				Ingredient.of(ExtraBotanyItems.gaiaHammer),
				Ingredient.of(ExtraBotanyItems.photoniumHammer),
				Ingredient.of(ExtraBotanyItems.shadowiumHammer),
				Ingredient.of(ExtraBotanyItems.aerialiteHammer),
				Ingredient.of(ExtraBotanyItems.orichalcosHammer),
				Ingredient.of(ExtraBotanyItems.dasRheingold),
				Ingredient.of(ExtraBotanyItems.theUniverse)
		));

	}

	private static ResourceLocation idFor(String s) {
		return prefix("terra_plate/" + s);
	}

	private static ResourceLocation idFor(ItemLike itemLike) {
		return prefix("terra_plate/" + BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath());
	}

	protected static class FinishedRecipe implements GeneratedRecipe {
		private final ResourceLocation id;
		private final int mana;
		private final ItemStack output;
		private final Ingredient[] inputs;

		public FinishedRecipe(ResourceLocation id, int mana, ItemStack output, Ingredient... inputs) {
			this.id = id;
			this.mana = mana;
			this.output = output;
			this.inputs = inputs;
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public net.minecraft.world.item.crafting.Recipe<?> recipe() {
			return new vazkii.botania.common.crafting.TerrestrialAgglomerationRecipe(mana, output, inputs);
		}
	}
}
