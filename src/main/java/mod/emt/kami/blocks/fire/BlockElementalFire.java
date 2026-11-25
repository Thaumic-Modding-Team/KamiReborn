package mod.emt.kami.blocks.fire;

import mod.emt.kami.Kami;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

// TODO: It's better to use ore dictionary and config list
public abstract class BlockElementalFire extends BlockFire {
    private final Map<IBlockState, IBlockState> transformations = new HashMap<>();

    public BlockElementalFire(String name) {
        this.setRegistryName(Kami.MOD_ID, name);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setTickRandomly(true);
    }

    public void addTransformation(IBlockState input, IBlockState output) {
        transformations.put(input, output);
    }

    public boolean isTransmutationTarget(IBlockState state) {
        return transformations.containsKey(state);
    }

    public boolean isTransmutationResult(IBlockState state) {
        return transformations.containsValue(state);
    }

    public boolean isNeighborTarget(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (isTransmutationTarget(world.getBlockState(pos.offset(facing)))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateTick(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        if (world.isRemote || !world.getGameRules().getBoolean("doFireTick")) {
            return;
        }

        if (!world.isAreaLoaded(pos, 2)) return; // Forge: prevent loading unloaded chunks when spreading fire
        if (!isNeighborTarget(world, pos)) {
            world.setBlockToAir(pos);
            return;
        }

        if (rand.nextInt(20) == 0) {
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos targetPos = pos.offset(facing);
                IBlockState targetState = world.getBlockState(targetPos);
                IBlockState result = transformations.get(targetState);

                if (result != null && world.isBlockLoaded(targetPos)) {
                    world.setBlockState(targetPos, result, 3);
                }
            }
        }

        world.scheduleUpdate(pos, this, tickRate(world) + rand.nextInt(3));
    }

    @Override
    public int tickRate(@NotNull World world) {
        return 200;
    }

    @Override
    public void onBlockAdded(World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!world.isRemote) {
            world.scheduleUpdate(pos, this, tickRate(world));
        }
    }

    @Override
    public void onEntityCollision(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, Entity entityIn) {
        if (entityIn.isImmuneToFire()) return;
        if (entityIn instanceof EntityPlayer) {
            if (((EntityPlayer) entityIn).capabilities.isCreativeMode) return;
        }

        entityIn.attackEntityFrom(DamageSource.IN_FIRE, 1.0F);
        if (!world.isRemote) entityIn.setFire(8);
        super.onEntityCollision(world, pos, state, entityIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, Random rand) {
        if (rand.nextInt(24) == 0) {
            world.playSound((float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }

        if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP) && !this.canCatchFire(world, pos.down(), EnumFacing.UP)) {
            if (this.canCatchFire(world, pos.west(), EnumFacing.EAST)) {
                for (int j = 0; j < 2; ++j) {
                    double d3 = (double) pos.getX() + rand.nextDouble() * 0.1D;
                    double d8 = (double) pos.getY() + rand.nextDouble();
                    double d13 = (double) pos.getZ() + rand.nextDouble();
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canCatchFire(world, pos.east(), EnumFacing.WEST)) {
                for (int k = 0; k < 2; ++k) {
                    double d4 = (double) (pos.getX() + 1) - rand.nextDouble() * 0.1D;
                    double d9 = (double) pos.getY() + rand.nextDouble();
                    double d14 = (double) pos.getZ() + rand.nextDouble();
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d9, d14, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canCatchFire(world, pos.north(), EnumFacing.SOUTH)) {
                for (int l = 0; l < 2; ++l) {
                    double d5 = (double) pos.getX() + rand.nextDouble();
                    double d10 = (double) pos.getY() + rand.nextDouble();
                    double d15 = (double) pos.getZ() + rand.nextDouble() * 0.1D;
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d5, d10, d15, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canCatchFire(world, pos.south(), EnumFacing.NORTH)) {
                for (int i1 = 0; i1 < 2; ++i1) {
                    double d6 = (double) pos.getX() + rand.nextDouble();
                    double d11 = (double) pos.getY() + rand.nextDouble();
                    double d16 = (double) (pos.getZ() + 1) - rand.nextDouble() * 0.1D;
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d11, d16, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canCatchFire(world, pos.up(), EnumFacing.DOWN)) {
                for (int j1 = 0; j1 < 2; ++j1) {
                    double d7 = (double) pos.getX() + rand.nextDouble();
                    double d12 = (double) (pos.getY() + 1) - rand.nextDouble() * 0.1D;
                    double d17 = (double) pos.getZ() + rand.nextDouble();
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                double d0 = (double) pos.getX() + rand.nextDouble();
                double d1 = (double) pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
                double d2 = (double) pos.getZ() + rand.nextDouble();
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
