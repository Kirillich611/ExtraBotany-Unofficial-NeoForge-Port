package io.github.lounode.extrabotany.api.recipe;

import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public interface EdelweissRecipe extends Recipe<net.minecraft.world.item.crafting.RecipeInput>, ManaOutputRecipe {
	ResourceLocation TYPE_ID = prefix("edelweiss");

	EntityTypePredicate getInput();

	@Override
	RecipeType<? extends EdelweissRecipe> getType();

	default int getManaOutput(@NotNull Level level, @NotNull BlockPos pos) {
		return getManaOutput();
	}

	@Override
	default boolean matches(net.minecraft.world.item.crafting.RecipeInput c, Level l) {
		return false;
	}

	@Override
	default ItemStack assemble(net.minecraft.world.item.crafting.RecipeInput c, @NotNull net.minecraft.core.HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	default ItemStack getResultItem(@NotNull net.minecraft.core.HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}
}
