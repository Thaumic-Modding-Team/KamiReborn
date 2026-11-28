package mod.emt.kami.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

import java.util.Random;

public class BlockElementalFirePerditio extends BlockElementalFire {
    public BlockElementalFirePerditio() {
        super(Aspect.ENTROPY);
    }

    @Override
    public boolean isTransmutationTarget(World world, BlockPos pos, IBlockState state) {
        return super.isTransmutationTarget(world, pos, state);
    }

    @Override
    public boolean isNeighborTarget(World world, BlockPos pos) {
        return super.isNeighborTarget(world, pos);
    }

    @Override
    public boolean canCatchFire(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return super.canCatchFire(world, pos, face);
    }

    @Override
    public void updateTick(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        super.updateTick(world, pos, state, rand);
    }

    @Override
    public int tickRate(@NotNull World world) {
        return 5;
    }
}
