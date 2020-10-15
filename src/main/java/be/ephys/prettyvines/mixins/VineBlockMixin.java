package be.ephys.prettyvines.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static be.ephys.prettyvines.helpers.Utils.randomIntInclusive;

@Mixin(VineBlock.class)
public class VineBlockMixin {

  @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
  private void randomTick$handleVineAge(BlockState state, ServerWorld worldIn, BlockPos pos, Random random, CallbackInfo callbackInfo) {
    if (cantGrowDown(worldIn, pos)) {
      return;
    }

    // seeded random so we always get the same value for the same block coords
    // easier than adding an age block property
    Random random2 = new Random(pos.toLong());
    int maxHeight = randomIntInclusive(random2, 4, 7);
    if (!isVineHeightAtMost(maxHeight, worldIn, pos)) {
      callbackInfo.cancel();
      return;
    }

    int minAirSpace = randomIntInclusive(random2, 0, 2);
    if (!isBottomFreeSpaceAtLeast(minAirSpace, worldIn, pos.down())) {
      callbackInfo.cancel();
    }
  }

  private static boolean isVineHeightAtMost(int maxHeight, ServerWorld worldIn, BlockPos pos) {
    int height = 0;

    while (worldIn.getBlockState(pos).getBlock() == Blocks.VINE) {
      height += 1;
      pos = pos.up();

      if (height >= maxHeight) {
        return false;
      }
    }

    return true;
  }

  private static boolean isBottomFreeSpaceAtLeast(int minHeight, ServerWorld worldIn, BlockPos pos) {
    int height = 0;

    while (worldIn.getBlockState(pos).getBlock() == Blocks.AIR) {
      height += 1;
      pos = pos.down();

      if (height > minHeight) {
        return true;
      }
    }

    return false;
  }

  private static boolean cantGrowDown(ServerWorld worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.down()).getBlock() != Blocks.AIR;
  }
}
