package io.github.lounode.extrabotany.forge.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import io.github.lounode.extrabotany.client.gui.HUD;
import io.github.lounode.extrabotany.network.ExtrabotanyPacket;
import io.github.lounode.extrabotany.network.clientbound.*;
import io.github.lounode.extrabotany.network.serverbound.*;

public final class ForgePacketHandler {
	public static void init(RegisterPayloadHandlersEvent event) {
		registerOperation();
		var registrar = event.registrar("1");
		registrar.playToServer(new CustomPacketPayload.Type<>(LeftClickPacketExcalibur.ID),
				ExtrabotanyPacket.codec(LeftClickPacketExcalibur::decode),
				(packet, context) -> packet.handle(context.player().getServer(), (ServerPlayer) context.player()));
		registrar.playToServer(new CustomPacketPayload.Type<>(LeftClickPacketJingwei.ID),
				ExtrabotanyPacket.codec(LeftClickPacketJingwei::decode),
				(packet, context) -> packet.handle(context.player().getServer(), (ServerPlayer) context.player()));
		registrar.playToServer(new CustomPacketPayload.Type<>(LeftClickPacketVoidArchives.ID),
				ExtrabotanyPacket.codec(LeftClickPacketVoidArchives::decode),
				(packet, context) -> packet.handle(context.player().getServer(), (ServerPlayer) context.player()));
		registrar.playToClient(new CustomPacketPayload.Type<>(ManaReaderPacket.ID),
				ExtrabotanyPacket.codec(ManaReaderPacket::decode), (packet, context) -> ManaReaderPacket.Handler.handle(packet));
		registrar.playToClient(new CustomPacketPayload.Type<>(SpawnGaiaPacket.ID),
				ExtrabotanyPacket.codec(SpawnGaiaPacket::decode), (packet, context) -> SpawnGaiaPacket.Handler.handle(packet));
		registrar.playToClient(new CustomPacketPayload.Type<>(ColorfulBossEventPacket.ID),
				ExtrabotanyPacket.codec(ColorfulBossEventPacket::decode),
				(packet, context) -> HUD.getInstance().getBossOverlay().update(packet));
	}

	private static void registerOperation() {
		ColorfulBossEventPacket.Operation.register("add", () -> ColorfulBossEventPacket.AddOperation.CODEC);
		ColorfulBossEventPacket.Operation.register("remove", () -> ColorfulBossEventPacket.RemoveOperation.CODEC);
		ColorfulBossEventPacket.Operation.register("update_progress", () -> ColorfulBossEventPacket.UpdateProgressOperation.CODEC);
		ColorfulBossEventPacket.Operation.register("update_name", () -> ColorfulBossEventPacket.UpdateNameOperation.CODEC);
		ColorfulBossEventPacket.Operation.register("update_style", () -> ColorfulBossEventPacket.UpdateStyleOperation.CODEC);
		ColorfulBossEventPacket.Operation.register("update_properties", () -> ColorfulBossEventPacket.UpdatePropertiesOperation.CODEC);
		ColorfulBossEventPacket.Operation.register("update_player_count", () -> GaiaBossEventPacket.UpdatePlayerCountOperation.CODEC);
		ColorfulBossEventPacket.Operation.register("update_grain_time", () -> GaiaBossEventPacket.UpdateGrainTimeOperation.CODEC);
	}

}
