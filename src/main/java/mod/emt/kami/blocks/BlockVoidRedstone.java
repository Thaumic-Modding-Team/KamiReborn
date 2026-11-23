package mod.emt.kami.blocks;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockVoidRedstone extends BlockMaterialCopy {
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");

    public BlockVoidRedstone() {
        super("void_ore_redstone", Blocks.REDSTONE_ORE, 0);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ENABLED, false));
    }

    public void activate(World world, BlockPos pos, IBlockState state) {
        this.spawnParticles(world, pos);

        if (!state.getValue(ENABLED)) {
            world.setBlockState(pos, state.withProperty(ENABLED, true));
        }
    }

    @Override
    public void onBlockClicked(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull EntityPlayer playerIn) {
        this.activate(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public boolean onBlockActivated(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        this.activate(worldIn, pos, state);
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onEntityWalk(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull Entity entityIn) {
        this.activate(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public void updateTick(World worldIn, @NotNull BlockPos pos, IBlockState state, @NotNull Random rand) {
        worldIn.setBlockState(pos, state.withProperty(ENABLED, false));
    }

    @Override
    public int tickRate(@NotNull World worldIn) {
        return 30;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Random rand) {
        if(stateIn.getValue(ENABLED)) {
            this.spawnParticles(worldIn, pos);
        }
    }

    private void spawnParticles(World worldIn, BlockPos pos) {
        Random random = worldIn.rand;
        double offset = 0.0625;

        for (int i = 0; i < 6; ++i) {
            double posX = (float)pos.getX() + random.nextFloat();
            double posY = (float)pos.getY() + random.nextFloat();
            double posZ = (float)pos.getZ() + random.nextFloat();

            if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube()) {
                posY = (double)pos.getY() + offset + 1.0D;
            }

            if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube()) {
                posY = (double)pos.getY() - offset;
            }

            if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube()) {
                posZ = (double)pos.getZ() + offset + 1.0D;
            }

            if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube()) {
                posZ = (double)pos.getZ() - offset;
            }

            if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube()) {
                posX = (double)pos.getX() + offset + 1.0D;
            }

            if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube()) {
                posX = (double)pos.getX() - offset;
            }

            if (posX < (double)pos.getX() || posX > (double)(pos.getX() + 1) || posY < 0.0D || posY > (double)(pos.getY() + 1) || posZ < (double)pos.getZ() || posZ > (double)(pos.getZ() + 1)) {
                worldIn.spawnParticle(EnumParticleTypes.REDSTONE, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ENABLED, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ENABLED) ? 1 : 0;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ENABLED);
    }
}
