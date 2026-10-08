package io.github.lounode.extrabotany.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.lounode.extrabotany.common.item.relic.CameraItem;

@Mixin(ItemCooldowns.class)
public abstract class CameraCooldownMixin {
	@Inject(method = "onCooldownEnded", at = @At("HEAD"))
	private void extrabotany$cameraReady(Item item, CallbackInfo ci) {
		var player = Minecraft.getInstance().player;
		if (player != null && player.getCooldowns() == (Object) this) {
			CameraItem.onItemCooldownFinish(player, item);
		}
	}
}
