package mod.emt.kami.utils.helpers;

import com.google.common.collect.Lists;
import gnu.trove.set.hash.THashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class TreeHelper {
    public static boolean fellTree(ItemStack itemstack, BlockPos start, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote) {
            MinecraftForge.EVENT_BUS.register(new TreeChopTask(itemstack, start, player, 1));
        }
        return true;
    }

    public static boolean isTreeStructure(World world, BlockPos origin) {
        for(int y = 0; y < 32; y++) {
            BlockPos checkPos = origin.up(y);
            if(isLog(world, checkPos)) {
                for(BlockPos adjacent : getAdjacentPositions(checkPos)) {
                    if(isLeaves(world, adjacent)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    private static boolean isLog(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isWood(world, pos);
    }

    private static boolean isLeaves(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isLeaves(state, world, pos);
    }

    public static List<BlockPos> getAdjacentPositions(BlockPos pos) {
        List<BlockPos> adjacent = new ArrayList<>();
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                for(int z = -1; z <= 1; z++) {
                    if(x == 0 && y == 0 && z == 0) continue;
                    adjacent.add(pos.add(x, y, z));
                }
            }
        }
        return adjacent;
    }

    public static class TreeChopTask {
        public final World world;
        public final EntityPlayer player;
        public final ItemStack tool;
        public final int blocksPerTick;
        public Queue<BlockPos> blocks = Lists.newLinkedList();
        public Set<BlockPos> visited = new THashSet<>();

        public TreeChopTask(ItemStack tool, BlockPos start, EntityPlayer player, int blocksPerTick) {
            this.world = player.getEntityWorld();
            this.player = player;
            this.tool = tool;
            this.blocksPerTick = blocksPerTick;
            this.blocks.add(start);
        }

        @SubscribeEvent
        public void fellTree(TickEvent.WorldTickEvent event) {
            if (event.side.isClient()) {
                this.finish();
            } else if (event.world.provider.getDimension() == this.world.provider.getDimension()) {
                int left = this.blocksPerTick;

                while(left > 0) {
                    if (this.blocks.isEmpty()) {
                        this.finish();
                        return;
                    }

                    BlockPos pos = this.blocks.remove();
                    if (this.visited.add(pos) && isLog(this.world, pos) && HarvestHelper.canHarvestBlock(this.world, pos, this.world.getBlockState(pos), this.player, true)) {
                        for(BlockPos adjacent : getAdjacentPositions(pos)) {
                            if (!this.visited.contains(adjacent)) {
                                this.blocks.add(adjacent);
                            }
                        }

                        //Adding up to a 3x3 surrounding the harvested block
                        for(int x = 0; x < 3; ++x) {
                            for(int z = 0; z < 3; ++z) {
                                BlockPos pos2 = pos.add(-1 + x, 1, -1 + z);
                                if (!this.visited.contains(pos2)) {
                                    this.blocks.add(pos2);
                                }
                            }
                        }

                        HarvestHelper.attemptHarvestBlock(this.world, this.player, pos);
                        left--;
                    }
                }

            }
        }

        private void finish() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
