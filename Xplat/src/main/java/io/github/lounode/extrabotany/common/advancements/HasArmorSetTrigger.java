package io.github.lounode.extrabotany.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;

import java.util.Optional;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class HasArmorSetTrigger extends SimpleCriterionTrigger<HasArmorSetTrigger.TriggerInstance> {
	public static final ResourceLocation ID = prefix("has_armor_set");
	public static final HasArmorSetTrigger INSTANCE = new HasArmorSetTrigger();

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player) {
		trigger(player, instance -> instance.matches(player));
	}

	public record ArmorPredicates(ItemPredicate head, ItemPredicate chest, ItemPredicate legs, ItemPredicate feet) {
		private static final Codec<ArmorPredicates> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemPredicate.CODEC.fieldOf("head").forGetter(ArmorPredicates::head),
				ItemPredicate.CODEC.fieldOf("chest").forGetter(ArmorPredicates::chest),
				ItemPredicate.CODEC.fieldOf("legs").forGetter(ArmorPredicates::legs),
				ItemPredicate.CODEC.fieldOf("feet").forGetter(ArmorPredicates::feet)
		).apply(instance, ArmorPredicates::new));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, ArmorPredicates armor) implements SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
				ArmorPredicates.CODEC.fieldOf("armor").forGetter(TriggerInstance::armor)
		).apply(instance, TriggerInstance::new));

		public static Criterion<TriggerInstance> forArmorSet(ItemStack[] set) {
			return INSTANCE.createCriterion(new TriggerInstance(Optional.empty(), new ArmorPredicates(
					ItemPredicate.Builder.item().of(set[0].getItem()).build(),
					ItemPredicate.Builder.item().of(set[1].getItem()).build(),
					ItemPredicate.Builder.item().of(set[2].getItem()).build(),
					ItemPredicate.Builder.item().of(set[3].getItem()).build())));
		}

		public boolean matches(Player player) {
			boolean darkened = player.getItemBySlot(EquipmentSlot.CHEST).is(ExtraBotanyItems.sanguinePleiadesCombatMaidSuit);
			return armor.head.test(player.getItemBySlot(EquipmentSlot.HEAD))
					&& (darkened || armor.chest.test(player.getItemBySlot(EquipmentSlot.CHEST)))
					&& armor.legs.test(player.getItemBySlot(EquipmentSlot.LEGS))
					&& armor.feet.test(player.getItemBySlot(EquipmentSlot.FEET))
					&& (!darkened || player.getItemBySlot(EquipmentSlot.HEAD).is(ExtraBotanyItems.pleiadesCombatMaidHeadgear)
							&& player.getItemBySlot(EquipmentSlot.LEGS).is(ExtraBotanyItems.pleiadesCombatMaidSkirt)
							&& player.getItemBySlot(EquipmentSlot.FEET).is(ExtraBotanyItems.pleiadesCombatMaidBoots));
		}
	}
}
