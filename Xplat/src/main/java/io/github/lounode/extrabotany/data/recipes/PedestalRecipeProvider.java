package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.Nullable;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.github.lounode.extrabotany.common.lib.ExtraBotanyTags;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class PedestalRecipeProvider extends ExtraBotanyRecipeProvider {
	public PedestalRecipeProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutputAdapter consumer) {
		consumer.accept(new FinishedPedestalRecipe(id("gilded_potato_mashed"),
				new ItemStack(ExtraBotanyItems.gildedPotatoMashed),
				Ingredient.of(ExtraBotanyItems.gildedPotato)
		));
		consumer.accept(new FinishedPedestalRecipe(id("spirit_fragment"),
				new ItemStack(ExtraBotanyItems.spiritFragment),
				Ingredient.of(ExtraBotanyItems.spiritFuel),
				Ingredient.of(ExtraBotanyTags.Items.HAMMERS),
				10,
				5
		));
	}

	protected ResourceLocation id(String s) {
		return prefix("pedestal_smash/" + s);
	}

	protected static class FinishedPedestalRecipe implements GeneratedRecipe {
		private final ResourceLocation id;
		private final Ingredient input;
		private final ItemStack output;
		private final Ingredient hammer;
		private final int strike;
		private final int exp;

		public FinishedPedestalRecipe(ResourceLocation id, ItemStack output, Ingredient input, @Nullable Ingredient hammer, int strike, int experience) {
			this.id = id;
			this.output = output;
			this.input = input;
			this.hammer = hammer == null ? Ingredient.of(ExtraBotanyTags.Items.HAMMERS) : hammer;
			this.strike = strike;
			this.exp = experience;
		}

		public FinishedPedestalRecipe(ResourceLocation id, ItemStack output, Ingredient input) {
			this(id, output, input, null, 5, 5);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public net.minecraft.world.item.crafting.Recipe<?> recipe() {
			return new io.github.lounode.extrabotany.common.crafting.PedestalsRecipe(output, hammer, input, strike, exp);
		}
	}
}
