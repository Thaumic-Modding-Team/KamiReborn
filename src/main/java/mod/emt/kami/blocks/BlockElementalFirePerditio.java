package mod.emt.kami.blocks;

import mod.emt.kami.config.ConfigHandlerKami;
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
        return state.getBlock() instanceof BlockElementalFire && !(state.getBlock() instanceof BlockElementalFirePerditio);
    }

    @Override
    public boolean isNeighborTarget(World world, BlockPos pos) {
        if (ConfigHandlerKami.elemental_fire._disableFireSpread) {
            return false;
        }

        //Checking all adjacent targets
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                for(int z = -1; z <= 1; z++) {
                    BlockPos targetPos = pos.add(x, y, z);
                    if(targetPos.equals(pos))
                        continue;

                    if (isTransmutationTarget(world, targetPos, world.getBlockState(targetPos))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean canCatchFire(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return world instanceof World && isTransmutationTarget((World) world, pos, world.getBlockState(pos));
    }

    @Override
    public boolean canPlaceBlockAt(@NotNull World world, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public void onBlockAdded(World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!world.isRemote) {
            world.scheduleUpdate(pos, this, tickRate(world));
        }
    }

    @Override
    public void updateTick(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        if (world.isRemote || !world.getGameRules().getBoolean("doFireTick")) {
            return;
        }

        if (!world.isAreaLoaded(pos, 1)) {
            world.scheduleUpdate(pos, this, tickRate(world));
            return;
        }

        if (!ConfigHandlerKami.elemental_fire._disableFireSpread) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos targetPos = pos.offset(facing);
                IBlockState targetState = world.getBlockState(targetPos);

                if (isTransmutationTarget(world, targetPos, targetState)) {
                    world.setBlockState(targetPos, this.getDefaultState().withProperty(AGE, 0), 3);
                }
            }
        }

        int age = state.getValue(AGE) + 1;
        if (age >= 6) {
            world.setBlockToAir(pos);
            return;
        }

        world.setBlockState(pos, state.withProperty(AGE, age), 4);
        world.scheduleUpdate(pos, this, tickRate(world));
    }

    @Override
    public int tickRate(@NotNull World world) {
        return 2;
    }
}