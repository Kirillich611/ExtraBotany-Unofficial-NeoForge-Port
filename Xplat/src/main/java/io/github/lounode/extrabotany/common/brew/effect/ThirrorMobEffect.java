package io.github.lounode.extrabotany.common.brew.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import io.github.lounode.extrabotany.common.brew.ExtraBotanyMobEffects;

import java.util.Objects;

public class ThirrorMobEffect extends MobEffect {
	public ThirrorMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@EventBusSubscriber(modid = "extrabotany")
	public static class EventHandler {

		@SubscribeEvent
		public static void onLivingAttack(LivingIncomingDamageEvent event) {
			LivingEntity defender = event.getEntity();
			if (!defender.hasEffect(ExtraBotanyMobEffects.THIRROR)) {
				return;
			}
			if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
				return;
			}

			int level = Objects.requireNonNull(defender.getEffect(ExtraBotanyMobEffects.THIRROR)).getAmplifier();
			float returnDamage = event.getAmount() / Math.max(1, 6 - level);

			attacker.hurt(defender.damageSources().thorns(defender), returnDamage);
		}
	}
}
