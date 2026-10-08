package io.github.lounode.extrabotany.common.item.equipment.bauble;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

import io.github.lounode.extrabotany.common.entity.AuraFireEntity;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.github.lounode.extrabotany.common.sounds.ExtraBotanySounds;
import io.github.lounode.extrabotany.common.util.SoundEventUtil;
import io.github.lounode.extrabotany.network.serverbound.LeftClickPacketJingwei;
import io.github.lounode.extrabotany.xplat.ExClientXplatAbstractions;

@EventBusSubscriber(modid = "extrabotany")
public class FeatherOfJingweiItem extends BaubleItem {
	public static final int MANA_PER_USE = 300;

	public FeatherOfJingweiItem(Properties props) {
		super(props);
	}

	@SubscribeEvent
	public static void leftClick(PlayerInteractEvent.LeftClickEmpty event) {
		Player player = event.getEntity();
		if (!EquipmentHandler.findOrEmpty(ExtraBotanyItems.featherOfJingwei, player).isEmpty()) {
			ExClientXplatAbstractions.INSTANCE.sendToServer(LeftClickPacketJingwei.INSTANCE);
		}
	}

	@SubscribeEvent
	public static void attackEntity(AttackEntityEvent event) {
		Player player = event.getEntity();
		if (!player.level().isClientSide) {
			trySpawnAuraFire(player);
		}

	}

	public static void trySpawnAuraFire(Player player) {
		trySpawnAuraFire(player, player.getAttackStrengthScale(0F));
	}

	public static void trySpawnAuraFire(Player player, float attackStrength) {
		if (attackStrength != 1 ||
				player.isSpectator() ||
				!player.getMainHandItem().isEmpty()) {
			return;
		}

		ItemStack jingwei = EquipmentHandler.findOrEmpty(ExtraBotanyItems.featherOfJingwei, player);
		if (jingwei.isEmpty()) {
			return;
		}
		if (!ManaItemHandler.instance().requestManaExactForTool(jingwei, player, MANA_PER_USE, true)) {
			return;
		}

		AuraFireEntity fire = getFire(player);
		player.level().addFreshEntity(fire);
		player.playNotifySound(ExtraBotanySounds.FEATHER_OF_JINGWEI_SHOOT, SoundSource.PLAYERS, 1f, SoundEventUtil.randomPitch(player.level()));
	}

	public static AuraFireEntity getFire(Player player) {
		AuraFireEntity fire = new AuraFireEntity(player);

		float motionModifier = 9F;

		fire.setGravity(0);
		fire.setDeltaMovement(fire.getDeltaMovement().scale(motionModifier));

		return fire;
	}
}
