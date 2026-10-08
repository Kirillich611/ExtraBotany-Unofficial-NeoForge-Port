package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.common.item.BotaniaItems;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class ElvenTradeProvider extends ExtraBotanyRecipeProvider {
	public ElvenTradeProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public void buildRecipes(RecipeOutputAdapter consumer) {
		consumer.accept(new FinishedElvenRecipe(id("elementium_quartz"), new ItemStack(ExtraBotanyItems.elementiumQuartz), Ingredient.of(BotaniaItems.MANA_QUARTZ), Ingredient.of(BotaniaItems.MANA_QUARTZ)));
	}

	protected static Ingredient ingr(ItemLike i) {
		return Ingredient.of(i);
	}

	private static ResourceLocation id(String path) {
		return prefix("elven_trade/" + path);
	}

	protected static class FinishedElvenRecipe implements GeneratedRecipe {
		private final ResourceLocation id;
		private final List<Ingredient> inputs;
		private final List<ItemStack> outputs;

		public FinishedElvenRecipe(ResourceLocation id, ItemStack output, Ingredient... inputs) {
			this(id, Arrays.asList(inputs), Collections.singletonList(output));
		}

		protected FinishedElvenRecipe(ResourceLocation id, List<Ingredient> inputs, List<ItemStack> outputs) {
			this.id = id;
			this.inputs = inputs;
			this.outputs = outputs;
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public net.minecraft.world.item.crafting.Recipe<?> recipe() {
			return new vazkii.botania.common.crafting.ElvenTradeRecipe(outputs, inputs);
		}
	}
}
