package io.github.lounode.extrabotany.forge.data;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import io.github.lounode.extrabotany.common.lib.LibMisc;
import io.github.lounode.extrabotany.data.*;
import io.github.lounode.extrabotany.data.loot.*;
import io.github.lounode.extrabotany.data.recipes.*;
import io.github.lounode.extrabotany.data.tags.*;

import java.util.List;
import java.util.Set;

import static io.github.lounode.extrabotany.common.ExtraBotanyDamageTypes.*;

@EventBusSubscriber(modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ForgeDatagenInitializer {
	@SubscribeEvent
	public static void configureForgeDatagen(GatherDataEvent event) {
		var generator = event.getGenerator();
		var output = generator.getPackOutput();
		var registries = new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(),
				new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, context -> {
					context.register(LINK_DAMAGE, LINK);
					context.register(EXCALIBUR_BEAM_DAMAGE, EXCALIBUR);
					context.register(JINGWEI_PUNCH_DAMAGE, JINGWEI);
					context.register(REVERSE_HEAL_DAMAGE, REVERSE_HEAL);
					context.register(BACKFIRE_DAMAGE, BACKFIRE);
				}), Set.of(LibMisc.MOD_ID));
		generator.addProvider(event.includeServer(), registries);
		var lookup = registries.getRegistryProvider();
		var blocks = new ForgeBlockTagProvider(output, lookup, event.getExistingFileHelper());
		generator.addProvider(event.includeServer(), blocks);
		generator.addProvider(event.includeServer(), new ForgeItemTagProvider(output, lookup, blocks.contentsGetter(), event.getExistingFileHelper()));
		generator.addProvider(event.includeServer(), new EntityTypeTagProvider(output, lookup));
		generator.addProvider(event.includeServer(), new DamageTypeTagProvider(output, lookup));
		generator.addProvider(event.includeServer(), new BlockLootProvider(output));
		generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(), List.of(
				new LootTableProvider.SubProviderEntry(provider -> new EntityLootProvider(), LootContextParamSets.ENTITY),
				new LootTableProvider.SubProviderEntry(provider -> new RewardBagLootProvider(), LootContextParamSets.EMPTY)), lookup));

		generator.addProvider(event.includeServer(), new AllRecipesProvider(output, lookup));
		generator.addProvider(event.includeServer(), AdvancementProvider.create(output, lookup));
		generator.addProvider(event.includeClient(), new BlockstateProvider(output));
		generator.addProvider(event.includeClient(), new FloatingFlowerModelProvider(output));
		generator.addProvider(event.includeClient(), new ItemModelProvider(output));
		generator.addProvider(event.includeClient(), new PottedPlantModelProvider(output));
		generator.addProvider(event.includeClient(), new SoundProvider(output, LibMisc.MOD_ID));
		generator.addProvider(event.includeClient(), new PatchouliBookProvider(output));
	}
}
