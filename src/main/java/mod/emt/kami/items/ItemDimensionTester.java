package mod.emt.kami.items;

import mod.emt.kami.dimension.TeleporterWaysBetween;
import mod.emt.kami.items.base.ItemBase;
import mod.emt.kami.registry.ModDimensionsKAMI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.jetbrains.annotations.NotNull;

// TODO: Remove when the way to get to the dimension becomes finalized
public class ItemDimensionTester extends ItemBase {
    public ItemDimensionTester() {
        super("dimension_tester", null);
        setMaxStackSize(1);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote && player instanceof EntityPlayerMP) {
            int targetDimension = player.isSneaking() ? 0 : ModDimensionsKAMI.WAYS_BETWEEN_ID;

            if (player.dimension != targetDimension) {
                DimensionManager.initDimension(targetDimension);
                WorldServer targetWorld = DimensionManager.getWorld(targetDimension);

                if (targetWorld != null) {
                    player.changeDimension(targetDimension, new TeleporterWaysBetween());
                }
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}