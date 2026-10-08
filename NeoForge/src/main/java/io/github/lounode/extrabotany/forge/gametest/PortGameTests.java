package io.github.lounode.extrabotany.forge.gametest;

import com.mojang.serialization.JsonOps;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.neoforge.BotaniaNeoForgeCapabilities;
import vazkii.botania.common.item.brew.BaseBrewItem;

import io.github.lounode.extrabotany.api.ExtrabotanyForgeCapabilities;
import io.github.lounode.extrabotany.common.brew.BrewUtil;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;

@GameTestHolder("extrabotany")
@PrefixGameTestTemplate(false)
public final class PortGameTests {
	@GameTest(template = "empty")
	public static void defaultCocktailCanReceiveFlaskBrew(GameTestHelper helper) {
		var cocktail = io.github.lounode.extrabotany.common.item.brew.ManaCocktailItem.getDefaultCocktail();
		var item = (BaseBrewItem) cocktail.getItem();
		helper.assertTrue(item.getSwigsLeft(cocktail) == 1, "Default cocktail must contain one sip");
		var flask = new ItemStack(vazkii.botania.common.item.BotaniaItems.BREW_FLASK);
		BrewUtil.setBrew(flask, vazkii.botania.common.brew.BotaniaBrews.VIGOR);
		var input = net.minecraft.world.item.crafting.CraftingInput.of(2, 1, java.util.List.of(cocktail, flask));
		var holder = helper.getLevel().getRecipeManager().byKey(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("extrabotany", "mana_cocktail_change_brew")).orElseThrow();
		var recipe = (net.minecraft.world.item.crafting.CraftingRecipe) holder.value();
		helper.assertTrue(recipe.matches(input, helper.getLevel()), "Cocktail/flask ingredients do not match");
		var output = recipe.assemble(input, helper.getLevel().registryAccess());
		helper.assertTrue(!output.isEmpty(), "Cocktail/flask crafting produces no result");
		helper.assertTrue(BrewUtil.getBrew(output) == vazkii.botania.common.brew.BotaniaBrews.VIGOR, "Crafting did not copy the flask brew");
		helper.assertTrue(item.getSwigsLeft(output) == item.getSwigs(output), "Crafted cocktail is not full");
		helper.succeed();
	}

	@GameTest(template = "empty")
	public static void ordinaryCraftingRecipesProduceOutputs(GameTestHelper helper) {
		int checked = 0;
		for (var holder : helper.getLevel().getRecipeManager().getRecipes()) {
			if (!holder.id().getNamespace().equals("extrabotany")) {
				continue;
			}
			var recipe = holder.value();
			if (recipe.getSerializer() != net.minecraft.world.item.crafting.RecipeSerializer.SHAPED_RECIPE
					&& recipe.getSerializer() != net.minecraft.world.item.crafting.RecipeSerializer.SHAPELESS_RECIPE) {
				continue;
			}
			var crafting = (net.minecraft.world.item.crafting.CraftingRecipe) recipe;
			var ingredients = crafting.getIngredients();
			var items = new java.util.ArrayList<ItemStack>();
			for (var ingredient : ingredients) {
				if (ingredient.isEmpty()) {
					items.add(ItemStack.EMPTY);
					continue;
				}
				var choices = ingredient.getItems();
				helper.assertTrue(choices.length > 0, "Unresolved ingredient in " + holder.id());
				items.add(choices[0].copy());
			}
			int width = crafting instanceof net.minecraft.world.item.crafting.ShapedRecipe shaped ? shaped.getWidth() : 3;
			int height = crafting instanceof net.minecraft.world.item.crafting.ShapedRecipe shaped ? shaped.getHeight() : (items.size() + 2) / 3;
			while (items.size() < width * height) {
				items.add(ItemStack.EMPTY);
			}
			var input = net.minecraft.world.item.crafting.CraftingInput.of(width, height, items);
			helper.assertTrue(crafting.matches(input, helper.getLevel()), "Crafting ingredients do not match " + holder.id());
			var output = crafting.assemble(input, helper.getLevel().registryAccess());
			helper.assertTrue(!output.isEmpty(), "Crafting produced no output for " + holder.id());
			helper.assertTrue(ItemStack.isSameItemSameComponents(output, crafting.getResultItem(helper.getLevel().registryAccess())), "Unexpected crafting output for " + holder.id());
			checked++;
		}
		helper.assertTrue(checked == 108, "Expected 108 ordinary crafting recipes, found " + checked);
		helper.succeed();
	}

