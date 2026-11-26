package mod.emt.kami.mixins;

import mod.emt.kami.blocks.BlockElementalFire;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockFalling.class)
public abstract class BlockFallingMixin {
    @Inject(method = "canFallThrough", at = @At("HEAD"), cancellable = true)
    private static void canFallThrough(IBlockState state, CallbackInfoReturnable<Boolean> ci) {
        if (state.getBlock() instanceof BlockElementalFire)
            ci.setReturnValue(true);
    }
}
