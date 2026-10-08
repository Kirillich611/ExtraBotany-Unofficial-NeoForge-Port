package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.concurrent.CompletableFuture;

public abstract class ExtraBotanyRecipeProvider extends RecipeProvider {
	protected ExtraBotanyRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected final void buildRecipes(RecipeOutput output) {
		buildRecipes(new RecipeOutputAdapter() {
			@Override
			public Advancement.Builder advancement() {
				return output.advancement();
			}

			@Override
			public void accept(ResourceLocation id, Recipe<?> recipe, AdvancementHolder advancement, ICondition... conditions) {
				output.accept(id, recipe, advancement, conditions);
			}
		});
	}

	protected abstract void buildRecipes(RecipeOutputAdapter output);

	protected interface GeneratedRecipe {
		ResourceLocation getId();
		Recipe<?> recipe();
	}

	protected interface RecipeOutputAdapter extends RecipeOutput {
		default void accept(GeneratedRecipe recipe) {
			accept(recipe.getId(), recipe.recipe(), null);
		}
	}

	public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItem(ItemLike item) {
		return has(item);
	}

	public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromTag(TagKey<Item> tag) {
		return has(tag);
	}

	protected static Ingredient tagIngr(String path) {
		return Ingredient.of(TagKey.create(net.minecraft.core.registries.Registries.ITEM,
				ResourceLocation.fromNamespaceAndPath("botania", path)));
	}

	protected static Block getBlockOrThrow(ResourceLocation id) {
		return io.github.lounode.extrabotany.common.util.RegistryUtil.getBlockOrThrow(id);
	}

	protected static void specialRecipe(RecipeOutput output, SimpleCraftingRecipeSerializer<?> serializer) {
		Recipe<?> recipe = serializer.codec().codec().parse(com.mojang.serialization.JsonOps.INSTANCE,
				new com.google.gson.JsonObject()).getOrThrow();
		output.accept(net.minecraft.core.registries.BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer), recipe, null);
	}

	protected static ShapedRecipeBuilder stairs(ItemLike result, ItemLike base) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 4).define('B', base)
				.pattern("B  ").pattern("BB ").pattern("BBB").unlockedBy("has_item", has(base));
	}

	protected static ShapedRecipeBuilder slabShape(ItemLike result, ItemLike base) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 6).define('B', base)
				.pattern("BBB").unlockedBy("has_item", has(base));
	}

	protected static ShapedRecipeBuilder chiseled(ItemLike result, ItemLike base) {
		return chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base));
	}

	protected static ShapedRecipeBuilder brick(ItemLike result, ItemLike base) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 4).define('B', base)
				.pattern("BB").pattern("BB").unlockedBy("has_item", has(base));
	}

	protected static ShapedRecipeBuilder pillar(ItemLike result, ItemLike base) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2).define('B', base)
				.pattern("B").pattern("B").unlockedBy("has_item", has(base));
	}

	protected static ShapedRecipeBuilder compression(ItemLike result, TagKey<Item> ingredient) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result).define('I', ingredient)
				.pattern("III").pattern("III").pattern("III").unlockedBy("has_item", has(ingredient));
	}

	protected static void deconstruct(RecipeOutput output, ItemLike result, TagKey<Item> ingredient, String id) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 9).requires(ingredient)
				.unlockedBy("has_item", has(ingredient)).save(output,
						io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix("conversions/" + id));
	}

	protected static GeneratedRecipe make(ItemLike flower, Ingredient... ingredients) {
		ResourceLocation id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(flower.asItem());
		return new GeneratedRecipe() {
			@Override
			public ResourceLocation getId() {
				return id.withPrefix("petal_apothecary/");
			}

			@Override
			public Recipe<?> recipe() {
				return new vazkii.botania.common.crafting.PetalApothecaryRecipe(new net.minecraft.world.item.ItemStack(flower),
						Ingredient.of(vazkii.botania.common.lib.BotaniaTags.Items.SEED_APOTHECARY_REAGENT), ingredients);
			}
		};
	}

	protected static Item getItemOrThrow(ResourceLocation id) {
		return io.github.lounode.extrabotany.common.util.RegistryUtil.getItemOrThrow(id);
	}

	protected static void registerSimpleArmorSet(RecipeOutput output, Ingredient material, String name, Criterion<?> criterion) {
		String[] slots = { "helmet", "chestplate", "leggings", "boots" };
		String[][] patterns = { { "MMM", "M M" }, { "M M", "MMM", "MMM" }, { "MMM", "M M", "M M" }, { "M M", "M M" } };
		for (int i = 0; i < slots.length; i++) {
			ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT,
					getItemOrThrow(ResourceLocation.fromNamespaceAndPath("extrabotany", name + "_" + slots[i]))).define('M', material);
			for (String row : patterns[i]) {
				builder.pattern(row);
			}
			builder.unlockedBy("has_item", criterion).save(output);
		}
	}

	protected static void createFloatingFlowerRecipe(RecipeOutput output, Item flower) {
		ResourceLocation id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(flower);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, getItemOrThrow(id.withPrefix("floating_")))
				.requires(vazkii.botania.common.lib.BotaniaTags.Items.FLOATING_FLOWERS).requires(flower)
				.group("botania:floating_flower").unlockedBy("has_item", has(flower)).save(output);
	}
}