	@GameTest(template = "empty")
	public static void botaniaWaterBowlFluidCapability(GameTestHelper helper) {
		var bowl = new ItemStack(vazkii.botania.common.item.BotaniaItems.WATER_BOWL);
		var fluid = bowl.getCapability(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.ITEM);
		helper.assertTrue(fluid != null, "Water bowl fluid capability is missing");
		helper.assertTrue(fluid.drain(1000, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE).getAmount() == 1000, "Water bowl simulation failed");
		helper.assertTrue(fluid.getContainer().is(vazkii.botania.common.item.BotaniaItems.WATER_BOWL), "Simulation consumed the water bowl");
		helper.assertTrue(fluid.drain(1000, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE).getAmount() == 1000, "Water bowl draining failed");
		helper.assertTrue(fluid.getContainer().is(net.minecraft.world.item.Items.BOWL), "Draining did not return an empty bowl");
		helper.assertTrue(fluid.getFluidInTank(0).isEmpty(), "Empty bowl still contains water");
		helper.succeed();
	}

	@GameTest(template = "empty")
	public static void recipesRoundTrip(GameTestHelper helper) {
		var ops = RegistryOps.create(JsonOps.INSTANCE, helper.getLevel().registryAccess());
		int count = 0;
		for (var holder : helper.getLevel().getRecipeManager().getRecipes()) {
			if (!holder.id().getNamespace().equals("extrabotany")) {
				continue;
			}
			var json = Recipe.CODEC.encodeStart(ops, holder.value()).getOrThrow();
			var decoded = Recipe.CODEC.parse(ops, json).getOrThrow();
			helper.assertTrue(decoded.getSerializer() == holder.value().getSerializer(), "Recipe serializer changed: " + holder.id());
			count++;
		}
		helper.assertTrue(count >= 200, "Too few ExtraBotany recipes loaded: " + count);
		helper.succeed();
	}

	@GameTest(template = "empty")
	public static void itemComponentsAndCapabilities(GameTestHelper helper) {
		var manaStack = new ItemStack(ExtraBotanyItems.manaRingMaster);
		var mana = manaStack.getCapability(BotaniaNeoForgeCapabilities.getItemApiLookupById(vazkii.botania.api.mana.ManaItem.LOOKUP));
		helper.assertTrue(mana != null, "Master mana ring capability is missing");
		mana.addMana(1000);
		helper.assertTrue(mana.getMana() == 1000, "Mana changes were not persisted");
		helper.assertTrue(manaStack.getCapability(BotaniaNeoForgeCapabilities.getItemApiLookupById(vazkii.botania.api.item.Relic.LOOKUP)) != null, "Relic capability is missing");
		helper.assertTrue(new ItemStack(ExtraBotanyItems.natureOrb).getCapability(ExtrabotanyForgeCapabilities.NATURE_ENERGY_ITEM) != null, "Nature orb capability is missing");
		var brew = BotaniaAPI.instance().getBrewRegistry().iterator().next();
		var cocktail = new ItemStack(ExtraBotanyItems.manaCocktail);
		BrewUtil.setBrew(cocktail, brew);
		helper.assertTrue(((BaseBrewItem) cocktail.getItem()).getBrew(cocktail) == brew, "Botania cannot read the cocktail brew component");
		var encoded = ItemStack.CODEC.encodeStart(RegistryOps.create(JsonOps.INSTANCE, helper.getLevel().registryAccess()), cocktail).getOrThrow();
		var restored = ItemStack.CODEC.parse(RegistryOps.create(JsonOps.INSTANCE, helper.getLevel().registryAccess()), encoded).getOrThrow();
		helper.assertTrue(BrewUtil.getBrew(restored) == brew, "Brew was lost during item serialization");
		helper.succeed();
	}

