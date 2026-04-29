package mod.emt.kami.items.tools;

import com.google.common.collect.ImmutableList;
import mod.emt.kami.api.item.IAreaBreakTool;
import mod.emt.kami.entities.EntityThrownAxe;
import mod.emt.kami.registry.ModSoundsKAMI;
import mod.emt.kami.utils.helpers.HarvestHelper;
import mod.emt.kami.utils.helpers.PlayerHelper;
import mod.emt.kami.utils.helpers.TreeHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.*;
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
import java.util.function.BiConsumer;

public class ItemAwakenedAxe extends ItemIchoriumAxe implements IAreaBreakTool {
    public static BiConsumer<EntityThrownAxe, EntityLivingBase> DAMAGE_CONSUMER = (entityAxe, hitEntity) -> {
        if(!entityAxe.world.isRemote && hitEntity != null) {
            hitEntity.attackEntityFrom(DamageSource.causeThrownDamage(entityAxe, entityAxe.owner), entityAxe.getDamage());
        }
    };
    public static BiConsumer<EntityThrownAxe, EntityLivingBase> LIGHTNING_CONSUMER = (entityAxe, hitEntity) -> {
        if (hitEntity != null) {
            hitEntity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, entityAxe.getDamage() - 5.0f);
            entityAxe.world.spawnEntity(new EntityLightningBolt(entityAxe.world, hitEntity.posX, hitEntity.posY, hitEntity.posZ, false));
        } else {
            entityAxe.world.spawnEntity(new EntityLightningBolt(entityAxe.world, entityAxe.posX, entityAxe.posY, entityAxe.posZ, false));
        }
    };
    public static BiConsumer<EntityThrownAxe, EntityLivingBase> DETONATION_CONSUMER = (entityAxe, hitEntity) -> {
        if(!entityAxe.world.isRemote) {
            entityAxe.world.createExplosion(entityAxe.owner, entityAxe.posX, entityAxe.posY, entityAxe.posZ, entityAxe.getSpeed() * 2.0f, false);
        }
    };
    public static BiConsumer<EntityThrownAxe, EntityLivingBase> EXPLOSION_CONSUMER = (entityAxe, hitEntity) -> {
        if(!entityAxe.world.isRemote) {
            entityAxe.world.createExplosion(entityAxe.owner, entityAxe.posX, entityAxe.posY, entityAxe.posZ, entityAxe.getSpeed() * 5.0f, true);
        }
    };

    public ItemAwakenedAxe() {
        super("awakened_ichorium_axe");
        this.addPropertyOverride(new ResourceLocation("impact_mode"), ((stack, worldIn, entityIn) -> EnumImpactMode.getImpactMode(stack).ordinal()));
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, @NotNull EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        if(player.isSneaking()) {
            EnumImpactMode mode = EnumImpactMode.getImpactMode(heldStack).nextMode();
            EnumImpactMode.setImpactMode(heldStack, mode);
            world.playSound(null, player.getPosition(), ModSoundsKAMI.ITEM_ICHOR_TOGGLE.getSoundEvent(), SoundCategory.PLAYERS, 1.0f, 1.5f);
            player.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.impact_mode." + mode).setStyle(new Style().setColor(mode.getTextColor())), true);
        } else if(hand == EnumHand.MAIN_HAND) {
            player.setActiveHand(hand);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public void onPlayerStoppedUsing(@NotNull ItemStack stack, World worldIn, @NotNull EntityLivingBase entityLiving, int timeLeft) {
        if (!worldIn.isRemote && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            int duration = this.getMaxItemUseDuration(stack) - timeLeft;
            float velocity = ItemBow.getArrowVelocity(duration);

            if(velocity >= 0.1) {
                EntityThrownAxe entityAxe = new EntityThrownAxe(worldIn, player, stack.copy());
                IAttributeInstance instance = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
                if (instance != null) {
                    entityAxe.setDamage((float) instance.getAttributeValue());
                }
                entityAxe.thrownFromSlot = player.inventory.currentItem;
                worldIn.spawnEntity(entityAxe);
                worldIn.playSound(null, player.getPosition(), ModSoundsKAMI.ITEM_ICHOR_THROW.getSoundEvent(), SoundCategory.PLAYERS, 3.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                stack.setCount(0);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        EnumImpactMode mode = EnumImpactMode.getImpactMode(stack);
        tooltip.add(mode.getTextColor() + I18n.format("tooltip.kami.tool.impact_mode." + mode));
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, @NotNull BlockPos pos, @NotNull EntityPlayer player) {
        if(stack.getItem() instanceof IAreaBreakTool && player.getHeldItemMainhand() == stack) {
            if(TreeHelper.isTreeStructure(player.world, pos)) {
                return TreeHelper.fellTree(stack, pos, player);
            } else {
                ImmutableList<BlockPos> harvestPositions = this.getBreakAreaPositions(player, stack, pos, false);
                HarvestHelper.harvestExtraBlocks(player, stack, harvestPositions);
            }
        }
        return false;
    }

    @Override
    public ImmutableList<BlockPos> getBreakAreaPositions(EntityPlayer player, ItemStack stack, BlockPos origin, boolean includeOrigin) {
        if(!player.isSneaking() && !player.world.isAirBlock(origin)) {
            RayTraceResult trace = PlayerHelper.rayTrace(player, 0);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && trace.sideHit != null && !TreeHelper.isTreeStructure(player.world, trace.getBlockPos())) {
                return HarvestHelper.getHarvestArea(player.world, player, trace, 3, 1, includeOrigin, true);
            }
        }
        return ImmutableList.of();
    }

    @Override
    public boolean spawnDropsAtPlayer(EntityPlayer player, ItemStack stack) {
        return true;
    }

    public enum EnumImpactMode {
        DAMAGE(TextFormatting.GOLD, DAMAGE_CONSUMER),
        LIGHTNING(TextFormatting.BLUE, LIGHTNING_CONSUMER),
        DETONATION(TextFormatting.DARK_GREEN, DETONATION_CONSUMER),
        EXPLOSION(TextFormatting.DARK_RED, EXPLOSION_CONSUMER);

        private final TextFormatting textColor;
        private final BiConsumer<EntityThrownAxe, EntityLivingBase> onHitConsumer;

        EnumImpactMode(TextFormatting textColor, BiConsumer<EntityThrownAxe, EntityLivingBase> onHitConsumer) {
            this.textColor = textColor;
            this.onHitConsumer = onHitConsumer;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public TextFormatting getTextColor() {
            return this.textColor;
        }

        public void onImpact(EntityThrownAxe entityAxe, @Nullable EntityLivingBase entityHit) {
            this.onHitConsumer.accept(entityAxe, entityHit);
        }

        public EnumImpactMode nextMode() {
            EnumImpactMode[] values = EnumImpactMode.values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static EnumImpactMode getImpactMode(ItemStack stack) {
            EnumImpactMode[] values = EnumImpactMode.values();
            int ordinal = stack.getTagCompound() != null ? stack.getTagCompound().getInteger("mode") : 0;
            ordinal = MathHelper.clamp(ordinal, 0, values.length - 1);
            return values[ordinal];
        }

        public static void setImpactMode(ItemStack stack, EnumImpactMode mode) {
            stack.setTagInfo("mode", new NBTTagInt(mode.ordinal()));
        }
    }
}
