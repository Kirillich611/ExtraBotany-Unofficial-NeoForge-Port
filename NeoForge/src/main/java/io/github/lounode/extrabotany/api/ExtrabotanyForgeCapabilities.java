package io.github.lounode.extrabotany.api;

import net.neoforged.neoforge.capabilities.ItemCapability;

import io.github.lounode.extrabotany.api.item.NatureEnergyItem;

public final class ExtrabotanyForgeCapabilities {
	private ExtrabotanyForgeCapabilities() {}

	public static final ItemCapability<NatureEnergyItem, Void> NATURE_ENERGY_ITEM = ItemCapability.createVoid(
			io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix("nature_energy_item"), NatureEnergyItem.class);
}
