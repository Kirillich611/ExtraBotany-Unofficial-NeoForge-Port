package io.github.lounode.extrabotany.common.brew.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

import io.github.lounode.extrabotany.common.ExtraBotanyDamageTypes;
import io.github.lounode.extrabotany.common.brew.ExtraBotanyMobEffects;

@EventBusSubscriber(modid = "extrabotany")
public class HealReverseMobEffect extends MobEffect {
	public HealReverseMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@SubscribeEvent
	private static void onLivingHeal(LivingHealEvent event) {
		LivingEntity entity = event.getEntity();
		float amount = event.getAmount();

		if (entity.hasEffect(ExtraBotanyMobEffects.HEAL_REVERSE)) {
			entity.hurt(ExtraBotanyDamageTypes.Sources.reverseHealDamage(entity.level().registryAccess()), amount);
			event.setAmount(0);
		}
	}
}
