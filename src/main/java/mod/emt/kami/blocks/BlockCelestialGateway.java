package mod.emt.kami.blocks;

import mod.emt.kami.tiles.TileCelestialGateway;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

public class BlockCelestialGateway extends BlockTileEntityKami {
    public static final AxisAlignedBB GATEWAY_AABB = new AxisAlignedBB(0, 0, 0, 1.0, 0.1875, 1.0);

    public BlockCelestialGateway() {
        super("celestial_gateway", Material.ROCK, MapColor.STONE, TileCelestialGateway.class);
        this.setHardness(2.0f);
        this.setResistance(20.0f);
        this.setSoundType(SoundType.STONE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return GATEWAY_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(@NotNull IBlockState base_state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        //TODO: Change side with rotation
        return super.isSideSolid(base_state, world, pos, side);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta);
    }

    @Override
    public int getMetaFromState(@NotNull IBlockState state) {
        return super.getMetaFromState(state);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return super.createBlockState();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState withRotation(@NotNull IBlockState state, @NotNull Rotation rot) {
        return super.withRotation(state, rot);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState withMirror(@NotNull IBlockState state, @NotNull Mirror mirrorIn) {
        return super.withMirror(state, mirrorIn);
    }
}
