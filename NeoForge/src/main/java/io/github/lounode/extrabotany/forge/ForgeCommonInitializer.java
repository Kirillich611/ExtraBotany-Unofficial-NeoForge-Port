package io.github.lounode.extrabotany.forge;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import org.slf4j.Logger;

import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.neoforge.BotaniaNeoForgeCapabilities;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.neoforge.integration.curios.CurioIntegration;

import io.github.lounode.extrabotany.api.ExtraBotaniaRegistries;
import io.github.lounode.extrabotany.api.ExtrabotanyForgeCapabilities;
import io.github.lounode.extrabotany.api.item.NatureEnergyItem;
import io.github.lounode.extrabotany.common.advancements.ExtrabotanyCriteriaTriggers;
import io.github.lounode.extrabotany.common.block.ExtraBotanyBlocks;
import io.github.lounode.extrabotany.common.block.block_entity.ExtraBotanyBlockEntities;
import io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks;
import io.github.lounode.extrabotany.common.brew.ExtraBotanyBrews;
import io.github.lounode.extrabotany.common.brew.ExtraBotanyMobEffects;
import io.github.lounode.extrabotany.common.crafting.ExtraBotanyRecipeTypes;
import io.github.lounode.extrabotany.common.entity.ExtraBotanyEntityType;
import io.github.lounode.extrabotany.common.entity.ExtraBotanyMemoryType;
import io.github.lounode.extrabotany.common.impl.WindImpl;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.github.lounode.extrabotany.common.item.brew.InfiniteWineItem;
import io.github.lounode.extrabotany.common.item.equipment.bauble.NatureOrbItem;
import io.github.lounode.extrabotany.common.item.equipment.tool.hammer.RheinHammerItem;
import io.github.lounode.extrabotany.common.item.material.ArmorsMaterial;
import io.github.lounode.extrabotany.common.item.relic.*;
import io.github.lounode.extrabotany.common.item.relic.void_archives.VoidArchivesItem;
import io.github.lounode.extrabotany.common.item.relic.voidcore.CoreOfTheVoidItem;
import io.github.lounode.extrabotany.common.lib.LibMisc;
import io.github.lounode.extrabotany.common.loot.RewardBagManager;
import io.github.lounode.extrabotany.common.sounds.ExtraBotanySounds;
import io.github.lounode.extrabotany.forge.network.ForgePacketHandler;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(LibMisc.MOD_ID)
public class ForgeCommonInitializer {
	private static final Logger LOGGER = LogUtils.getLogger();

	public ForgeCommonInitializer(IEventBus modBus, ModContainer context) {

		coreInit(context);
		registryInit(modBus);
		modBus.addListener(this::commonSetup);
		modBus.addListener(ForgePacketHandler::init);
		modBus.addListener(this::attachItemCaps);
	}

	public void commonSetup(FMLCommonSetupEvent evt) {

		registerEvents();

		evt.enqueueWork(() -> {
			BiConsumer<ResourceLocation, Supplier<? extends Block>> consumer = (resourceLocation, blockSupplier) -> ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(resourceLocation, blockSupplier);
			ExtraBotanyBlocks.registerFlowerPotPlants(consumer);
			ExtrabotanyFlowerBlocks.registerFlowerPotPlants(consumer);
		});

		//Integration
		if (ModList.get().isLoaded("tconstruc")) {
			//modEventBus.addListener(this::registerTinkersMaterials);
		}
	}

	private void coreInit(ModContainer context) {
		ForgeExtrabotanyConfig.setup(context);
	}

	private void registryInit(IEventBus modEventBus) {
		bind(modEventBus, Registries.SOUND_EVENT, ExtraBotanySounds::init);
		modEventBus.addListener((RegisterEvent event) -> {
			if (event.getRegistryKey().equals(Registries.ARMOR_MATERIAL)) {
				ArmorsMaterial.registerMaterials(event.getRegistry(Registries.ARMOR_MATERIAL));
			}
			if (event.getRegistryKey().equals(Registries.MOB_EFFECT)) {
				ExtraBotanyMobEffects.registerPotions(event.getRegistry(Registries.MOB_EFFECT));
			}
		});
		//Block&ItemBlock&Items
		bind(modEventBus, Registries.BLOCK, ExtraBotanyBlocks::registerBlocks);
		bindForItems(modEventBus, ExtraBotanyBlocks::registerItemBlocks);
		bind(modEventBus, Registries.BLOCK_ENTITY_TYPE, ExtraBotanyBlockEntities::registerTiles);
		bindForItems(modEventBus, ExtraBotanyItems::registerItems);

		bind(modEventBus, Registries.BLOCK, ExtrabotanyFlowerBlocks::registerBlocks);
		bindForItems(modEventBus, ExtrabotanyFlowerBlocks::registerItemBlocks);
		bind(modEventBus, Registries.BLOCK_ENTITY_TYPE, ExtrabotanyFlowerBlocks::registerTEs);

		//GUI & Recipe
		bind(modEventBus, Registries.RECIPE_SERIALIZER, ExtraBotanyItems::registerRecipeSerializers);
		bind(modEventBus, Registries.RECIPE_TYPE, ExtraBotanyRecipeTypes::submitRecipeTypes);
		bind(modEventBus, Registries.RECIPE_SERIALIZER, ExtraBotanyRecipeTypes::submitRecipeSerializers);

		// Entities
		bind(modEventBus, Registries.ENTITY_TYPE, ExtraBotanyEntityType::registerEntities);
		modEventBus.addListener((EntityAttributeCreationEvent e) -> ExtraBotanyEntityType.registerAttributes((type, builder) -> e.put(type, builder.build())));
		bind(modEventBus, Registries.MEMORY_MODULE_TYPE, ExtraBotanyMemoryType::registerMemories);

		// Potions

		bind(modEventBus, BotaniaRegistries.BREWS, ExtraBotanyBrews::submitRegistrations);

		// Rest
		//registerDatas(modEventBus);

		bind(modEventBus, Registries.TRIGGER_TYPE, consumer -> ExtrabotanyCriteriaTriggers.init((id, trigger) -> consumer.accept(trigger, id)));

		//Creative tab
		bind(modEventBus, Registries.CREATIVE_MODE_TAB, consumer -> {
			consumer.accept(CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.extrabotany").withStyle(style -> style.withColor(ChatFormatting.WHITE)))
					.hideTitle()
					.icon(() -> new ItemStack(ExtraBotanyItems.zadkiel))
					//.withTabsBefore(CreativeModeTabs.NATURAL_BLOCKS)
					.backgroundTexture(ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tab_extrabotany.png"))
					//.withSearchBar()
					.build(),
					ExtraBotaniaRegistries.EXTRA_BOTANIA_TAB_KEY.location());
		});

		modEventBus.addListener((BuildCreativeModeTabContentsEvent e) -> {
			if (e.getTabKey() == ExtraBotaniaRegistries.EXTRA_BOTANIA_TAB_KEY) {
				for (Item item : this.itemsToAddToCreativeTab) {
					if (item instanceof CustomCreativeTabContents cc) {
						cc.addToCreativeTab(item, e);
					} else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
						cc.addToCreativeTab(item, e);
					} else {
						e.accept(item);
					}
				}
			}
		});

	}

