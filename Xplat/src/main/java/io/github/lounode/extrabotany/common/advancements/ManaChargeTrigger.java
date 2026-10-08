package io.github.lounode.extrabotany.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

import static io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix;

public class ManaChargeTrigger extends SimpleCriterionTrigger<ManaChargeTrigger.TriggerInstance> {
	public static final ResourceLocation ID = prefix("mana_charge");
	public static final ManaChargeTrigger INSTANCE = new ManaChargeTrigger();

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack stack, long mana) {
		trigger(player, instance -> instance.matches(stack, mana));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, ItemPredicate item, MinMaxBoundsExtension.Longs mana) implements SimpleInstance {

		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
				ItemPredicate.CODEC.optionalFieldOf("item", ItemPredicate.Builder.item().build()).forGetter(TriggerInstance::item),
				MinMaxBoundsExtension.Longs.CODEC.optionalFieldOf("mana", MinMaxBoundsExtension.Longs.ANY).forGetter(TriggerInstance::mana)
		).apply(instance, TriggerInstance::new));
		public static Criterion<TriggerInstance> manaCharged(ItemPredicate item, MinMaxBoundsExtension.Longs mana) {
			return INSTANCE.createCriterion(new TriggerInstance(Optional.empty(), item, mana));
		}

		public boolean matches(ItemStack stack, long mana) {
			return item.test(stack) && this.mana.matches(mana);
		}
	}
}
