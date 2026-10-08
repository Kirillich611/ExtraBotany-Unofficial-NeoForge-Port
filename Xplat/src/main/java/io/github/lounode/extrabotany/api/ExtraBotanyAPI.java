package io.github.lounode.extrabotany.api;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vazkii.botania.api.ServiceUtil;

import io.github.lounode.extrabotany.api.item.CoreOfTheVoidVariant;
import io.github.lounode.extrabotany.api.item.VoidArchivesVariant;

import java.util.HashMap;
import java.util.Map;

public interface ExtraBotanyAPI {
	String MODID = "extrabotany";
	Logger LOGGER = LoggerFactory.getLogger(MODID);

	ExtraBotanyAPI INSTANCE = ServiceUtil.findService(ExtraBotanyAPI.class, () -> new ExtraBotanyAPI() {});

	net.minecraft.core.Holder<ArmorMaterial> DUMMY_ARMOR_MATERIAL = net.minecraft.core.Holder.direct(new ArmorMaterial(
			Map.of(), 0, SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.EMPTY, java.util.List.of(), 0, 0));

	Tier DUMMY_ITEM_TIER = new Tier() {
		@Override
		public int getUses() {
			return 0;
		}

		@Override
		public float getSpeed() {
			return 0;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@Override
		public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() {
			return net.minecraft.tags.BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 0;
		}

		@NotNull
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.EMPTY;
		}
	};

	static ExtraBotanyAPI instance() {
		return INSTANCE;
	}

	default void registerCOVVariant(CoreOfTheVoidVariant variant) {};

	default void registerVoidArchivesVariant(VoidArchivesVariant variant) {};

	default Map<String, CoreOfTheVoidVariant> getCOVVariants() {
		return new HashMap<>();
	}

	default Map<String, VoidArchivesVariant> getVoidArchivesVariants() {
		return new HashMap<>();
	}

	default net.minecraft.core.Holder<ArmorMaterial> getPleiadsMaidCombatArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default net.minecraft.core.Holder<ArmorMaterial> getStarryIdolArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default net.minecraft.core.Holder<ArmorMaterial> getGoblinSlayerArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default net.minecraft.core.Holder<ArmorMaterial> getShadowWarriorArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}
}
