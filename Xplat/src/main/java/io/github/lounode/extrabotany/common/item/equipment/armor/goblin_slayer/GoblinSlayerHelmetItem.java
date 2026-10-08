package io.github.lounode.extrabotany.common.item.equipment.armor.goblin_slayer;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.github.lounode.extrabotany.common.lib.ExtraBotanyTags;

public class GoblinSlayerHelmetItem extends GoblinSlayerArmorItem {

	private static final float UNDEAD_DAMAGE_BONUS = 0.5F;

	public GoblinSlayerHelmetItem(Properties properties) {
		super(Type.HELMET, properties);
	}

	@EventBusSubscriber(modid = "extrabotany")
	public static class EventHandler {

		@SubscribeEvent
		public static void onPlayerAttack(LivingIncomingDamageEvent event) {
			if (!(event.getSource().getEntity() instanceof Player player)) {
				return;
			}
			ItemStack armorStack = player.getItemBySlot(EquipmentSlot.HEAD);
			if (!armorStack.is(ExtraBotanyItems.goblinSlayerHelmet)) {
				return;
			}
			if (!(armorStack.getItem() instanceof GoblinSlayerArmorItem suit)) {
				return;
			}
			if (!suit.hasArmorSet(player)) {
				return;
			}

			if (event.getEntity().getType().is(EntityTypeTags.UNDEAD)) {
				float origin = event.getAmount();
				event.setAmount(origin * (1 + UNDEAD_DAMAGE_BONUS));
			}

			if (event.getEntity().getType().is(ExtraBotanyTags.Entities.GOBLINS)) {
				event.setAmount(Integer.MAX_VALUE);
			}
		}
	}
}
