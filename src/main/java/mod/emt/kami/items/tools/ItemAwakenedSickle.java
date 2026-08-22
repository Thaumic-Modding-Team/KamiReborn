package mod.emt.kami.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import mod.emt.kami.api.item.IAreaBreakTool;
import mod.emt.kami.registry.ModSoundsKAMI;
import mod.emt.kami.utils.helpers.HarvestHelper;
import mod.emt.kami.utils.helpers.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
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
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemAwakenedSickle extends ItemIchoriumHoe implements IAreaBreakTool {
    public static final int MAX_AREA = 15;

    public ItemAwakenedSickle() {
        super("awakened_ichorium_sickle");
        this.addPropertyOverride(new ResourceLocation("harvest_mode"), ((stack, worldIn, entityIn) -> EnumHarvestMode.getMode(stack).ordinal()));
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof EntityPlayer && !entityIn.world.isRemote) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if(player.getActiveItemStack() == stack) {
                int duration = player.getItemInUseMaxCount();
                int stages = Math.min(MAX_AREA, duration / 10);
                if(stages > 0 && duration % 10 == 0) {
                    EnumHarvestMode mode = EnumHarvestMode.getMode(stack);
                    player.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.harvest_area", stages).setStyle(new Style().setColor(mode.getTextColor())), true);
                }
            }
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        RayTraceResult trace = PlayerHelper.rayTrace(playerIn, 0);
        if(playerIn.isSneaking() && (trace == null || trace.typeOfHit == RayTraceResult.Type.MISS)) {
            if(worldIn.isRemote) {
                worldIn.playSound(playerIn, playerIn.getPosition(), ModSoundsKAMI.ITEM_ICHOR_TOGGLE.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.5F);
            } else {
                EnumHarvestMode mode = EnumHarvestMode.getMode(heldStack).nextMode();
                EnumHarvestMode.setMode(heldStack, mode);
                playerIn.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.harvest_mode." + mode).setStyle(new Style().setColor(mode.getTextColor())), true);
            }
        } else {
            playerIn.setActiveHand(handIn);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public @NotNull EnumActionResult onItemUse(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumActionResult actionResult = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        ItemStack heldStack = player.getHeldItem(hand);
        //TODO: Change onUse to instantly harvest plants or leaves with silk touch.
        if(actionResult == EnumActionResult.SUCCESS) {
            for(BlockPos aoePos : this.getBreakAreaPositions(player, heldStack, pos, false)) {
                super.onItemUse(player, worldIn, aoePos, hand, facing, hitX, hitY, hitZ);
            }
        }
        return actionResult;
    }

    @Override
    public void onPlayerStoppedUsing(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull EntityLivingBase entityLiving, int timeLeft) {
        int duration = stack.getMaxItemUseDuration() - timeLeft;
        if(entityLiving instanceof EntityPlayer && duration > 10) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            int radius = Math.min(MAX_AREA, duration / 10);
            EnumHarvestMode mode = EnumHarvestMode.getMode(stack);
            AxisAlignedBB area = new AxisAlignedBB(player.getPosition()).grow(radius, 3, radius);

            //TODO:
            //  Reap: Destroys plant blocks in an area
            //  ???: (Purple) Destroys leaf blocks in an area
            //  Plant: Tills soil in an area and attempts to plant seeds held in offhand in tilled soil
            //  Harvest: Harvests crops in an area and places drops at player's feet
            //  Behead: Swings the sickle, dealing damage in an area. Killing blows guarantee a head drop
            switch (mode) {
                case GRASSLANDS:
                    this.attemptPlantReap(worldIn, player, area);
                    break;
                case CANOPY:
                    this.attemptDestroyCanopy(worldIn, player, radius);
                case SOW:
                    this.attemptTillAndPlant(worldIn, player, area, player.getHeldItemOffhand());
                    break;
                case REAP:
                    this.attemptHarvest(worldIn, player, area);
                    break;
                case BEHEAD:
                    this.attemptBeheading(worldIn, player, area);
                    break;
            }
        }
    }

    public void attemptPlantReap(World world, EntityPlayer player, AxisAlignedBB area) {
        if(!world.isRemote) {
            for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(area.minX, area.minY, area.minZ), new BlockPos(area.maxX - 1, area.maxY, area.maxZ - 1))) {
                IBlockState state = world.getBlockState(pos);
                if (state.getMaterial() == Material.PLANTS && state.getBlockHardness(world, pos) == 0) {
                    //TODO: Break effects
                    HarvestHelper.attemptHarvestBlock(world, player, pos);
                }
            }
        }
    }

    public void attemptTillAndPlant(World world, EntityPlayer player, AxisAlignedBB area, ItemStack offhandStack) {
        for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(area.minX, area.minY, area.minZ), new BlockPos(area.maxX - 1, area.maxY, area.maxZ - 1))) {
            boolean planted = this.attemptPlant(world, pos, player, offhandStack);
            if(!planted) {
                super.onItemUse(player, world, pos, EnumHand.MAIN_HAND, EnumFacing.UP, pos.getX(), pos.getY(), pos.getZ());
                this.attemptPlant(world, pos, player, offhandStack);
            }
        }
    }

    public boolean attemptPlant(World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
        if(!stack.isEmpty() && (stack.getItem() instanceof IPlantable || Block.getBlockFromItem(stack.getItem()) instanceof IPlantable)) {
            if(stack.onItemUse(player, world, pos, EnumHand.OFF_HAND, EnumFacing.UP, pos.getX(), pos.getY(), pos.getZ()) == EnumActionResult.SUCCESS) {
                //TODO: Particle effects
                return true;
            }
        }
        return false;
    }

    public void attemptHarvest(World world, EntityPlayer player, AxisAlignedBB area) {
        //TODO: Do basic crop harvesting here. Use MI crop handling to expand the harvestable crop types.
    }

    public void attemptDestroyCanopy(World world, EntityPlayer player, int radius) {
        BlockPos start = player.getPosition().add(-radius, -2, -radius);
        BlockPos end = player.getPosition().add(radius, radius, radius);
        for(BlockPos pos : BlockPos.getAllInBox(start, end)) {
            IBlockState state = world.getBlockState(pos);
            if(state.getBlock().isLeaves(state, world, pos)) {
                //TODO: Break effect
                HarvestHelper.attemptHarvestBlock(world, player, pos);
            }
        }
    }

    public void attemptBeheading(World world, EntityPlayer player, AxisAlignedBB area) {
        for(EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, area)) {
            if(!this.isBeheadingTarget(player, entity)) continue;
            player.attackTargetEntityWithCurrentItem(entity);
            //TODO: Add an event listener that adds skulls to normal drops (only if skull is not already dropped). Use TiC implementation.
        }
    }

    public boolean isBeheadingTarget(EntityPlayer player, EntityLivingBase entity) {
        return entity.isEntityAlive()
                && !entity.getIsInvulnerable()
                && !(entity instanceof EntityPlayer)
                && !(entity instanceof IEntityOwnable && ((IEntityOwnable) entity).getOwner() == player);
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
    public @NotNull Multimap<String, AttributeModifier> getAttributeModifiers(@NotNull EntityEquipmentSlot slot, @NotNull ItemStack stack) {
        Multimap<String, AttributeModifier> multiMap = HashMultimap.create();
        if(slot == EntityEquipmentSlot.MAINHAND) {
            multiMap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.toolMaterial.getAttackDamage(), 0));
            multiMap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4f, 0));
        }
        return multiMap;
    }

    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack, @NotNull Enchantment enchantment) {
        return enchantment.type == EnumEnchantmentType.WEAPON || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean onBlockStartBreak(@NotNull ItemStack stack, @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        if(stack.getItem() instanceof IAreaBreakTool && player.getHeldItemMainhand() == stack) {
            ImmutableList<BlockPos> harvestPositions = this.getBreakAreaPositions(player, stack, pos, false);
            for(BlockPos harvestPos : harvestPositions) {
                IBlockState state = player.world.getBlockState(harvestPos);
                if(state.getMaterial() == Material.PLANTS && state.getBlockHardness(player.world, harvestPos) == 0) {
                    HarvestHelper.attemptHarvestBlock(player.world, player, harvestPos);
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        EnumHarvestMode mode = EnumHarvestMode.getMode(stack);
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.kami.tool.aoe_mode", 3));
        tooltip.add(mode.getTextColor() + I18n.format("tooltip.kami.tool.harvest_mode." + mode));
    }

    @Override
    public ImmutableList<BlockPos> getBreakAreaPositions(EntityPlayer player, ItemStack stack, BlockPos origin, boolean includeOrigin) {
        if(!player.isSneaking() && !player.world.isAirBlock(origin)) {
            RayTraceResult trace = PlayerHelper.rayTrace(player, 0);
            if(trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                //TODO: This probably needs a custom handler for the valid blocks.
                return HarvestHelper.getHarvestArea(player.world, player, trace, 3, 1, includeOrigin, false);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public boolean spawnDropsAtPlayer(EntityPlayer player, ItemStack stack) {
        return true;
    }

    public enum EnumHarvestMode {
        GRASSLANDS(TextFormatting.GRAY),
        CANOPY(TextFormatting.DARK_PURPLE),
        SOW(TextFormatting.DARK_GREEN),
        REAP(TextFormatting.BLUE),
        BEHEAD(TextFormatting.DARK_RED);

        private final TextFormatting textColor;

        EnumHarvestMode(TextFormatting textColor) {
            this.textColor = textColor;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public TextFormatting getTextColor() {
            return this.textColor;
        }

        public EnumHarvestMode nextMode() {
            EnumHarvestMode[] values = EnumHarvestMode.values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static EnumHarvestMode getMode(ItemStack stack) {
            EnumHarvestMode[] values = EnumHarvestMode.values();
            int ordinal = stack.getTagCompound() != null ? stack.getTagCompound().getInteger("mode") : 0;
            return values[MathHelper.clamp(ordinal, 0, values.length - 1)];
        }

        public static void setMode(ItemStack stack, EnumHarvestMode mode) {
            stack.setTagInfo("mode", new NBTTagInt(mode.ordinal()));
        }
    }
}
