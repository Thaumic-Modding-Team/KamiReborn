package mod.emt.kami.items.tools;

import com.google.common.collect.ImmutableList;
import mod.emt.kami.api.item.IAreaBreakTool;
import mod.emt.kami.registry.ModSoundsKAMI;
import mod.emt.kami.utils.helpers.HarvestHelper;
import mod.emt.kami.utils.helpers.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
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

import java.util.List;
import java.util.function.Predicate;

public class ItemAwakenedShovel extends ItemIchoriumShovel implements IAreaBreakTool {
    public static final int MAX_DEPTH = 15;

    public ItemAwakenedShovel() {
        super("awakened_ichorium_shovel");
        this.addPropertyOverride(new ResourceLocation("bury_area"), ((stack, worldIn, entityIn) -> EnumBuryMode.getMode(stack).ordinal()));
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof EntityPlayer && !entityIn.world.isRemote) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if(player.getActiveItemStack() == stack) {
                int duration = player.getItemInUseMaxCount();
                int stages = Math.min(MAX_DEPTH, duration / 10);
                if(stages > 0 && duration % 10 == 0) {
                    EnumBuryMode mode = EnumBuryMode.getMode(stack);
                    player.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.bury_area", stages).setStyle(new Style().setColor(mode.getTextColor())), true);
                }
            }
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            worldIn.playSound(null, playerIn.getPosition(), ModSoundsKAMI.ITEM_ICHOR_TOGGLE.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.5F);

            if(!worldIn.isRemote) {
                EnumBuryMode mode = EnumBuryMode.getMode(heldStack).nextMode();
                EnumBuryMode.setMode(heldStack, mode);
                playerIn.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.bury_mode." + mode).setStyle(new Style().setColor(mode.getTextColor())), true);
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
            int depth = Math.min(MAX_DEPTH, duration / 10);
            EnumBuryMode mode = EnumBuryMode.getMode(stack);
            AxisAlignedBB area = new AxisAlignedBB(player.getPosition()).grow(depth, 2, depth);
            for (Entity entity : worldIn.getEntitiesWithinAABB(mode.getEntityClass(), area, mode::isEntityValid)) {
                if(!worldIn.isRemote) {
                    worldIn.playSound(null, entity.getPosition(), ModSoundsKAMI.ITEM_ICHOR_BURY.getSoundEvent(), SoundCategory.PLAYERS, 1.0f, 1.0f);
                    worldIn.playSound(null, entity.getPosition(), SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    double posX = entity.posX;
                    double posY = entity.posY - (int) Math.ceil(entity.height + 1.0);
                    double posZ = entity.posZ;
                    entity.setPositionAndUpdate(posX, posY, posZ);
                } else {
                    IBlockState state = worldIn.getBlockState(entity.getPosition().down());
                    if(state.getBlock() != Blocks.AIR && state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                        for (int i = 0; i < 12; i++) {
                            worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                                    entity.posX + (worldIn.rand.nextFloat() - 0.5D) * entity.width,
                                    entity.getEntityBoundingBox().minY + 0.1D,
                                    entity.posZ + (worldIn.rand.nextFloat() - 0.5D) * entity.width,
                                    -entity.motionX * 4.0D, 1.5D, -entity.motionZ * 4.0D,
                                    Block.getStateId(state));
                        }
                    }
                }
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
        EnumBuryMode mode = EnumBuryMode.getMode(stack);
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.kami.tool.aoe_mode", 3));
        tooltip.add(mode.getTextColor() + I18n.format("tooltip.kami.tool.bury_mode." + mode));
    }

    @Override
    public ImmutableList<BlockPos> getBreakAreaPositions(EntityPlayer player, ItemStack stack, BlockPos origin, boolean includeOrigin) {
        if(!player.isSneaking() && !player.world.isAirBlock(origin)) {
            RayTraceResult trace = PlayerHelper.rayTrace(player, 0);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && trace.sideHit != null) {
                return HarvestHelper.getHarvestArea(player.world, player, trace, 3, 1, includeOrigin, true);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public boolean spawnDropsAtPlayer(EntityPlayer player, ItemStack stack) {
        return true;
    }

    public enum EnumBuryMode {
        UNDEAD(TextFormatting.GOLD, EntityLivingBase.class, EntityLivingBase::isEntityUndead),     //White
        ANIMALS(TextFormatting.DARK_GREEN, EntityAnimal.class),     //Green
        ALL(TextFormatting.BLUE, EntityLivingBase.class),           //Blue
        HOSTILE(TextFormatting.DARK_RED, EntityMob.class);          //Red

        private final TextFormatting textColor;
        private final Class<? extends EntityLivingBase> entityClass;
        private final Predicate<EntityLivingBase> predicate;

        EnumBuryMode(TextFormatting textColor, Class<? extends EntityLivingBase> entityClass, Predicate<EntityLivingBase> predicate) {
            this.textColor = textColor;
            this.entityClass = entityClass;
            this.predicate = predicate;
        }

        EnumBuryMode(TextFormatting textColor, Class<? extends EntityLivingBase> entityClass) {
            this(textColor, entityClass, entity -> true);
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public TextFormatting getTextColor() {
            return this.textColor;
        }

        public Class<? extends EntityLivingBase> getEntityClass() {
            return this.entityClass;
        }

        public boolean isEntityValid(EntityLivingBase entity) {
            return entity.isEntityAlive() && entity.isNonBoss() && !(entity instanceof EntityPlayer) && this.predicate.test(entity);
        }

        public EnumBuryMode nextMode() {
            EnumBuryMode[] values = EnumBuryMode.values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static EnumBuryMode getMode(ItemStack stack) {
            EnumBuryMode[] values = EnumBuryMode.values();
            int ordinal = stack.getTagCompound() != null ? stack.getTagCompound().getInteger("mode") : 0;
            return values[MathHelper.clamp(ordinal, 0, values.length - 1)];
        }

        public static void setMode(ItemStack stack, EnumBuryMode mode) {
            stack.setTagInfo("mode", new NBTTagInt(mode.ordinal()));
        }
    }
}