	@GameTest(template = "empty")
	public static void flowerStateSurvivesPlacement(GameTestHelper helper) {
		var block = io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.bellflower;
		var pos = new net.minecraft.core.BlockPos(1, 1, 1);
		helper.setBlock(pos.below(), net.minecraft.world.level.block.Blocks.DIRT);
		helper.setBlock(pos, block);
		var flower = (io.github.lounode.extrabotany.common.block.flower.generating.BellflowerBlockEntity) helper.getBlockEntity(pos);
		flower.setPassiveDecayTicks(1234);
		var drops = net.minecraft.world.level.block.Block.getDrops(block.defaultBlockState(), helper.getLevel(), helper.absolutePos(pos), flower);
		helper.assertTrue(drops.size() == 1, "Flower did not drop an item");
		helper.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR);
		helper.setBlock(pos, block);
		var restored = (io.github.lounode.extrabotany.common.block.flower.generating.BellflowerBlockEntity) helper.getBlockEntity(pos);
		restored.applyComponentsFromItemStack(drops.getFirst());
		helper.assertTrue(restored.getPassiveDecayTicks() == 1234, "Flower decay timer was reset after placement");
		helper.succeed();
	}

	@GameTest(template = "empty")
	public static void flowerStateSyncAndRedstone(GameTestHelper helper) {
		var pos = new net.minecraft.core.BlockPos(1, 1, 1);
		helper.setBlock(pos.below(), net.minecraft.world.level.block.Blocks.DIRT);
		helper.setBlock(pos, io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.bellflower);
		var flower = (io.github.lounode.extrabotany.common.block.flower.generating.BellflowerBlockEntity) helper.getBlockEntity(pos);
		flower.setPassiveDecayTicks(1234);
		helper.assertTrue(flower.getUpdateTag(helper.getLevel().registryAccess()).getInt("passiveDecayTicks") == 1234,
				"Client update omitted the flower decay timer");
		for (var block : java.util.List.of(
				io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.tradeOrchid,
				io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.tradeOrchidFloating,
				io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.woodienia,
				io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks.woodieniaFloating)) {
			helper.setBlock(pos, block.defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED, true));
			var powered = (vazkii.botania.api.block_entity.SpecialFlowerBlockEntity) helper.getBlockEntity(pos);
			helper.assertTrue(powered.isPowered(), "Flower does not expose its redstone state: " + block);
		}
		helper.succeed();
	}

	@GameTest(template = "empty")
	public static void legacyBotaniaNamesRemainReadable(GameTestHelper helper) {
		var items = net.minecraft.core.registries.BuiltInRegistries.ITEM;
		helper.assertTrue(items.get(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("botania", "lexicon"))
				== vazkii.botania.common.item.BotaniaItems.LEXICA_BOTANIA, "Old lexicon ID no longer resolves");
		helper.assertTrue(items.get(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("botania", "rune_fire"))
				== vazkii.botania.common.item.BotaniaItems.RUNE_OF_FIRE, "Old rune ID no longer resolves");
		var blocks = net.minecraft.core.registries.BuiltInRegistries.BLOCK;
		helper.assertTrue(blocks.get(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("botania", "fabulous_pool"))
				== vazkii.botania.common.block.BotaniaBlocks.FABULOUS_MANA_POOL, "Old fabulous pool ID no longer resolves");
		helper.assertTrue(blocks.get(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("botania", "diluted_pool"))
				== vazkii.botania.common.block.BotaniaBlocks.DILUTED_MANA_POOL, "Old diluted pool ID no longer resolves");
		helper.succeed();
	}

	@GameTest(template = "empty")
	public static void bossPacketsRoundTrip(GameTestHelper helper) {
		var event = new net.minecraft.server.level.ServerBossEvent(net.minecraft.network.chat.Component.literal("Gaia"),
				net.minecraft.world.BossEvent.BossBarColor.GREEN, net.minecraft.world.BossEvent.BossBarOverlay.PROGRESS);
		var packets = java.util.List.of(
				io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket.createAddPacket(event),
				io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket.createRemovePacket(event.getId()),
				io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket.createUpdateProgressPacket(event),
				io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket.createUpdateNamePacket(event),
				io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket.createUpdateStylePacket(event),
				io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket.createUpdatePropertiesPacket(event),
				new io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket(event.getId(), new io.github.lounode.extrabotany.network.clientbound.GaiaBossEventPacket.UpdatePlayerCountOperation(3)),
				new io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket(event.getId(), new io.github.lounode.extrabotany.network.clientbound.GaiaBossEventPacket.UpdateGrainTimeOperation(20)));
		for (var packet : packets) {
			var buffer = new net.minecraft.network.RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(), helper.getLevel().registryAccess());
			try {
				packet.encode(buffer);
				var decoded = io.github.lounode.extrabotany.network.clientbound.ColorfulBossEventPacket.decode(buffer);
				helper.assertTrue(packet.equals(decoded), "Boss packet changed during serialization: " + packet.operation().getType());
			} finally {
				buffer.release();
			}
		}
		helper.succeed();
	}
}
