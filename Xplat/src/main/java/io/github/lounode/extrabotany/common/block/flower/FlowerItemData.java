package io.github.lounode.extrabotany.common.block.flower;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;

import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;

/** Preserves a flower's cooldown or decay when it is broken and placed again. */
public final class FlowerItemData {
	private FlowerItemData() {}

	public static void collect(SpecialFlowerBlockEntity flower, DataComponentMap.Builder components, String key) {
		CompoundTag saved = flower.saveCustomOnly(flower.getLevel().registryAccess());
		CompoundTag retained = new CompoundTag();
		if (saved.contains(key)) {
			retained.put(key, saved.get(key).copy());
		}
		CompoundTag custom = new CompoundTag();
		custom.put("BlockEntityTag", retained);
		components.set(DataComponents.CUSTOM_DATA, CustomData.of(custom));
	}

	public static void apply(SpecialFlowerBlockEntity flower, CustomData data) {

		if (data != null && flower.getLevel() != null) {
			CompoundTag tag = data.copyTag();
			if (tag.contains("BlockEntityTag")) {
				flower.loadCustomOnly(tag.getCompound("BlockEntityTag"), flower.getLevel().registryAccess());
			}
		}
	}
}
