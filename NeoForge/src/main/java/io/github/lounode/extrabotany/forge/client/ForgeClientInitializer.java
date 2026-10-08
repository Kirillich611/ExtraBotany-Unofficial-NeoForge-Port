package io.github.lounode.extrabotany.forge.client;

import com.google.common.base.Suppliers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.neoforge.BotaniaNeoForgeCapabilities;
import vazkii.patchouli.api.PatchouliAPI;

import io.github.lounode.extrabotany.client.ExtraBotanyItemProperties;
import io.github.lounode.extrabotany.client.core.ExtraBotanyModels;
import io.github.lounode.extrabotany.client.gui.HUD;
import io.github.lounode.extrabotany.client.model.ExtrabotanyLayerDefinitions;
import io.github.lounode.extrabotany.client.renderer.BlockRenderLayers;
import io.github.lounode.extrabotany.client.renderer.ColorHandler;
import io.github.lounode.extrabotany.client.renderer.entity.EntityRenderers;
import io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks;
import io.github.lounode.extrabotany.common.lib.LibMisc;
import io.github.lounode.extrabotany.xplat.ExtraBotanyConfig;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientInitializer {
	public static HUD hud;

	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent evt) {
		BlockRenderLayers.skipPlatformBlocks = true; // platforms can use standard rendering on Forge
		BlockRenderLayers.init(ItemBlockRenderTypes::setRenderLayer);

		var bus = NeoForge.EVENT_BUS;
		hud = new HUD(Minecraft.getInstance());

		PatchouliAPI.get().setConfigFlag("otaku_mode", ExtraBotanyConfig.client().otakuMode());
		/*
		bus.addListener((CustomizeGuiOverlayEvent.BossEventProgress e) -> {
			var result = BossBarHandler.onBarRender(e.getGuiGraphics(), e.getX(), e.getY(),
					e.getBossEvent(), true);
			result.ifPresent(increment -> {
				e.setCanceled(true);
				e.setIncrement(increment);
			});
		});
		
		//MasterRingToolTip TODO
		
		bus.addListener(EventPriority.LOWEST, (RenderTooltipEvent.Color e) -> {
			var manaItem = vazkii.botania.api.mana.ManaItem.LOOKUP.find(e.getItemStack());
			if (manaItem == null) {
				return;
			}
			// Forge does not pass the tooltip width to any tooltip event.
			// To avoid a mixin here, we just duplicate the width checking part.
			int width = 0;
			MasterBandOfManaTooltipComponent manaBar = null;
			for (ClientTooltipComponent component : e.getComponents()) {
				width = Math.max(width, component.getWidth(e.getFont()));
				if (component instanceof MasterBandOfManaTooltipComponent c) {
					manaBar = c;
				}
			}
			if (manaBar != null) {
				manaBar.setContext(e.getX(), e.getY(), width);
			}
		});
		
		*/

		bus.addListener((ClientPlayerNetworkEvent.LoggingOut event) -> HUD.onDisconnected());
	}

	private static final Supplier<Map<BlockEntityType<?>, Function<BlockEntity, WandHUD>>> WAND_HUD = Suppliers.memoize(() -> {
		var ret = new IdentityHashMap<BlockEntityType<?>, Function<BlockEntity, WandHUD>>();
		ExtrabotanyFlowerBlocks.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});

		return Collections.unmodifiableMap(ret);
	});

	@SubscribeEvent
	private static void attachBeCapabilities(RegisterCapabilitiesEvent event) {
		WAND_HUD.get().forEach((type, factory) -> event.registerBlockEntity(
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(WandHUD.BLOCK_LOOKUP), type, (be, context) -> factory.apply(be)));
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiLayersEvent e) {
		e.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, io.github.lounode.extrabotany.common.lib.ResourceLocationHelper.prefix("hud"),
				(graphics, delta) -> hud.onDrawScreenPost(graphics, delta.getGameTimeDeltaPartialTick(false)));
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
		ExtrabotanyLayerDefinitions.init(evt::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		EntityRenderers.registerBlockEntityRenderers(evt::registerBlockEntityRenderer);
		EntityRenderers.registerEntityRenderers(evt::registerEntityRenderer);
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block evt) {
		ColorHandler.submitBlocks(evt::register);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item evt) {
		ColorHandler.submitItems(evt::register);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelEvent.RegisterAdditional evt) {
		var resourceManager = Minecraft.getInstance().getResourceManager();
		ExtraBotanyModels.INSTANCE.onModelRegister(resourceManager, id -> evt.register(ModelResourceLocation.standalone(id)));
		ExtraBotanyItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));
	}

	@SubscribeEvent
	public static void onModelBake(ModelEvent.ModifyBakingResult evt) {
		ExtraBotanyModels.INSTANCE.onModelBake(evt.getModelBakery(), evt.getModels());
	}
}
