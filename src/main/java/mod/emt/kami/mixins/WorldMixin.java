package mod.emt.kami.mixins;

import mod.emt.kami.blocks.BlockElementalFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(World.class)
public abstract class WorldMixin {
    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void destroyBlock(BlockPos pos, boolean dropBlock, CallbackInfoReturnable<Boolean> ci) {
        World world = (World) (Object) this;
        if (world.getBlockState(pos).getBlock() instanceof BlockElementalFire) {
            ci.setReturnValue(world.setBlockToAir(pos));
        }
    }

    @Inject(method = "extinguishFire", at = @At("HEAD"), cancellable = true)
    public void extinguishFire(@Nullable EntityPlayer player, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> ci) {
        World fire = (World) (Object) this;
        if (fire.getBlockState(pos.offset(side)).getBlock() instanceof BlockElementalFire) {
            fire.playEvent(player, 1009, pos.offset(side), 0);
            fire.setBlockToAir(pos.offset(side));
            ci.setReturnValue(true);
        }
    }
}
