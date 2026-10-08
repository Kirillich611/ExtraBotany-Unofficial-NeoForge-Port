package io.github.lounode.extrabotany.common.advancements;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class ExtrabotanyCriteriaTriggers {
	public static void init(BiConsumer<ResourceLocation, CriterionTrigger<?>> register) {
		register.accept(ItemUsedTrigger.ID, ItemUsedTrigger.INSTANCE);
		register.accept(ManaChargeTrigger.ID, ManaChargeTrigger.INSTANCE);
		register.accept(HasArmorSetTrigger.ID, HasArmorSetTrigger.INSTANCE);
	}
}
