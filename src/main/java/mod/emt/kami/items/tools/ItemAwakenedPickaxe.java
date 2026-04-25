package mod.emt.kami.items.tools;

import com.google.common.collect.ImmutableList;
import mod.emt.kami.api.item.IAreaBreakTool;
import mod.emt.kami.utils.helpers.HarvestHelper;
import mod.emt.kami.utils.helpers.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemAwakenedPickaxe extends ItemIchoriumPickaxe implements IAreaBreakTool {
    public ItemAwakenedPickaxe() {
        super("awakened_ichorium_pickaxe");
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof EntityPlayer && !entityIn.world.isRemote) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if(player.getActiveItemStack() == stack) {
                int duration = player.getItemInUseMaxCount();
                int stages = Math.min(12, (duration - 10) / 10);
                if(stages > 0 && duration % 10 == 0) {
                    player.sendStatusMessage(new TextComponentString(String.valueOf(stages)), true);
                }
            }
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void onPlayerStoppedUsing(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull EntityLivingBase entityLiving, int timeLeft) {
        int duration = stack.getMaxItemUseDuration() - timeLeft;
        if(entityLiving instanceof EntityPlayer && duration > 20) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            RayTraceResult trace = PlayerHelper.getPlayerTrace(player, 0);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && trace.sideHit != null) {
                int diameter = this.getBreakDiameter(stack);
                int depth = Math.min(12, (duration - 10) / 10);
                ImmutableList<BlockPos> harvestPositions = HarvestHelper.getHarvestArea(player.world, player, trace, diameter, depth, true);
                HarvestHelper.harvestExtraBlocks(player, stack, harvestPositions);
            }
        }
    }

    @Override
    public @NotNull EnumAction getItemUseAction(@NotNull ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean onBlockStartBreak(@NotNull ItemStack stack, @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        if(stack.getItem() instanceof IAreaBreakTool && player.getHeldItemMainhand() == stack) {
            ImmutableList<BlockPos> harvestPositions = this.getBreakAreaPositions(player, stack, pos, false);
            HarvestHelper.harvestExtraBlocks(player, stack, harvestPositions);
        }
        return false;
    }

    @Override
    public ImmutableList<BlockPos> getBreakAreaPositions(EntityPlayer player, ItemStack stack, BlockPos origin, boolean includeOrigin) {
        if(!player.isSneaking() && !player.world.isAirBlock(origin)) {
            RayTraceResult trace = PlayerHelper.getPlayerTrace(player, 0);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && trace.sideHit != null) {
                int diameter = this.getBreakDiameter(stack);
                return HarvestHelper.getHarvestArea(player.world, player, trace, diameter, 1, includeOrigin);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public boolean spawnDropsAtPlayer(EntityPlayer player, ItemStack stack) {
        return true;
    }

    public int getBreakDiameter(ItemStack stack) {
        //TODO
        return 3;//stack.getTagCompound() != null ? stack.getTagCompound().getInteger("breakDiameter") : 0;
    }

    public void setBreakDiameter(ItemStack stack, int breakDiameter) {
        stack.setTagInfo("breakDiameter", new NBTTagInt(breakDiameter));
    }
}
