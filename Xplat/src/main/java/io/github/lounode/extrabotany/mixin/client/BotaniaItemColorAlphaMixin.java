package io.github.lounode.extrabotany.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import vazkii.botania.client.render.ColorHandler;

/** Item tint colors require ARGB rather than RGB starting with Minecraft 1.21. */
@Mixin(ColorHandler.class)
public abstract class BotaniaItemColorAlphaMixin {
	@ModifyVariable(method = "submitItems", at = @At("HEAD"), argsOnly = true)
	private static ColorHandler.ItemHandlerConsumer extrabotany$opaqueItemColors(ColorHandler.ItemHandlerConsumer items) {
		return (handler, registeredItems) -> items.register((stack, tint) -> handler.getColor(stack, tint) | 0xFF000000, registeredItems);
	}
}
