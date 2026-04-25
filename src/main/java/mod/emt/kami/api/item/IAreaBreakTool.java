package mod.emt.kami.api.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface IAreaBreakTool {
    ImmutableList<BlockPos> getBreakAreaPositions(EntityPlayer player, ItemStack stack, BlockPos origin, boolean includeOrigin);

    boolean spawnDropsAtPlayer(EntityPlayer player, ItemStack stack);
}
