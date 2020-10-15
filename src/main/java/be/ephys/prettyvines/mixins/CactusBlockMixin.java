package be.ephys.prettyvines.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static be.ephys.prettyvines.helpers.Utils.randomIntInclusive;

@Mixin(CactusBlock.class)
public class CactusBlockMixin {
  @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
  private void randomTick$randomiseHeight(BlockState state, ServerWorld worldIn, BlockPos pos, Random random, CallbackInfo callbackInfo) {
    callbackInfo.cancel();

    BlockPos up = pos.up();
    if (!worldIn.isAirBlock(up)) {
      return;
    }

    CactusBlock self = (CactusBlock) (Object) this;

    int i;
    for(i = 1; worldIn.getBlockState(pos.down(i)).isIn(self); ++i) {
    }

    Random random2 = new Random(pos.toLong());
    int maxHeight = randomIntInclusive(random2, 2, 4);

    if (i < maxHeight) {
      int j = state.get(CactusBlock.AGE);
      if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, up, state, true)) {
        if (j == 15) {
          worldIn.setBlockState(up, self.getDefaultState());
          BlockState blockstate = state.with(CactusBlock.AGE, Integer.valueOf(0));
          worldIn.setBlockState(pos, blockstate, 4);
          blockstate.neighborChanged(worldIn, up, self, pos, false);
        } else {
          worldIn.setBlockState(pos, state.with(CactusBlock.AGE, Integer.valueOf(j + 1)), 4);
        }
        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
      }
    }
  }
}
