package io.github.lounode.extrabotany.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.RadiusDescriptor;

import io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks;
import io.github.lounode.extrabotany.mixin.accessor.LightningBoltAccessor;
import io.github.lounode.extrabotany.xplat.ExtraBotanyConfig;

import java.util.List;

public class ReikarlilyBlockEntity extends io.github.lounode.extrabotany.common.block.flower.ExtraGeneratingFlowerBlockEntity {

	public static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_RESIDUAL_HEAT = "residualHeat";
	private static final String TAG_SPAWN_LIGHTNING_COOLDOWN = "spawnLightningCooldown";

	private static final int RANGE = 4;
	public static final int MAX_MANA = 12_000;
	public static final int COOLDOWN = 3600;
	public static final int PRODUCE_MANA = 12000;
	public static final int RESIDUAL_HEAT_AFTER_PRODUCE = 1500;
	public static final int RESIDUAL_HEAT_PRODUCE_MANA = 45;
	public static final int SPAWN_LIGHTNING_COOLDOWN = 3600;
	private static final int INITIAL_SPAWN_LIGHTNING_COOLDOWN = 3600;

	private int cooldown;
	private int residualHeat;
	private int spawnLightningCooldown;

	public ReikarlilyBlockEntity(BlockPos pos, BlockState state) {
		super(ExtrabotanyFlowerBlocks.REIKARLILY, pos, state);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		setSpawnLightningCooldown(INITIAL_SPAWN_LIGHTNING_COOLDOWN + getLevel().getRandom().nextInt(400));
	}

	@Override
	public void tickFlower() {
		super.tickFlower();
		if (getLevel().isClientSide()) {
			return;
		}
		if (getLevel().getGameTime() % 20 == 0) {
			markForImmediateSync();
		}

		if (getResidualHeat() > 0) {
			addMana(getResidualHeatProduceMana());
			setResidualHeat(getResidualHeat() - 1);
		}

		if (getSpawnLightningCooldown() > 0) {
			setSpawnLightningCooldown(getSpawnLightningCooldown() - 1);
		}

		if (getCooldown() > 0) {
			setCooldown(getCooldown() - 1);
			return;
		}

		if (getSpawnLightningCooldown() <= 0 && trySpawnLightningBolt()) {
			setSpawnLightningCooldown(getAfterSpawnLightningCooldown());
		}

		if (resolveActiveProduce()) {
			setCooldown(getAfterWorkCooldown());
			setResidualHeat(getResidualHeatAfterProduce());
			addMana(getProduceMana());
		}
	}

	public boolean trySpawnLightningBolt() {
		if (!getLevel().isRainingAt(getEffectivePos())) {
			return false;
		}

		LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(getLevel());
		double xOffset = (getLevel().getRandom().nextDouble() - 0.5) * 0.5; // [-0.25, 0.25]
		double zOffset = (getLevel().getRandom().nextDouble() - 0.5) * 0.5; // [-0.25, 0.25]
		lightningBolt.moveTo(
				getEffectivePos().getX() + xOffset,
				getEffectivePos().getY() + 1,
				getEffectivePos().getZ() + zOffset
		);
		getLevel().addFreshEntity(lightningBolt);
		return true;
	}

	public boolean resolveActiveProduce() {
		boolean produce = false;

		List<LightningBolt> lightnings = getLevel().getEntitiesOfClass(LightningBolt.class, new AABB(getEffectivePos()).inflate(RANGE));

		for (var lightning : lightnings) {
			if (getMana() >= getMaxMana()) {
				continue;
			}
			if (((LightningBoltAccessor) lightning).isVisualOnly()) {
				continue;
			}

			if (lightning.isAlive()) {
				lightning.setVisualOnly(true);
				produce = true;
				break;
			}
		}
		return produce;
	}

	public int getAfterWorkCooldown() {
		return ExtraBotanyConfig.common().reikarlilyProduceCooldown();
	}

	public int getAfterSpawnLightningCooldown() {
		return ExtraBotanyConfig.common().reikarlilySpawnLightningCooldown();
	}

	public int getProduceMana() {
		return ExtraBotanyConfig.common().reikarlilyProduceMana();
	}

	public int getResidualHeatAfterProduce() {
		return ExtraBotanyConfig.common().reikarlilyPassiveGenerateTime();
	}

	public int getResidualHeatProduceMana() {
		return ExtraBotanyConfig.common().reikarlilyPassiveGenerateMana();
	}

	@Override
	public int getMaxMana() {
		return ExtraBotanyConfig.common().reikarlilyMaxMana();
	}

	@Override
	public int getColor() {
		return 0x0000CD;
	}

	@Override
	public void saveAdditional(CompoundTag cmp, net.minecraft.core.HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);

		cmp.putInt(TAG_COOLDOWN, getCooldown());
		cmp.putInt(TAG_RESIDUAL_HEAT, getResidualHeat());
		cmp.putInt(TAG_SPAWN_LIGHTNING_COOLDOWN, getSpawnLightningCooldown());
	}

	@Override
	public void loadAdditional(CompoundTag cmp, net.minecraft.core.HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);

		setCooldown(cmp.getInt(TAG_COOLDOWN));
		setResidualHeat(cmp.getInt(TAG_RESIDUAL_HEAT));
		setSpawnLightningCooldown(cmp.getInt(TAG_SPAWN_LIGHTNING_COOLDOWN));
	}

	@Override
	@Nullable
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getResidualHeat() {
		return residualHeat;
	}

	public void setResidualHeat(int heat) {
		this.residualHeat = heat;
	}

	public int getSpawnLightningCooldown() {
		return spawnLightningCooldown;
	}

	public void setSpawnLightningCooldown(int cooldown) {
		this.spawnLightningCooldown = cooldown;
	}

	@Override
	protected void collectImplicitComponents(net.minecraft.core.component.DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		io.github.lounode.extrabotany.common.block.flower.FlowerItemData.collect(this, components, TAG_COOLDOWN);
	}

	@Override
	protected void applyImplicitComponents(net.minecraft.world.level.block.entity.BlockEntity.DataComponentInput components) {
		super.applyImplicitComponents(components);
		io.github.lounode.extrabotany.common.block.flower.FlowerItemData.apply(this, components.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA));
	}
}
