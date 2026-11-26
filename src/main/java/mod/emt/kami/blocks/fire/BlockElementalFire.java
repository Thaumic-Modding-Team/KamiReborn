package mod.emt.kami.blocks.fire;

import mod.emt.kami.Kami;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

// TODO: It's better to use ore dictionary and config list
public abstract class BlockElementalFire extends BlockFire {
    private final static Map<Aspect, IBlockState> FIRE_STATES = new HashMap<>();

    private final Map<IBlockState, IBlockState> transformations = new HashMap<>();
    private final Aspect fireAspect;

    public BlockElementalFire(String name, Aspect fireAspect) {
        this.setRegistryName(Kami.MOD_ID, name);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setLightLevel(1.0F);
        this.fireAspect = fireAspect;
        setElementalFirePlacementState(fireAspect, this.getDefaultState());
    }

    public static IBlockState getElementalFirePlacementState(Aspect aspect) {
        return FIRE_STATES.getOrDefault(aspect, Blocks.AIR.getDefaultState());
    }

    public static void setElementalFirePlacementState(Aspect aspect, IBlockState placementState) {
        FIRE_STATES.put(aspect, placementState);
    }

    public Aspect getFireAspect() {
        return fireAspect;
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
    public boolean canCatchFire(IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        //TODO: Add a config option to disable fire spread
        return isTransmutationTarget(world.getBlockState(pos));
    }

    @Override
    public void updateTick(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        if (world.isRemote || !world.getGameRules().getBoolean("doFireTick")) {
            return;
        }

        if (!world.isAreaLoaded(pos, 2)) {
            return;
        }

        if (!isNeighborTarget(world, pos)) {
            world.setBlockToAir(pos);
            return;
        }

        if (!this.canPlaceBlockAt(world, pos)) {
            world.setBlockToAir(pos);
            return;
        }

        boolean fireSource = world.getBlockState(pos.down()).getBlock().isFireSource(world, pos.down(), EnumFacing.UP);
        int age = state.getValue(AGE);

        if (!fireSource && world.isRaining() && this.canDie(world, pos) && rand.nextFloat() < 0.2F + age * 0.03F) {
            world.setBlockToAir(pos);
            return;
        }

        if (age < 15) {
            state = state.withProperty(AGE, age + rand.nextInt(3) / 2);
            world.setBlockState(pos, state, 4);
        }

        world.scheduleUpdate(pos, this, tickRate(world) + rand.nextInt(10));

        if (rand.nextInt(20) == 0) {
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos targetPos = pos.offset(facing);
                transmuteOrDestroy(world, targetPos, rand);
            }
        }

        if (!fireSource) {
            if (!canNeighborCatchFire(world, pos)) {
                if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP) || age > 3) {
                    world.setBlockToAir(pos);
                }

                return;
            }

            if (!canCatchFire(world, pos.down(), EnumFacing.UP) && age == 15 && rand.nextInt(4) == 0) {
                world.setBlockToAir(pos);
                return;
            }
        }

        boolean highHumidity = world.isBlockinHighHumidity(pos);
        int humidityModifier = highHumidity ? -50 : 0;

        for (EnumFacing facing : EnumFacing.values()) {
            int strength = facing.getAxis() == EnumFacing.Axis.Y ? 250 : 300;
            tryCatchFire(world, pos.offset(facing), strength + humidityModifier, rand, age, facing.getOpposite());
        }

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 4; y++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    int difficulty = 100;
                    if (y > 1) {
                        difficulty += (y - 1) * 100;
                    }

                    BlockPos targetPos = pos.add(x, y, z);
                    int encouragement = getNeighborEncouragement(world, targetPos);

                    if (encouragement <= 0) {
                        continue;
                    }

                    int chance = (encouragement + 70) / (age + 30);
                    chance += 70;

                    if (highHumidity) {
                        chance /= 2;
                    }

                    if (chance > 0 && rand.nextInt(difficulty) <= chance && (!world.isRaining() || !this.canDie(world, targetPos))) {
                        int newAge = Math.min(15, age + rand.nextInt(5) / 4);
                        setBlockWithTransmutationTarget(world, targetPos, newAge, rand);
                    }
                }
            }
        }
    }

    public int getDecayChance() {
        return 1;
    }

    private void transmuteOrDestroy(World world, BlockPos pos, Random rand) {
        IBlockState result = transformations.get(world.getBlockState(pos));

        if (result == null) {
            return;
        }

        int decayChance = Math.max(1, getDecayChance());
        if (rand.nextInt(decayChance) == 0) {
            world.setBlockState(pos, result, 3);
        } else {
            world.setBlockToAir(pos);
        }
    }

    private void setBlockWithTransmutationTarget(World world, BlockPos pos, int age, Random rand) {
        IBlockState state = world.getBlockState(pos);

        if (isTransmutationTarget(state)) {
            transmuteOrDestroy(world, pos, rand);
        } else if (world.isAirBlock(pos) && isNeighborTarget(world, pos)) {
            world.setBlockState(pos, this.getDefaultState().withProperty(AGE, age), 3);
        }
    }

    private int getBlockFlammability(IBlockAccess world, BlockPos pos) {
        return isTransmutationTarget(world.getBlockState(pos)) ? 100 : 0;
    }

    private int getBlockFireSpreadSpeed(IBlockAccess world, BlockPos pos) {
        return isTransmutationTarget(world.getBlockState(pos)) ? 100 : 0;
    }

    private void tryCatchFire(World world, BlockPos pos, int chance, Random rand, int age, EnumFacing face) {
        int flammability = getBlockFlammability(world, pos);

        if (rand.nextInt(chance) >= flammability) {
            return;
        }

        IBlockState original = world.getBlockState(pos);
        boolean tnt = original.getBlock() == Blocks.TNT;

        if (isTransmutationTarget(original)) {
            transmuteOrDestroy(world, pos, rand);
        } else if (rand.nextInt(age + 10) < 5 && !world.isRainingAt(pos)) {
            int newAge = Math.min(15, age + rand.nextInt(5) / 4);
            setBlockWithTransmutationTarget(world, pos, newAge, rand);
        } else {
            world.setBlockToAir(pos);
        }

        if (tnt) {
            Blocks.TNT.onPlayerDestroy(world, pos, original.withProperty(BlockTNT.EXPLODE, true));
        }
    }

    private boolean canNeighborCatchFire(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (canCatchFire(world, pos.offset(facing), facing.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private int getNeighborEncouragement(World world, BlockPos pos) {
        if (!world.isAirBlock(pos)) {
            return 0;
        }

        int encouragement = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighborPos = pos.offset(facing);
            encouragement = Math.max(encouragement, getBlockFireSpreadSpeed(world, neighborPos));
        }

        return encouragement;
    }

    @Override
    public int tickRate(@NotNull World world) {
        return 200;
    }

    @Override
    public void onBlockAdded(World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!world.isRemote) {
            if (!canPlaceBlockAt(world, pos)) {
                world.setBlockToAir(pos);
                return;
            }
            world.scheduleUpdate(pos, this, tickRate(world) + world.rand.nextInt(10));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull EnumPushReaction getPushReaction(@NotNull IBlockState state) {
        return EnumPushReaction.DESTROY;
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
