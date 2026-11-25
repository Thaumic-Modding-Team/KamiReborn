package mod.emt.kami.items;

import mod.emt.kami.items.base.ItemBase;
import mod.emt.kami.registry.ModBlocksKAMI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemFireTest extends ItemBase {
    public ItemFireTest() {
        super("fire_tester");
    }

    @Override
    public @NotNull EnumActionResult onItemUse(@NotNull EntityPlayer player, World world, BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos placePos = pos.offset(facing);

        if (!world.isBlockModifiable(player, placePos) || !player.canPlayerEdit(placePos, facing, player.getHeldItem(hand))) {
            return EnumActionResult.FAIL;
        }

        if (!world.getBlockState(placePos).getBlock().isReplaceable(world, placePos)) {
            return EnumActionResult.FAIL;
        }

        if (!world.isRemote) {
            world.setBlockState(placePos, ModBlocksKAMI.IGNIS_FIRE.getDefaultState(), 3);
        }

        return EnumActionResult.SUCCESS;
    }
}