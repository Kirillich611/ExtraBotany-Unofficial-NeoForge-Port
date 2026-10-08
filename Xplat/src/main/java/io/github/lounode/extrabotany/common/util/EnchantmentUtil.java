package io.github.lounode.extrabotany.common.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class EnchantmentUtil {
	private EnchantmentUtil() {}

	public static int getLevel(ResourceKey<Enchantment> enchantment, ItemStack stack) {
		for (var entry : stack.getEnchantments().entrySet()) {
			if (entry.getKey().is(enchantment)) {
				return entry.getIntValue();
			}
		}
		return 0;
	}
}
