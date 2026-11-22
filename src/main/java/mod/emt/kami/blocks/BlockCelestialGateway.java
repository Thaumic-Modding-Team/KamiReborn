package mod.emt.kami.blocks;

import mod.emt.kami.tiles.TileCelestialGateway;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;

public class BlockCelestialGateway extends BlockTileEntityKami {
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final EnumMap<EnumFacing, AxisAlignedBB> GATEWAY_AABBS = new EnumMap<>(new HashMap<EnumFacing, AxisAlignedBB>() {{
        put(EnumFacing.DOWN,    new AxisAlignedBB(0.0,  0.875,  0.0,    1.0,    1.0,    1.0));
        put(EnumFacing.UP,      new AxisAlignedBB(0.0,  0.0,    0.0,    1.0,    0.125,  1.0));
        put(EnumFacing.NORTH,   new AxisAlignedBB(0.0,  0.0,    0.875,  1.0,    1.0,    1.0));
        put(EnumFacing.SOUTH,   new AxisAlignedBB(0.0,  0.0,    0.0,    1.0,    1.0,    0.125));
        put(EnumFacing.WEST,    new AxisAlignedBB(0.875,0.0,    0.0,    1.0,    1.0,    1.0));
        put(EnumFacing.EAST,    new AxisAlignedBB(0.0,  0.0,    0.0,    0.125,  1.0,    1.0));
    }});

    public BlockCelestialGateway() {
        super("celestial_gateway", Material.ROCK, MapColor.STONE, TileCelestialGateway.class);
        this.setHardness(2.0f);
        this.setResistance(20.0f);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return GATEWAY_AABBS.get(state.getValue(FACING));
    }

    @Override
    public boolean onBlockActivated(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileCelestialGateway && ((TileCelestialGateway) tile).hasSavedPosition(playerIn)) {
            ((TileCelestialGateway) tile).teleportToSavedPosition(playerIn);
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(@NotNull IBlockState base_state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        return world.getBlockState(pos).getValue(FACING).getOpposite() == side;
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
    public @NotNull IBlockState getStateForPlacement(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @NotNull EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(@NotNull IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState withRotation(@NotNull IBlockState state, @NotNull Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState withMirror(@NotNull IBlockState state, @NotNull Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }
}
