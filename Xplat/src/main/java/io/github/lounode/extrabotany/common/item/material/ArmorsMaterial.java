package io.github.lounode.extrabotany.common.item.material;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.botania.common.item.BotaniaItems;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.github.lounode.extrabotany.common.sounds.ExtraBotanySounds;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ArmorsMaterial implements StringRepresentable {
	STARRY_IDOL("starry_idol", 16, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
		map.put(ArmorItem.Type.BOOTS, 2);
		map.put(ArmorItem.Type.LEGGINGS, 5);
		map.put(ArmorItem.Type.CHESTPLATE, 6);
		map.put(ArmorItem.Type.HELMET, 2);
	}), 18, ExtraBotanySounds.ARMOR_EQUIP_IDOL, 0, 0.0F,
			() -> Ingredient.of(BotaniaItems.MANAWEAVE_CLOTH)),
	PLEIADS_MAID_COMBAT("pleiads_maid_combat", 50, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
		map.put(ArmorItem.Type.BOOTS, 4);
		map.put(ArmorItem.Type.LEGGINGS, 7);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 3);
	}), 50, ExtraBotanySounds.ARMOR_EQUIP_MAID, 4F, 0.0F,
			() -> Ingredient.of(ExtraBotanyItems.dasRheingold)),
	GOBLIN_SLAYER("goblin_slayer", 21, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 6);
		map.put(ArmorItem.Type.HELMET, 3);
	}), 40, ExtraBotanySounds.ARMOR_EQUIP_GOBLIN, 2.5F, 0.0F,
			() -> Ingredient.of(ExtraBotanyItems.photonium)),
	SHADOW_WARRIOR("shadow_warrior", 23, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
		map.put(ArmorItem.Type.BOOTS, 3);
		map.put(ArmorItem.Type.LEGGINGS, 7);
		map.put(ArmorItem.Type.CHESTPLATE, 6);
		map.put(ArmorItem.Type.HELMET, 3);
	}), 28, ExtraBotanySounds.ARMOR_EQUIP_WARRIOR, 1.5F, 0.0F,
			() -> Ingredient.of(ExtraBotanyItems.shadowium));

	public net.minecraft.core.Holder<ArmorMaterial> holder() {
		return material;
	}

	public static void registerMaterials(net.minecraft.core.Registry<ArmorMaterial> registry) {
		for (var material : values()) {
			material.material.register(registry);
		}
	}

	public static int durability(net.minecraft.core.Holder<ArmorMaterial> holder, ArmorItem.Type type) {
		for (var material : values()) {
			if (holder.value() == material.holder().value()) {
				return material.getDurabilityForType(type);
			}
		}
		return type.getDurability(33);
	}

	public static final StringRepresentable.EnumCodec<ArmorsMaterial> CODEC = StringRepresentable.fromEnum(ArmorsMaterial::values);
	private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
		map.put(ArmorItem.Type.BOOTS, 13);
		map.put(ArmorItem.Type.LEGGINGS, 15);
		map.put(ArmorItem.Type.CHESTPLATE, 16);
		map.put(ArmorItem.Type.HELMET, 11);
	});

	private final vazkii.botania.common.helper.RegistryHelper.HolderProxy<ArmorMaterial> material;
	private final String name;
	private final int durabilityMultiplier;
	private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	ArmorsMaterial(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionFunctionForType = protectionFunctionForType;
		this.enchantmentValue = enchantmentValue;
		this.sound = sound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
		this.material = vazkii.botania.common.helper.RegistryHelper.holderProxy(net.minecraft.core.registries.Registries.ARMOR_MATERIAL, io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix(name), new ArmorMaterial(protectionFunctionForType, enchantmentValue, net.minecraft.core.Holder.direct(sound), repairIngredient, java.util.List.of(new ArmorMaterial.Layer(io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix(name))), toughness, knockbackResistance));
	}

	public int getDurabilityForType(ArmorItem.Type type) {
		return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
	}

	public int getDefenseForType(ArmorItem.Type type) {
		return this.protectionFunctionForType.get(type);
	}

	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	public SoundEvent getEquipSound() {
		return this.sound;
	}

	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	public String getName() {
		return this.name;
	}

	public float getToughness() {
		return this.toughness;
	}

	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
