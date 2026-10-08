package io.github.lounode.extrabotany.common.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;

public final class PotionUtil {
	private PotionUtil() {}

	public static int getColor(Iterable<MobEffectInstance> effects) {
		return PotionContents.getColor(effects);
	}
}
