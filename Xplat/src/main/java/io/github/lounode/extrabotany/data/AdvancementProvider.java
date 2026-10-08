package io.github.lounode.extrabotany.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import io.github.lounode.extrabotany.common.advancements.HasArmorSetTrigger;
import io.github.lounode.extrabotany.common.advancements.ManaChargeTrigger;
import io.github.lounode.extrabotany.common.advancements.MinMaxBoundsExtension;
import io.github.lounode.extrabotany.common.block.ExtraBotanyBlocks;
import io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks;
import io.github.lounode.extrabotany.common.entity.ExtraBotanyEntityType;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.github.lounode.extrabotany.common.item.equipment.armor.goblin_slayer.GoblinSlayerArmorItem;
import io.github.lounode.extrabotany.common.item.equipment.armor.pleiades_combat_maid.PleiadesCombatMaidArmorItem;
import io.github.lounode.extrabotany.common.item.equipment.armor.shadow_warrior.ShadowWarriorArmorItem;
import io.github.lounode.extrabotany.common.item.equipment.armor.starry_idol.StarryIdolArmorItem;
import io.github.lounode.extrabotany.common.item.relic.MasterBandOfManaItem;
import io.github.lounode.extrabotany.common.lib.ExtraBotanyTags;
import io.github.lounode.extrabotany.common.lib.LibAdvancementNames;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class AdvancementProvider extends vazkii.botania.data.AdvancementProvider {

	public static String CODE_TRIGGE = "code_triggered";

	public static net.minecraft.data.advancements.AdvancementProvider create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		return new net.minecraft.data.advancements.AdvancementProvider(packOutput, lookupProvider, List.of(new ExtrabotanyAdvancements()));
	}

	public static class ExtrabotanyAdvancements implements AdvancementSubProvider {
		@Override
		public void generate(HolderLookup.Provider lookup, Consumer<net.minecraft.advancements.AdvancementHolder> consumer) {
			net.minecraft.advancements.AdvancementHolder root = Advancement.Builder.advancement()
					.display(rootDisplay(ExtraBotanyItems.zadkiel, "itemGroup.extrabotany", "extrabotany.desc", prefix("textures/block/photonium_block.png")))
					.addCriterion("use_lexicon", UseItemSuccessTrigger.Instance.used(BotaniaItems.LEXICA_BOTANIA))
					.save(consumer, mainId(LibAdvancementNames.ROOT));
			net.minecraft.advancements.AdvancementHolder senbonZakura = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.manaReader, LibAdvancementNames.SENBON_ZAKURA, AdvancementType.CHALLENGE))
					.parent(root)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.SENBON_ZAKURA));
			//Pedestal
			net.minecraft.advancements.AdvancementHolder pedestal = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyBlocks.livingrockPedestal, LibAdvancementNames.CRAFT_PEDESTAL, AdvancementType.TASK))
					.parent(root)
					.addCriterion("craft_pedestal", onPickup(ExtraBotanyTags.Items.PEDESTALS))
					.save(consumer, mainId(LibAdvancementNames.CRAFT_PEDESTAL));
			net.minecraft.advancements.AdvancementHolder kurukuru = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyBlocks.livingrockPedestal, LibAdvancementNames.KURUKURU, AdvancementType.GOAL))
					.parent(pedestal)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.KURUKURU));
			net.minecraft.advancements.AdvancementHolder goodtek = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.manasteelHammer, LibAdvancementNames.GOODTEK, AdvancementType.TASK))
					.parent(pedestal)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.GOODTEK));

			net.minecraft.advancements.AdvancementHolder deepDarkFantasy = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.nightmareFuel, LibAdvancementNames.DEEP_DARK_FANTASY, AdvancementType.TASK))
					.parent(root)
					.addCriterion("eat_nightmare_fuel", UseItemSuccessTrigger.Instance.used(ExtraBotanyItems.nightmareFuel)
					)
					.save(consumer, mainId(LibAdvancementNames.DEEP_DARK_FANTASY));
			net.minecraft.advancements.AdvancementHolder stygianTwins = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.shadowium, LibAdvancementNames.STYGIAN_TWINS, AdvancementType.TASK))
					.parent(goodtek)
					.addCriterion("craft_shadowium", onPickup(ExtraBotanyTags.Items.INGOTS_SHADOWIUM))
					.addCriterion("craft_photonium", onPickup(ExtraBotanyTags.Items.INGOTS_SHADOWIUM))
					.save(consumer, mainId(LibAdvancementNames.STYGIAN_TWINS));
			net.minecraft.advancements.AdvancementHolder gaiaTrial = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.challengeTicket, LibAdvancementNames.GAIA_TRIAL, AdvancementType.CHALLENGE))
					.parent(stygianTwins)
					.addCriterion("kill_gaia_3rd",
							KilledTrigger.TriggerInstance.playerKilledEntity(
									EntityPredicate.Builder.entity()
											.of(ExtraBotanyEntityType.GAIA_III)
							)
					)
					.save(consumer, mainId(LibAdvancementNames.GAIA_TRIAL));
			net.minecraft.advancements.AdvancementHolder theSourceOfHonkai = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.pandorasBox, LibAdvancementNames.THE_SOURCE_OF_HONKAI, AdvancementType.TASK))
					.parent(gaiaTrial)
					.addCriterion("open_pandoras_box", UseItemSuccessTrigger.Instance.used(ExtraBotanyItems.pandorasBox))
					.save(consumer, mainId(LibAdvancementNames.THE_SOURCE_OF_HONKAI));
			net.minecraft.advancements.AdvancementHolder theOriginalDivineKey = relicBindAdvancement(
					ExtraBotanyItems.voidArchives, LibAdvancementNames.THE_ORIGINAL_DIVINE_KEY)
					.parent(theSourceOfHonkai)
					.save(consumer, mainId(LibAdvancementNames.THE_ORIGINAL_DIVINE_KEY));
			net.minecraft.advancements.AdvancementHolder iSeeEveryThing = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.camera, LibAdvancementNames.I_SEE_EVERYTHING, AdvancementType.GOAL))
					.parent(stygianTwins)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.I_SEE_EVERYTHING));
			net.minecraft.advancements.AdvancementHolder onePunch = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.featherOfJingwei, LibAdvancementNames.ONE_PUNCH, AdvancementType.CHALLENGE))
					.parent(theSourceOfHonkai)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.ONE_PUNCH));
			net.minecraft.advancements.AdvancementHolder soulSteel = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.orichalcos, LibAdvancementNames.SOULSTEEL, AdvancementType.CHALLENGE))
					.parent(theSourceOfHonkai)
					.addCriterion("craft_orichalcos", onPickup(ExtraBotanyTags.Items.INGOTS_ORICHALCOS))
					.save(consumer, mainId(LibAdvancementNames.SOULSTEEL));
			//ManaRing
			net.minecraft.advancements.AdvancementHolder craftRing = relicBindAdvancement(
					ExtraBotanyItems.manaRingMaster, LibAdvancementNames.PANDA_DO_NOT_WEAR_RINGS)
					.parent(soulSteel)
					.save(consumer, mainId(LibAdvancementNames.PANDA_DO_NOT_WEAR_RINGS));

			net.minecraft.advancements.AdvancementHolder overlord = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.manaRingMaster, LibAdvancementNames.OVERLOAD, AdvancementType.CHALLENGE))
					.parent(craftRing)
					.addCriterion("mana_charge", ManaChargeTrigger.TriggerInstance.manaCharged(
							ItemPredicate.Builder.item().of(ExtraBotanyItems.manaRingMaster).build(),
							MinMaxBoundsExtension.Longs.atLeast(MasterBandOfManaItem.ADVANCEMENT_PHASE1_REQUIRE)
					))
					.save(consumer, mainId(LibAdvancementNames.OVERLOAD));
			net.minecraft.advancements.AdvancementHolder lordOfKing = Advancement.Builder.advancement()
					.display(hidden(ExtraBotanyItems.manaRingMaster, LibAdvancementNames.LOAD_OF_RING, AdvancementType.CHALLENGE))
					.parent(overlord)
					.addCriterion("mana_charge", ManaChargeTrigger.TriggerInstance.manaCharged(
							ItemPredicate.Builder.item().of(ExtraBotanyItems.manaRingMaster).build(),
							MinMaxBoundsExtension.Longs.atLeast(MasterBandOfManaItem.ADVANCEMENT_PHASE2_REQUIRE)
					))
					.save(consumer, mainId(LibAdvancementNames.LOAD_OF_RING));
			net.minecraft.advancements.AdvancementHolder hundredBlockPierce = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.failnaught, LibAdvancementNames.HUNDRED_BLOCK_PIERCE, AdvancementType.CHALLENGE))
					.parent(soulSteel)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.HUNDRED_BLOCK_PIERCE));
			net.minecraft.advancements.AdvancementHolder skyIsNotTheLimit = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.aerialite, LibAdvancementNames.SKY_IS_NOT_THE_LIMIT, AdvancementType.TASK))
					.parent(stygianTwins)
					.addCriterion("craft_aerialite", onPickup(ExtraBotanyTags.Items.INGOTS_AERIALITE))
					.save(consumer, mainId(LibAdvancementNames.SKY_IS_NOT_THE_LIMIT));

			net.minecraft.advancements.AdvancementHolder potatoServer = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.gildedPotatoMashed, LibAdvancementNames.POTATO_SERVER, AdvancementType.TASK))
					.parent(root)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.POTATO_SERVER));

			net.minecraft.advancements.AdvancementHolder rheinKraft = relicBindAdvancement(ExtraBotanyItems.rheinHammer, LibAdvancementNames.RHEIN_KRAFT)
					.parent(theSourceOfHonkai)
					.save(consumer, mainId(LibAdvancementNames.RHEIN_KRAFT));
			net.minecraft.advancements.AdvancementHolder spongeHammer = Advancement.Builder.advancement()
					.display(simple(Items.SPONGE, LibAdvancementNames.SPONGE_HAMMER, AdvancementType.TASK))
					.parent(rheinKraft)
					.addCriterion(CODE_TRIGGE, net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.SPONGE_HAMMER));
			net.minecraft.advancements.AdvancementHolder omgItsMiku = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.starryIdolHeadgear, LibAdvancementNames.OMG_ITS_MIKU, AdvancementType.TASK))
					.parent(root)
					.addCriterion("has_armor_set", armorSet(StarryIdolArmorItem.ARMOR_SET))
					.save(consumer, mainId(LibAdvancementNames.OMG_ITS_MIKU));
			net.minecraft.advancements.AdvancementHolder sevenSamurai = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.shadowWarriorHelmet, LibAdvancementNames.SEVEN_SAMURAI, AdvancementType.CHALLENGE))
					.parent(stygianTwins)
					.addCriterion("has_armor_set", armorSet(ShadowWarriorArmorItem.ARMOR_SET))
					.save(consumer, mainId(LibAdvancementNames.SEVEN_SAMURAI));
			net.minecraft.advancements.AdvancementHolder goblinKiller = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.goblinSlayerHelmet, LibAdvancementNames.GOBLIN_KILLER, AdvancementType.CHALLENGE))
					.parent(stygianTwins)
					.addCriterion("has_armor_set", armorSet(GoblinSlayerArmorItem.ARMOR_SET))
					.save(consumer, mainId(LibAdvancementNames.GOBLIN_KILLER));
			net.minecraft.advancements.AdvancementHolder cuteDress = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.pleiadesCombatMaidSuit, LibAdvancementNames.CUTE_DRESS, AdvancementType.CHALLENGE))
					.parent(stygianTwins)
					.addCriterion("has_armor_set", armorSet(PleiadesCombatMaidArmorItem.ARMOR_SET))
					.save(consumer, mainId(LibAdvancementNames.CUTE_DRESS));
			net.minecraft.advancements.AdvancementHolder corruption = Advancement.Builder.advancement()
					.display(simple(ExtraBotanyItems.sanguinePleiadesCombatMaidSuit, LibAdvancementNames.CORRUPTION, AdvancementType.CHALLENGE))
					.parent(cuteDress)
					.addCriterion("has_item", onPickup(ExtraBotanyItems.sanguinePleiadesCombatMaidSuit))
					.save(consumer, mainId(LibAdvancementNames.CORRUPTION));
			net.minecraft.advancements.AdvancementHolder aBaldrupt = Advancement.Builder.advancement()
					.display(hidden(BotaniaBlocks.DAFFOMILL, LibAdvancementNames.A_BALDRUPT, AdvancementType.TASK))
					.parent(goodtek)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.A_BALDRUPT));
			net.minecraft.advancements.AdvancementHolder muromiSan = Advancement.Builder.advancement()
					.display(simple(ExtrabotanyFlowerBlocks.annoyingflower, LibAdvancementNames.MUROMI_SAN, AdvancementType.GOAL))
					.parent(goodtek)
					.addCriterion("code_triggered", net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.MUROMI_SAN));
			net.minecraft.advancements.AdvancementHolder itsOnlyTheFairyTale = Advancement.Builder.advancement()
					.display(simple(ExtrabotanyFlowerBlocks.edelweiss, LibAdvancementNames.ITS_ONLY_THE_FAIRY_TALE, AdvancementType.TASK))
					.parent(goodtek)
					.addCriterion(CODE_TRIGGE, net.minecraft.advancements.CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(consumer, mainId(LibAdvancementNames.ITS_ONLY_THE_FAIRY_TALE));
		}
	}

	protected static net.minecraft.advancements.Criterion<HasArmorSetTrigger.TriggerInstance> armorSet(Supplier<ItemStack[]> armorSet) {
		return HasArmorSetTrigger.TriggerInstance.forArmorSet(armorSet.get());
	}

	private static Advancement.Builder relicBindAdvancement(Item relicItem, String titleKey) {
		return Advancement.Builder.advancement()
				.display(simple(relicItem, titleKey, AdvancementType.CHALLENGE))
				.rewards(AdvancementRewards.Builder.experience(50))
				.addCriterion("has_relic", RelicBindTrigger.Instance.bound(relicItem));
	}

	protected static DisplayInfo simple(ItemLike icon, String name, AdvancementType frameType) {
		String expandedName = "advancement.extrabotany:" + name;
		return new DisplayInfo(new ItemStack(icon.asItem()),
				Component.translatable(expandedName),
				Component.translatable(expandedName + ".desc"),
				java.util.Optional.empty(), frameType, true, true, false);
	}

	protected static DisplayInfo hidden(ItemLike icon, String name, AdvancementType frameType) {
		String expandedName = "advancement.extrabotany:" + name;
		return new DisplayInfo(new ItemStack(icon.asItem()),
				Component.translatable(expandedName),
				Component.translatable(expandedName + ".desc"),
				java.util.Optional.empty(), frameType, true, true, true);
	}

	private static String mainId(String name) {
		return prefix("main/" + name).toString();
	}

	private static String challengeId(String name) {
		return prefix("challenge/" + name).toString();
	}
}
