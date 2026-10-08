package io.github.lounode.extrabotany.common.lib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class CommonItemTags {
	private CommonItemTags() {}

	public static final TagKey<Item> INGOTS_MANASTEEL = tag("ingots/manasteel");
	public static final TagKey<Item> INGOTS_TERRASTEEL = tag("ingots/terrasteel");
	public static final TagKey<Item> INGOTS_ELEMENTIUM = tag("ingots/elementium");
	public static final TagKey<Item> NUGGETS_ELEMENTIUM = tag("nuggets/elementium");
	public static final TagKey<Item> GEMS_DRAGONSTONE = tag("gems/dragonstone");
	public static final TagKey<Item> GEMS_MANA_DIAMOND = tag("gems/mana_diamond");
	public static final TagKey<Item> DUSTS_MANA = tag("dusts/mana");

	private static TagKey<Item> tag(String path) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
	}
}
