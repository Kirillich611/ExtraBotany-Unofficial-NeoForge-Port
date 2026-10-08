package io.github.lounode.extrabotany.common.crafting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import vazkii.botania.api.recipe.StateIngredient;

import io.github.lounode.extrabotany.api.recipe.StonesiaRecipe;

public class StonesiasRecipe implements StonesiaRecipe {

	private final StateIngredient input;
	private final int outputMana;

	public StonesiasRecipe(StateIngredient input, int outputMana) {
		this.input = input;
		this.outputMana = outputMana;
	}

	@Override
	public StateIngredient getInput() {
		return input;
	}

	@Override
	public int getManaOutput() {
		return outputMana;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraBotanyRecipeTypes.STONESIA_SERIALIZER;
	}

	@Override
	public RecipeType<? extends StonesiaRecipe> getType() {
		return ExtraBotanyRecipeTypes.STONESIA_RECIPE_TYPE;
	}

	public static class Serializer implements RecipeSerializer<StonesiasRecipe> {
		private static final com.mojang.serialization.MapCodec<StonesiasRecipe> CODEC =
				com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(
						vazkii.botania.common.crafting.StateIngredients.TYPED_CODEC.fieldOf("input").forGetter(StonesiasRecipe::getInput),
						com.mojang.serialization.Codec.INT.fieldOf("outputMana").forGetter(StonesiasRecipe::getManaOutput)
				).apply(instance, StonesiasRecipe::new));
		private static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, StonesiasRecipe> STREAM_CODEC =
				net.minecraft.network.codec.StreamCodec.composite(
						vazkii.botania.common.crafting.StateIngredients.TYPED_STREAM_CODEC, StonesiasRecipe::getInput,
						net.minecraft.network.codec.ByteBufCodecs.VAR_INT, StonesiasRecipe::getManaOutput,
						StonesiasRecipe::new);

		@Override
		public com.mojang.serialization.MapCodec<StonesiasRecipe> codec() {
			return CODEC;
		}

		@Override
		public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, StonesiasRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
