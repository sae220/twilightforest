package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import twilightforest.block.*;
import twilightforest.block.entity.CandelabraBlockEntity;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.init.TFBlocks;

public class CandleDispenseBehavior extends OptionalDispenseItemBehavior {

	public CandleDispenseBehavior() {
	}

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		if (!level.isClientSide()) {
			BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
			this.setSuccess(tryAddCandle(level, blockpos, stack.getItem()) || tryCreateSkullCandle(level, blockpos, stack.getItem()));
			if (this.isSuccess()) {
				stack.shrink(1);
			}
		}

		return stack;
	}

	private static boolean tryAddCandle(ServerLevel level, BlockPos pos, Item candle) {
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			if (candle == AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(sc.getCandleColor())).asItem()) {
				BlockState state = level.getBlockState(pos);
				int candles = state.getValue(BlockStateProperties.CANDLES);
				if (candles < 4) {
					level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.CANDLES, candles + 1));

					level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
					level.getLightEngine().checkBlock(pos);
					level.sendBlockUpdated(pos, state, state, 1);
					return true;
				}
			}
		} else if (level.getBlockEntity(pos) instanceof CandelabraBlockEntity candelabra) {
			if (!(candle instanceof BlockItem block)) return false;
			BlockState state = level.getBlockState(pos);
			for (int i = 0; i < 3; i++) {
				Block storedCandle = candelabra.getCandle(i);
				if (storedCandle != Blocks.AIR) continue;
				level.setBlockAndUpdate(pos, state.setValue(CandelabraBlock.CANDLES.get(i), true));
				candelabra.setCandle(i, block.getBlock());

				level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.getLightEngine().checkBlock(pos);
				level.sendBlockUpdated(pos, state, state, 1);
				return true;
			}
		}
		return false;
	}

	private static boolean tryCreateSkullCandle(ServerLevel level, BlockPos pos, Item candle) {
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.getBlock() instanceof AbstractSkullBlock skull) {
			SkullBlock.Types type = (SkullBlock.Types) skull.getType();
			boolean wall = blockstate.getBlock() instanceof WallSkullBlock;
			switch (type) {

				case SKELETON -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.SKELETON_SKULL_CANDLE.get(), candle);
				}
				case WITHER_SKELETON -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), candle);
				}
				case PLAYER -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.PLAYER_SKULL_CANDLE.get(), candle);
				}
				case ZOMBIE -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.ZOMBIE_SKULL_CANDLE.get(), candle);
				}
				case CREEPER -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.CREEPER_SKULL_CANDLE.get(), candle);
				}
				default -> {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	private static void makeFloorSkull(Level level, BlockPos pos, Block newBlock, Item candle) {
		ResolvableProfile profile = null;
		if (level.getBlockEntity(pos) instanceof SkullBlockEntity skull) profile = skull.getOwnerProfile();
		level.setBlockAndUpdate(pos, newBlock.defaultBlockState()
			.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
			.setValue(SkullCandleBlock.ROTATION, level.getBlockState(pos).getValue(SkullBlock.ROTATION)));
		level.setBlockEntity(new SkullCandleBlockEntity(pos,
			newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
				.setValue(SkullCandleBlock.ROTATION, level.getBlockState(pos).getValue(SkullBlock.ROTATION)),
			AbstractSkullCandleBlock.candleToCandleColor(candle).getValue()));
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	private static void makeWallSkull(Level level, BlockPos pos, Block newBlock, Item candle) {
		ResolvableProfile profile = null;
		if (level.getBlockEntity(pos) instanceof SkullBlockEntity skull) profile = skull.getOwnerProfile();
		level.setBlockAndUpdate(pos, newBlock.defaultBlockState()
			.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
			.setValue(WallSkullCandleBlock.FACING, level.getBlockState(pos).getValue(WallSkullBlock.FACING)));
		level.setBlockEntity(new SkullCandleBlockEntity(pos,
			newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
				.setValue(WallSkullCandleBlock.FACING, level.getBlockState(pos).getValue(WallSkullBlock.FACING)),
			AbstractSkullCandleBlock.candleToCandleColor(candle).getValue()));
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}
}
