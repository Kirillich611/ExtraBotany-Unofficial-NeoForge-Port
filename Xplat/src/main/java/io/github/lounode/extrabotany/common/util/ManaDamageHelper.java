package io.github.lounode.extrabotany.common.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.mana.ManaItemHandler;

/** Preserves ExtraBotany's item-specific mana costs with Botania's component-based tools. */
public final class ManaDamageHelper {
	private ManaDamageHelper() {}

	public static int damageItemIfPossible(ItemStack stack, int amount, LivingEntity entity, int manaPerDamage) {
		if (!(entity instanceof Player player)) {
			return amount;
		}
		while (amount > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, manaPerDamage, true)) {
			amount--;
		}
		return amount;
	}
}
