package io.github.lounode.extrabotany.common.item.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public interface ICustomEnchantable extends net.neoforged.neoforge.common.extensions.IItemExtension {
	boolean canEnchant(ItemStack stack, net.minecraft.core.Holder<Enchantment> enchantment);
	boolean canEnchantOnTable(ItemStack stack, net.minecraft.core.Holder<Enchantment> enchantment);

	@Override
	default boolean supportsEnchantment(ItemStack stack, net.minecraft.core.Holder<Enchantment> enchantment) {
		return canEnchant(stack, enchantment);
	}

	@Override
	default boolean isPrimaryItemFor(ItemStack stack, net.minecraft.core.Holder<Enchantment> enchantment) {
		return canEnchantOnTable(stack, enchantment);
	}
}
