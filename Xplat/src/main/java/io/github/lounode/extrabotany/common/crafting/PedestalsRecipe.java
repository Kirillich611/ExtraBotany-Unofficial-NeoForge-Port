package io.github.lounode.extrabotany.common.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import io.github.lounode.extrabotany.api.recipe.PedestalRecipe;

public class PedestalsRecipe implements PedestalRecipe {
	private final ItemStack output;

	private final Ingredient smashTools;
	private final Ingredient input;
	private final int strike;
	private final int exp;

	public PedestalsRecipe(ItemStack output, Ingredient smashTools, Ingredient input, int strike, int exp) {
		this.output = output;
		this.input = input;
		this.smashTools = smashTools;
		this.strike = strike;
		this.exp = exp;
	}

	@Override
	public boolean matches(net.minecraft.world.item.crafting.RecipeInput container, Level world) {
		ItemStack inputStack = container.getItem(0);
		return input.test(inputStack);
	}

	@Override
	public ItemStack assemble(net.minecraft.world.item.crafting.RecipeInput container, net.minecraft.core.HolderLookup.Provider registryAccess) {
		return getResultItem(registryAccess).copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registryAccess) {
		return output;
	}

	@Override
	public Ingredient getSmashTools() {
		return smashTools;
	}

	@Override
	public Ingredient getInput() {
		return input;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public int getStrike() {
		return strike;
	}

	@Override
	public int getExp() {
		return exp;
	}

	@NotNull
	@Override
	public RecipeType<? extends PedestalRecipe> getType() {
		return ExtraBotanyRecipeTypes.PEDESTAL_SMASH_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ExtraBotanyRecipeTypes.PEDESTAL_SMASH_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<PedestalsRecipe> {
		private static final com.mojang.serialization.MapCodec<PedestalsRecipe> CODEC =
				com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(
						net.minecraft.world.item.ItemStack.CODEC.fieldOf("output").forGetter(PedestalsRecipe::getOutput),
						net.minecraft.world.item.crafting.Ingredient.CODEC_NONEMPTY.fieldOf("smash_tools").forGetter(PedestalsRecipe::getSmashTools),
						net.minecraft.world.item.crafting.Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(PedestalsRecipe::getInput),
						com.mojang.serialization.Codec.INT.fieldOf("strike").forGetter(PedestalsRecipe::getStrike),
						com.mojang.serialization.Codec.INT.fieldOf("exp").forGetter(PedestalsRecipe::getExp)
				).apply(instance, PedestalsRecipe::new));
		private static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PedestalsRecipe> STREAM_CODEC =
				net.minecraft.network.codec.StreamCodec.composite(
						net.minecraft.world.item.ItemStack.STREAM_CODEC, PedestalsRecipe::getOutput,
						net.minecraft.world.item.crafting.Ingredient.CONTENTS_STREAM_CODEC, PedestalsRecipe::getSmashTools,
						net.minecraft.world.item.crafting.Ingredient.CONTENTS_STREAM_CODEC, PedestalsRecipe::getInput,
						net.minecraft.network.codec.ByteBufCodecs.VAR_INT, PedestalsRecipe::getStrike,
						net.minecraft.network.codec.ByteBufCodecs.VAR_INT, PedestalsRecipe::getExp,
						PedestalsRecipe::new);

		@Override
		public com.mojang.serialization.MapCodec<PedestalsRecipe> codec() {
			return CODEC;
		}

		@Override
		public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PedestalsRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
