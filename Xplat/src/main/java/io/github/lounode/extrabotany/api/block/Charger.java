package io.github.lounode.extrabotany.api.block;

import net.minecraft.world.item.ItemStack;

public interface Charger {
	ItemStack getItem();
	void setItem(ItemStack stack);
	float getChargeProcess();

	default boolean isValidItem(ItemStack stack) {
		return vazkii.botania.api.mana.ManaItem.LOOKUP.find(stack) != null;
	}
}
