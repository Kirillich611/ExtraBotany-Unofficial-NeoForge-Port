package io.github.lounode.extrabotany.common.crafting;

import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import io.github.lounode.extrabotany.api.recipe.EdelweissRecipe;

public class EdelweissRecipes implements EdelweissRecipe {

	private final EntityTypePredicate input;
	private final int outputMana;

	public EdelweissRecipes(EntityTypePredicate input, int outputMana) {
		this.input = input;
		this.outputMana = outputMana;
	}

	@Override
	public EntityTypePredicate getInput() {
		return input;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraBotanyRecipeTypes.EDELWEISS_SERIALIZER;
	}

	@Override
	public RecipeType<? extends EdelweissRecipe> getType() {
		return ExtraBotanyRecipeTypes.EDELWEISS_RECIPE_TYPE;
	}

	@Override
	public int getManaOutput() {
		return outputMana;
	}

	public static class Serializer implements RecipeSerializer<EdelweissRecipes> {
		private static final com.mojang.serialization.MapCodec<EdelweissRecipes> CODEC =
				com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(
						net.minecraft.advancements.critereon.EntityTypePredicate.CODEC.fieldOf("input").forGetter(EdelweissRecipes::getInput),
						com.mojang.serialization.Codec.INT.fieldOf("outputMana").forGetter(EdelweissRecipes::getManaOutput)
				).apply(instance, EdelweissRecipes::new));
		private static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, EdelweissRecipes> STREAM_CODEC =
				net.minecraft.network.codec.StreamCodec.composite(
						net.minecraft.network.codec.ByteBufCodecs.fromCodecWithRegistries(net.minecraft.advancements.critereon.EntityTypePredicate.CODEC), EdelweissRecipes::getInput,
						net.minecraft.network.codec.ByteBufCodecs.VAR_INT, EdelweissRecipes::getManaOutput,
						EdelweissRecipes::new);

		@Override
		public com.mojang.serialization.MapCodec<EdelweissRecipes> codec() {
			return CODEC;
		}

		@Override
		public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, EdelweissRecipes> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
