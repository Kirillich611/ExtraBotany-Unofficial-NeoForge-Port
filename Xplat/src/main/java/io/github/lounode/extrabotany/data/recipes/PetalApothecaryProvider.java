package io.github.lounode.extrabotany.data.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.botania.common.item.BotaniaItems;

import io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;

public class PetalApothecaryProvider extends ExtraBotanyRecipeProvider {
	public PetalApothecaryProvider(PackOutput packOutput, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public void buildRecipes(RecipeOutputAdapter consumer) {
		Ingredient white = tagIngr("petals/white");
		Ingredient orange = tagIngr("petals/orange");
		Ingredient magenta = tagIngr("petals/magenta");
		Ingredient lightBlue = tagIngr("petals/light_blue");
		Ingredient yellow = tagIngr("petals/yellow");
		Ingredient lime = tagIngr("petals/lime");
		Ingredient pink = tagIngr("petals/pink");
		Ingredient gray = tagIngr("petals/gray");
		Ingredient lightGray = tagIngr("petals/light_gray");
		Ingredient cyan = tagIngr("petals/cyan");
		Ingredient purple = tagIngr("petals/purple");
		Ingredient blue = tagIngr("petals/blue");
		Ingredient brown = tagIngr("petals/brown");
		Ingredient green = tagIngr("petals/green");
		Ingredient red = tagIngr("petals/red");
		Ingredient black = tagIngr("petals/black");
		Ingredient runeWater = Ingredient.of(BotaniaItems.RUNE_OF_WATER);
		Ingredient runeFire = Ingredient.of(BotaniaItems.RUNE_OF_FIRE);
		Ingredient runeEarth = Ingredient.of(BotaniaItems.RUNE_OF_EARTH);
		Ingredient runeAir = Ingredient.of(BotaniaItems.RUNE_OF_AIR);
		Ingredient runeSpring = Ingredient.of(BotaniaItems.RUNE_OF_SPRING);
		Ingredient runeSummer = Ingredient.of(BotaniaItems.RUNE_OF_SUMMER);
		Ingredient runeAutumn = Ingredient.of(BotaniaItems.RUNE_OF_AUTUMN);
		Ingredient runeWinter = Ingredient.of(BotaniaItems.RUNE_OF_WINTER);
		Ingredient runeMana = Ingredient.of(BotaniaItems.RUNE_OF_MANA);
		Ingredient runeLust = Ingredient.of(BotaniaItems.RUNE_OF_LUST);
		Ingredient runeGluttony = Ingredient.of(BotaniaItems.RUNE_OF_GLUTTONY);
		Ingredient runeGreed = Ingredient.of(BotaniaItems.RUNE_OF_GREED);
		Ingredient runeSloth = Ingredient.of(BotaniaItems.RUNE_OF_SLOTH);
		Ingredient runeWrath = Ingredient.of(BotaniaItems.RUNE_OF_WRATH);
		Ingredient runeEnvy = Ingredient.of(BotaniaItems.RUNE_OF_ENVY);
		Ingredient runePride = Ingredient.of(BotaniaItems.RUNE_OF_PRIDE);

		Ingredient redstoneRoot = Ingredient.of(BotaniaItems.REDSTONE_ROOT);
		Ingredient pixieDust = Ingredient.of(BotaniaItems.PIXIE_DUST);
		Ingredient gaiaSpirit = Ingredient.of(BotaniaItems.GAIA_SPIRIT);
		Ingredient spritFragment = Ingredient.of(ExtraBotanyItems.spiritFragment);
		Ingredient manaDust = Ingredient.of(BotaniaItems.MANA_POWDER);
		//Recipes

		consumer.accept(make(ExtrabotanyFlowerBlocks.tradeOrchid, lime, lime, green, brown, runeGreed, runeLust, redstoneRoot));
		consumer.accept(make(ExtrabotanyFlowerBlocks.woodienia, brown, brown, brown, gray, Ingredient.of(ExtraBotanyItems.elementiumQuartz), runeGluttony, redstoneRoot));
		consumer.accept(make(ExtrabotanyFlowerBlocks.reikarlily, lightBlue, lightBlue, cyan, cyan, blue, runePride, runeEnvy, runeSloth, gaiaSpirit));
		consumer.accept(make(ExtrabotanyFlowerBlocks.bellflower, yellow, yellow, lime, lime, spritFragment));
		consumer.accept(make(ExtrabotanyFlowerBlocks.annoyingflower, white, white, pink, pink, green, runeMana, spritFragment));
		consumer.accept(make(ExtrabotanyFlowerBlocks.stonesia, gray, gray, black, gaiaSpirit, runeAutumn, runeGluttony));
		consumer.accept(make(ExtrabotanyFlowerBlocks.edelweiss, white, white, white, lightBlue, lightBlue, manaDust, runeMana, runeWinter));
		consumer.accept(make(ExtrabotanyFlowerBlocks.resoncund, magenta, magenta, orange, orange, runeLust, runeGluttony));
		consumer.accept(make(ExtrabotanyFlowerBlocks.sunshineLily, yellow, yellow, yellow, orange));
		consumer.accept(make(ExtrabotanyFlowerBlocks.moonlightLily, black, black, purple, gray));
		consumer.accept(make(ExtrabotanyFlowerBlocks.serenitian, purple, purple, blue, blue, runeMana, runeSloth, runeGreed, gaiaSpirit, Ingredient.of(Items.WITHER_ROSE)));
		consumer.accept(make(ExtrabotanyFlowerBlocks.twinstar, yellow, yellow, yellow, orange, orange, orange, manaDust, manaDust));
		consumer.accept(make(ExtrabotanyFlowerBlocks.omniviolet, purple, purple, blue, blue, runeSpring, runeMana, runeLust));
		consumer.accept(make(ExtrabotanyFlowerBlocks.tinkle, yellow, yellow, green, lime, runeWater, runeEarth, manaDust, spritFragment, spritFragment));
		consumer.accept(make(ExtrabotanyFlowerBlocks.bloodEnchantress, red, red, red, red, runeFire, runeSummer, runeWrath));
		consumer.accept(make(ExtrabotanyFlowerBlocks.mirrowtunia, cyan, cyan, lightBlue, blue, runeWrath, runePride, runeAir, manaDust));
		consumer.accept(make(ExtrabotanyFlowerBlocks.manalink, cyan, cyan, cyan, lightBlue, lightBlue, runeSloth, runeLust, gaiaSpirit));
		consumer.accept(make(ExtrabotanyFlowerBlocks.necrofleur, gray, gray, pink, pink, red, runeWrath, manaDust));
		consumer.accept(make(ExtrabotanyFlowerBlocks.enchanter, purple, purple, magenta, lime, lime, runePride, runeGreed, runeGluttony, gaiaSpirit));

	}
}
