package mod.emt.kami.utils.helpers;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HarvestHelper {

    /**
     * Returns a of all possible block positions for in a given area.
     *
     * @param trace The player raytrace to the block.
     * @param diameter The harvest area diameter
     * @param depth The harvest area depth
     * @param includeOrigin If the origin block position be included in the returned set
     * @return A list containing all blocks within a given area.
     */
    public static List<BlockPos> getHarvestArea(@NotNull RayTraceResult trace, int diameter, int depth, boolean includeOrigin) {
        EnumFacing side = trace.sideHit;
        BlockPos startPos = trace.getBlockPos();

        if (diameter % 2 == 0) {
            float hx = (float)trace.hitVec.x - (float)trace.getBlockPos().getX();
            float hy = (float)trace.hitVec.y - (float)trace.getBlockPos().getY();
            float hz = (float)trace.hitVec.z - (float)trace.getBlockPos().getZ();
            if (side.getAxis() == EnumFacing.Axis.Y && (double)hx < (double)0.5F || side.getAxis() == EnumFacing.Axis.Z && (double)hx < (double)0.5F) {
                startPos = startPos.add(-diameter / 2, 0, 0);
            }

            if (side.getAxis() != EnumFacing.Axis.Y && (double)hy < (double)0.5F) {
                startPos = startPos.add(0, -diameter / 2, 0);
            }

            if (side.getAxis() == EnumFacing.Axis.Y && (double)hz < (double)0.5F || side.getAxis() == EnumFacing.Axis.X && (double)hz < (double)0.5F) {
                startPos = startPos.add(0, 0, -diameter / 2);
            }
        } else {
            startPos = startPos.add(
                    -(side.getAxis() == EnumFacing.Axis.X ? (side == EnumFacing.EAST  ? depth - 1 : 0) : diameter / 2),
                    -(side.getAxis() == EnumFacing.Axis.Y ? (side == EnumFacing.UP    ? depth - 1 : 0) : (diameter <= 1 ? 0 : 1)),
                    -(side.getAxis() == EnumFacing.Axis.Z ? (side == EnumFacing.SOUTH ? depth - 1 : 0) : diameter / 2));
        }

        List<BlockPos> posList = new ArrayList<>();
        for(int dd = 0; dd < depth; dd++) {
            for(int dw = 0; dw < diameter; dw++) {
                for(int dh = 0; dh < diameter; dh++) {
                    BlockPos pos = startPos.add(
                            side.getAxis() == EnumFacing.Axis.X ? dd : dw,
                            side.getAxis() == EnumFacing.Axis.Y ? dd : dh,
                            side.getAxis() == EnumFacing.Axis.Z ? dd : (side.getAxis() == EnumFacing.Axis.Y ? dh : dw)
                    );
                    if (!pos.equals(trace.getBlockPos()) || includeOrigin) {
                        posList.add(pos);
                    }
                }
            }
        }

        return posList;
    }

    public static ImmutableList<BlockPos> getHarvestArea(World world, EntityPlayer player, @NotNull RayTraceResult trace, int diameter, int depth, boolean includeOrigin, boolean strictHarvestCheck) {
        ImmutableList.Builder<BlockPos> posBuilder = ImmutableList.builder();
        for(BlockPos pos : getHarvestArea(trace, diameter, depth, includeOrigin)) {
            IBlockState state = world.getBlockState(pos);
            if(canHarvestBlock(world, pos, state, player, strictHarvestCheck)) {
                posBuilder.add(pos);
            }
        }
        return posBuilder.build();
    }

    public static boolean canHarvestBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player, boolean strict) {
        float hardness = state.getPlayerRelativeBlockHardness(player, world, pos);

        boolean toolCheck = true;
        if(strict) {
            ItemStack heldStack = player.getHeldItemMainhand();
            toolCheck = heldStack.getItem().getToolClasses(heldStack).contains(state.getBlock().getHarvestTool(state)) || heldStack.getItem().canHarvestBlock(state, player.getHeldItemMainhand());
        }
        return toolCheck && hardness >= 0 && ForgeHooks.canHarvestBlock(state.getBlock(), player, world, pos);
    }

    public static void harvestExtraBlocks(EntityPlayer player, ItemStack stack, ImmutableList<BlockPos> harvestPositions) {
        int damage = 0;
        for(BlockPos pos : harvestPositions) {
            if(attemptHarvestBlock(player.world, player, pos)) {
                damage++;
            }
        }
        if(damage > 0 && !player.isCreative()) {
            stack.damageItem(damage, player);
        }
    }

    public static boolean attemptHarvestBlock(World world, EntityPlayer player, BlockPos pos) {
        if (world.isAirBlock(pos))
            return false;

        EntityPlayerMP playerMP = (player instanceof EntityPlayerMP) ? (EntityPlayerMP) player : null;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        float hardness = state.getBlockHardness(world, pos);
        float relativeHardness = state.getPlayerRelativeBlockHardness(player, world, pos);
        if (hardness >= 0 && relativeHardness >= 0 && ForgeHooks.canHarvestBlock(block, player, world, pos)) {
            int xpToDrop = 0;
            if (playerMP != null) {
                xpToDrop = ForgeHooks.onBlockBreakEvent(world, playerMP.interactionManager.getGameType(), playerMP, pos);
                if (xpToDrop == -1) {
                    return false;
                }
                playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
            }

            if (!world.isRemote) {
                if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
                    block.onPlayerDestroy(world, pos, state);
                    if (!player.capabilities.isCreativeMode) {
                        block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
                        if (xpToDrop > 0)
                            block.dropXpOnBlockBreak(world, pos, xpToDrop);
                    }
                }
            } else {
                if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
                    block.onPlayerDestroy(world, pos, state);
                }
                if (Minecraft.getMinecraft().getConnection() != null)
                    Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
            }
            return true;
        }
        return false;
    }
}
