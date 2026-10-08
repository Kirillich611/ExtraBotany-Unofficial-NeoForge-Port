package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class EdelweissRecipeProvider extends ExtraBotanyRecipeProvider {

	public EdelweissRecipeProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutputAdapter consumer) {
		consumer.accept(new Eat(id("snow_golem"), EntityTypePredicate.of(EntityType.SNOW_GOLEM), 3200));
	}

	protected ResourceLocation id(String id) {
		return prefix("edelweiss/" + id);
	}

	protected static class Eat implements GeneratedRecipe {
		private final ResourceLocation id;
		private final EntityTypePredicate input;
		private final int outputMana;

		public Eat(ResourceLocation id, EntityTypePredicate input, int outputMana) {
			this.id = id;
			this.input = input;
			this.outputMana = outputMana;
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public net.minecraft.world.item.crafting.Recipe<?> recipe() {
			return new io.github.lounode.extrabotany.common.crafting.EdelweissRecipes(input, outputMana);
		}
	}
}
