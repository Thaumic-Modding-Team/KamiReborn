package mod.emt.kami.items.misc;

import mod.emt.kami.config.ConfigHandlerKami;
import mod.emt.kami.entities.EntityElementalFireBottle;
import mod.emt.kami.items.base.ItemBase;
import mod.emt.kami.registry.ElementalFireRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class ItemElementalFireBottle extends ItemBase {
    public ItemElementalFireBottle() {
        super("elemental_fire_bottle");
        this.setRarity(EnumRarity.COMMON);
        this.setHasSubtypes(true);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BottleDispenserLogic());
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            for(Aspect aspect : ElementalFireRegistry.getElementalFireTypes()) {
                items.add(ElementalFireRegistry.getElementalFireBottle(aspect));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull String getItemStackDisplayName(@NotNull ItemStack stack) {
        Aspect aspect = ElementalFireRegistry.getAspectFromBottle(stack);
        if(aspect != null) {
            return net.minecraft.util.text.translation.I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + "_" + aspect.getTag() + ".name").trim();
        } else {
            return net.minecraft.util.text.translation.I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
        }

    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        Aspect aspect = ElementalFireRegistry.getAspectFromBottle(heldStack);

        if(aspect == null) {
            return new ActionResult<>(EnumActionResult.FAIL, heldStack);
        }

        ItemStack thrown = playerIn.capabilities.isCreativeMode ? ItemHandlerHelper.copyStackWithSize(heldStack, 1) : heldStack.splitStack(1);
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS,
                0.5f, 0.4f / itemRand.nextFloat() * 0.4f + 0.8f);

        if(!worldIn.isRemote) {
            EntityElementalFireBottle bottle = new EntityElementalFireBottle(worldIn, playerIn, thrown, aspect);
            bottle.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0f, 0.5f, 1.0f);
            worldIn.spawnEntity(bottle);
        }

        playerIn.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public @NotNull EnumActionResult onItemUse(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = player.getHeldItem(hand);
        Aspect aspect = ElementalFireRegistry.getAspectFromBottle(heldStack);
        if(aspect != null && player.isSneaking()) {
            IBlockState fireState = ElementalFireRegistry.getElementalFireBlock(aspect);
            if(fireState.getBlock() != Blocks.AIR) {
                worldIn.setBlockState(pos.offset(facing), fireState);
            }

            if(!player.isCreative()) {
                heldStack.shrink(1);
            }
            worldIn.playSound(player, pos, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.AMBIENT, 1.0f, 0.5f);
            worldIn.playSound(player, pos, SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, SoundCategory.AMBIENT, 1.0f, 0.1f + (float) (worldIn.rand.nextGaussian() * 0.05f));
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        //TODO: Warning tooltip
        if(!ConfigHandlerKami.elemental_fire._disableTooltip) {
            Aspect aspect = ElementalFireRegistry.getAspectFromBottle(stack);
            if(aspect != null) {
                if(aspect != Aspect.ENTROPY) {
                    tooltip.add(I18n.format("tooltip.kami.elemental_fire.warning.0"));
                    tooltip.add(I18n.format("tooltip.kami.elemental_fire.warning.1"));
                    tooltip.add(I18n.format("tooltip.kami.elemental_fire.warning.2"));
                } else {
                    tooltip.add(I18n.format("tooltip.kami.elemental_fire.perditio"));
                }
            }
        }
    }

    public static class BottleDispenserLogic implements IBehaviorDispenseItem {
        @Override
        public @NotNull ItemStack dispense(@NotNull IBlockSource source, @NotNull ItemStack stack) {
            return new BehaviorProjectileDispense() {
                @Override
                protected @NotNull IProjectile getProjectileEntity(@NotNull World worldIn, @NotNull IPosition position, @NotNull ItemStack stackIn) {
                    ItemStack thrown = ItemHandlerHelper.copyStackWithSize(stackIn, 1);
                    Aspect aspect = ElementalFireRegistry.getAspectFromBottle(thrown);
                    return new EntityElementalFireBottle(worldIn, position.getX(), position.getY(), position.getZ(), thrown, aspect != null ? aspect : Aspect.AIR);
                }

                @Override
                protected float getProjectileInaccuracy() {
                    return super.getProjectileInaccuracy() * 0.5f;
                }

                @Override
                protected float getProjectileVelocity() {
                    return super.getProjectileVelocity() * 1.25f;
                }
            }.dispense(source, stack);
        }
    }
}
