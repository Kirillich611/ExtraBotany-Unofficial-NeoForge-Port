package io.github.lounode.extrabotany.common.util;

import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttributeUtil {
	public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = Util.make(new DecimalFormat("#.##"), (format) -> {
		format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
	});

	public static net.minecraft.resources.ResourceLocation modifierId(String name) {
		return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("extrabotany",
				"attribute/" + name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_/.-]", "_"));
	}

	public static void addAttributeModifier(ItemStack stack, net.minecraft.core.Holder<Attribute> attribute, AttributeModifier modifier, @Nullable EquipmentSlot slot) {
		var modifiers = stack.getOrDefault(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS,
				stack.getItem().getDefaultAttributeModifiers());
		stack.set(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, modifiers.withModifierAdded(
				attribute, modifier, slot == null ? net.minecraft.world.entity.EquipmentSlotGroup.ANY
						: net.minecraft.world.entity.EquipmentSlotGroup.bySlot(slot)));
	}

	public static void removeAttributeModifier(ItemStack stack, String name) {
		var modifiers = stack.get(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS);
		if (modifiers == null) {
			return;
		}
		var id = modifierId(name);
		stack.set(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS,
				new net.minecraft.world.item.component.ItemAttributeModifiers(modifiers.modifiers().stream()
						.filter(entry -> !entry.modifier().id().equals(id)).toList(), modifiers.showInTooltip()));
	}

	public static Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> forSlot(net.minecraft.world.item.component.ItemAttributeModifiers modifiers, EquipmentSlot slot) {
		Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> result = com.google.common.collect.HashMultimap.create();
		modifiers.forEach(slot, result::put);
		return result;
	}

	public static net.minecraft.world.item.component.ItemAttributeModifiers fromSlots(java.util.function.Function<EquipmentSlot, Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier>> modifiers) {
		var builder = net.minecraft.world.item.component.ItemAttributeModifiers.builder();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			for (var entry : modifiers.apply(slot).entries()) {
				builder.add(entry.getKey(), entry.getValue(), net.minecraft.world.entity.EquipmentSlotGroup.bySlot(slot));
			}
		}
		return builder.build();
	}

	public static List<Component> getTooltips(Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> multimap) {
		List<Component> list = new ArrayList<>();
		for (Map.Entry<net.minecraft.core.Holder<Attribute>, AttributeModifier> entry : multimap.entries()) {
			AttributeModifier attributemodifier = entry.getValue();
			double d0 = attributemodifier.amount();
			boolean flag = false;

			double d1;
			if (attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
				if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
					d1 = d0 * 10.0D;
				} else {
					d1 = d0;
				}
			} else {
				d1 = d0 * 100.0D;
			}

			if (d0 > 0.0D) {
				list.add(Component.translatable("attribute.modifier.plus." + attributemodifier.operation().id(), ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().value().getDescriptionId())).withStyle(ChatFormatting.BLUE));
			} else if (d0 < 0.0D) {
				d1 *= -1.0D;
				list.add(Component.translatable("attribute.modifier.take." + attributemodifier.operation().id(), ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().value().getDescriptionId())).withStyle(ChatFormatting.RED));
			}
		}

		return list;
	}
}