	private void registerEvents() {
		NeoForge.EVENT_BUS.addListener((LevelTickEvent.Post event) -> WindImpl.EventHandler.onLevelTick(event.getLevel()));
		RewardBagManager.registerListener();
	}

	private void attachItemCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
				ExtraBotanyBlockEntities.PEDESTAL, net.neoforged.neoforge.items.wrapper.SidedInvWrapper::new);
		event.registerBlockEntity(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
				ExtraBotanyBlockEntities.MANA_CHARGER, net.neoforged.neoforge.items.wrapper.SidedInvWrapper::new);
		event.registerBlockEntity(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
				ExtraBotanyBlockEntities.POWER_FRAME, net.neoforged.neoforge.items.wrapper.SidedInvWrapper::new);
		if (EquipmentHandler.instance instanceof CurioIntegration ci) {
			ci.initCapability(event, itemsToAddToCreativeTab.stream().filter(item -> item instanceof BaubleItem).toArray(Item[]::new));
		}
		MANA_ITEM.get().forEach((item, factory) -> event.registerItem(BotaniaNeoForgeCapabilities.getItemApiLookupById(vazkii.botania.api.mana.ManaItem.LOOKUP), (stack, context) -> factory.apply(stack), item));
		NATURE_ENERGY_ITEM.get().forEach((item, factory) -> event.registerItem(ExtrabotanyForgeCapabilities.NATURE_ENERGY_ITEM, (stack, context) -> factory.apply(stack), item));
		RELIC.get().forEach((item, factory) -> event.registerItem(BotaniaNeoForgeCapabilities.getItemApiLookupById(vazkii.botania.api.item.Relic.LOOKUP), (stack, context) -> factory.apply(stack), item));
	}

	private static final Supplier<Map<Item, Function<ItemStack, NatureEnergyItem>>> NATURE_ENERGY_ITEM = Suppliers.memoize(() -> Map.of(
			ExtraBotanyItems.natureOrb, NatureOrbItem.NatureEnergyImpl::new
	));

	private static final Supplier<Map<Item, Function<ItemStack, ManaItem>>> MANA_ITEM = Suppliers.memoize(() -> Map.of(
			ExtraBotanyItems.manaRingMaster, MasterBandOfManaItem.ExtendManaItemImpl::new
	));
	private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
			ExtraBotanyItems.manaRingMaster, MasterBandOfManaItem::makeRelic,
			ExtraBotanyItems.camera, CameraItem::makeRelic,
			ExtraBotanyItems.failnaught, FailnaughtItem::makeRelic,
			ExtraBotanyItems.excalibur, ExcaliburItem::makeRelic,
			ExtraBotanyItems.coreOfTheVoid, CoreOfTheVoidItem::makeRelic,
			ExtraBotanyItems.pandorasBox, PandorasBoxItem::makeRelic,
			ExtraBotanyItems.infiniteWine, InfiniteWineItem::makeRelic,
			ExtraBotanyItems.voidArchives, VoidArchivesItem::makeRelic,
			ExtraBotanyItems.rheinHammer, RheinHammerItem::makeRelic,
			ExtraBotanyItems.achillesShield, AchillesShieldItem::makeRelic
	));

	private static <T> void bind(IEventBus modEventBus, ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		modEventBus.addListener((RegisterEvent event) -> {
			if (registry.equals(event.getRegistryKey())) {
				source.accept((t, rl) -> event.register(registry, rl, () -> t));
			}
		});
	}

	private final Set<Item> itemsToAddToCreativeTab = new LinkedHashSet<>();

	private void bindForItems(IEventBus modEventBus, Consumer<BiConsumer<Item, ResourceLocation>> source) {
		modEventBus.addListener((RegisterEvent event) -> {
			if (event.getRegistryKey().equals(Registries.ITEM)) {
				source.accept((t, rl) -> {
					itemsToAddToCreativeTab.add(t);
					event.register(Registries.ITEM, rl, () -> t);
				});
			}
		});
	}
}
