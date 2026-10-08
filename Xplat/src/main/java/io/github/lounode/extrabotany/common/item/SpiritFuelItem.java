package io.github.lounode.extrabotany.common.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

@EventBusSubscriber(modid = "extrabotany")
public class SpiritFuelItem extends Item {
	public SpiritFuelItem(Properties properties) {
		super(properties);
	}

	@SubscribeEvent
	public static void makeFuel(FurnaceFuelBurnTimeEvent wrapper) {
		if (wrapper.getItemStack().is(ExtraBotanyItems.spiritFuel)) {
			wrapper.setBurnTime(12800);
		}
	}
}
