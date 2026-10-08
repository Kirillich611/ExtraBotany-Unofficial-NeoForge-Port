package io.github.lounode.extrabotany.common.integration.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;

import io.github.lounode.extrabotany.common.integration.kubejs.schema.EdelweissSchema;
import io.github.lounode.extrabotany.common.integration.kubejs.schema.NaturePedestalSchema;
import io.github.lounode.extrabotany.common.integration.kubejs.schema.OmnivioletSchema;
import io.github.lounode.extrabotany.common.integration.kubejs.schema.StonesiaSchema;
import io.github.lounode.extrabotany.common.lib.LibMisc;

public class KubeJSExtrabotanyPlugin implements KubeJSPlugin {
	@Override
	public void registerRecipeComponents(dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry registry) {
		registry.register(io.github.lounode.extrabotany.common.integration.kubejs.util.BlockStateComponent.TYPE);
	}

	@Override
	public void registerRecipeSchemas(RecipeSchemaRegistry event) {
		event.namespace(LibMisc.MOD_ID)
				.register("pedestal_smash", NaturePedestalSchema.SCHEMA)
				.register("stonesia", StonesiaSchema.SCHEMA)
				.register("omniviolet", OmnivioletSchema.SCHEMA)
				.register("edelweiss", EdelweissSchema.SCHEMA);

	}
}
