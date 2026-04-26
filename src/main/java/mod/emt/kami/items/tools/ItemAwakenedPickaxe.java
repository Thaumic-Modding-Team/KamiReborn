package mod.emt.kami.items.tools;

import com.google.common.collect.ImmutableList;
import mod.emt.kami.api.item.IAreaBreakTool;
import mod.emt.kami.utils.helpers.HarvestHelper;
import mod.emt.kami.utils.helpers.PlayerHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.fx.FXDispatcher;

import java.util.List;

public class ItemAwakenedPickaxe extends ItemIchoriumPickaxe implements IAreaBreakTool {
    public static final int MAX_DEPTH = 15;

    public ItemAwakenedPickaxe() {
        super("awakened_ichorium_pickaxe");
        this.addPropertyOverride(new ResourceLocation("aoe_mode"), ((stack, worldIn, entityIn) -> EnumAoeMode.getAoeMode(stack).ordinal()));
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof EntityPlayer && !entityIn.world.isRemote) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if(player.getActiveItemStack() == stack) {
                int duration = player.getItemInUseMaxCount();
                int stages = Math.min(MAX_DEPTH, duration / 10);
                if(stages > 0 && duration % 10 == 0) {
                    EnumAoeMode mode = EnumAoeMode.getAoeMode(stack);
                    player.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.dig_depth", stages).setStyle(new Style().setColor(mode.getTextColor())), true);
                }
            }
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            if(!worldIn.isRemote) {
                EnumAoeMode mode = EnumAoeMode.getAoeMode(heldStack).nextMode();
                EnumAoeMode.setAoeMode(heldStack, mode);
                playerIn.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.aoe_mode", mode.getBreakAreaSize()).setStyle(new Style().setColor(mode.getTextColor())), true);
            }
        } else {
            playerIn.setActiveHand(handIn);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public void onPlayerStoppedUsing(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull EntityLivingBase entityLiving, int timeLeft) {
        int duration = stack.getMaxItemUseDuration() - timeLeft;
        if(entityLiving instanceof EntityPlayer && duration > 10) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            RayTraceResult trace = PlayerHelper.getPlayerTrace(player, 0);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && trace.sideHit != null) {
                int diameter = this.getBreakAreaSize(stack);
                int depth = Math.min(MAX_DEPTH, duration / 10);
                ImmutableList<BlockPos> harvestPositions = HarvestHelper.getHarvestArea(player.world, player, trace, diameter, depth, true);
                if(worldIn.isRemote) {
                    for(BlockPos pos : harvestPositions) {
                        //TODO: Change effect color?
                        FXDispatcher.INSTANCE.drawBamf(pos, 0x7AA721, true, true, trace.sideHit);
                    }
                }
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        EnumAoeMode mode = EnumAoeMode.getAoeMode(stack);
        tooltip.add(mode.getTextColor() + I18n.format("tooltip.kami.tool.aoe_mode", mode.getBreakAreaSize()));
    }

    @Override
    public ImmutableList<BlockPos> getBreakAreaPositions(EntityPlayer player, ItemStack stack, BlockPos origin, boolean includeOrigin) {
        if(!player.isSneaking() && !player.world.isAirBlock(origin)) {
            RayTraceResult trace = PlayerHelper.getPlayerTrace(player, 0);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && trace.sideHit != null) {
                int diameter = this.getBreakAreaSize(stack);
                return HarvestHelper.getHarvestArea(player.world, player, trace, diameter, 1, includeOrigin);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public boolean spawnDropsAtPlayer(EntityPlayer player, ItemStack stack) {
        return true;
    }

    public int getBreakAreaSize(ItemStack stack) {
        return EnumAoeMode.getAoeMode(stack).getBreakAreaSize();
    }

    public enum EnumAoeMode {
        ONE(1, TextFormatting.GOLD),
        THREE(3, TextFormatting.BLUE),
        FIVE(5, TextFormatting.DARK_GREEN),
        SEVEN(7, TextFormatting.DARK_RED);

        private final int breakAreaSize;
        private final TextFormatting textColor;

        EnumAoeMode(int breakAreaSize, TextFormatting textColor) {
            this.breakAreaSize = breakAreaSize;
            this.textColor = textColor;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public int getBreakAreaSize() {
            return this.breakAreaSize;
        }

        public TextFormatting getTextColor() {
            return this.textColor;
        }

        public EnumAoeMode nextMode() {
            EnumAoeMode[] values = EnumAoeMode.values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static EnumAoeMode getAoeMode(ItemStack stack) {
            EnumAoeMode[] values = EnumAoeMode.values();
            int ordinal = stack.getTagCompound() != null ? stack.getTagCompound().getInteger("mode") : 0;
            ordinal = MathHelper.clamp(ordinal, 0, values.length - 1);
            return values[ordinal];
        }

        public static void setAoeMode(ItemStack stack, EnumAoeMode mode) {
            stack.setTagInfo("mode", new NBTTagInt(mode.ordinal()));
        }
    }
}
