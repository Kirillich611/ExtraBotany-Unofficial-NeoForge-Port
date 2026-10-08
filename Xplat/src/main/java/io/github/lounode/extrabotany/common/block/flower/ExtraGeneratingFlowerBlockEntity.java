package io.github.lounode.extrabotany.common.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;

/** Keeps addon-specific flower state persistent and available to client HUDs. */
public abstract class ExtraGeneratingFlowerBlockEntity extends GeneratingFlowerBlockEntity {
	protected ExtraGeneratingFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();
		if (!getLevel().isClientSide()) {
			setChanged();
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);
		tag.merge(saveCustomOnly(registries));
		return tag;
	}
}
