package io.github.lounode.extrabotany.common.util;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.function.Consumer;

/** Updates legacy ExtraBotany item data through Minecraft's copy-on-write component API. */
public final class ItemNBTHelper {
	private ItemNBTHelper() {}

	private static CompoundTag data(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
	}

	public static void update(ItemStack stack, Consumer<CompoundTag> edit) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, edit);
	}

	public static boolean verifyExistance(ItemStack stack, String key) {
		return data(stack).contains(key);
	}

	public static void removeEntry(ItemStack stack, String key) {
		update(stack, tag -> tag.remove(key));
	}

	public static Tag get(ItemStack stack, String key) {
		return data(stack).get(key);
	}

	public static void set(ItemStack stack, String key, Tag value) {
		update(stack, tag -> tag.put(key, value.copy()));
	}

	public static int getInt(ItemStack stack, String key, int fallback) {
		var tag = data(stack);
		return tag.contains(key) ? tag.getInt(key) : fallback;
	}

	public static long getLong(ItemStack stack, String key, long fallback) {
		var tag = data(stack);
		return tag.contains(key) ? tag.getLong(key) : fallback;
	}

	public static boolean getBoolean(ItemStack stack, String key, boolean fallback) {
		var tag = data(stack);
		return tag.contains(key) ? tag.getBoolean(key) : fallback;
	}

	public static String getString(ItemStack stack, String key, String fallback) {
		var tag = data(stack);
		return tag.contains(key) ? tag.getString(key) : fallback;
	}

	public static ListTag getList(ItemStack stack, String key, int type, boolean nullIfAbsent) {
		var tag = data(stack);
		return nullIfAbsent && !tag.contains(key) ? null : tag.getList(key, type);
	}

	public static void setInt(ItemStack stack, String key, int value) {
		update(stack, tag -> tag.putInt(key, value));
	}

	public static void setLong(ItemStack stack, String key, long value) {
		update(stack, tag -> tag.putLong(key, value));
	}

	public static void setBoolean(ItemStack stack, String key, boolean value) {
		update(stack, tag -> tag.putBoolean(key, value));
	}

	public static void setString(ItemStack stack, String key, String value) {
		update(stack, tag -> tag.putString(key, value));
	}

	public static void setList(ItemStack stack, String key, ListTag value) {
		set(stack, key, value);
	}

	public static JsonObject serializeStack(ItemStack stack) {
		var ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
		return ItemStack.CODEC.encodeStart(ops, stack).getOrThrow().getAsJsonObject();
	}
}
