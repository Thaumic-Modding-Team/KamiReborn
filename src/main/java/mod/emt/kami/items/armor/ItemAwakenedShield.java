package mod.emt.kami.items.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.invadermonky.thaumicapi.ThaumicAPIMod;
import com.invadermonky.thaumicapi.network.NetworkHandlerTAPI;
import com.invadermonky.thaumicapi.network.messages.MessageAuraToClient;
import com.invadermonky.thaumicapi.utils.helpers.PlayerHelper;
import com.invadermonky.thaumicapi.utils.libs.ModIds;
import mod.emt.kami.client.KeyBindingsKami;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import thaumcraft.api.casters.*;
import thaumcraft.api.items.IArchitect;
import thaumcraft.common.items.casters.CasterManager;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;
import thecodex6824.thaumicaugmentation.ThaumicAugmentation;
import thecodex6824.thaumicaugmentation.api.augment.AugmentableItem;
import thecodex6824.thaumicaugmentation.api.augment.CapabilityAugmentableItem;
import thecodex6824.thaumicaugmentation.api.augment.IAugmentableItem;
import thecodex6824.thaumicaugmentation.api.event.CastEvent;
import thecodex6824.thaumicaugmentation.api.util.FocusUtils;
import thecodex6824.thaumicaugmentation.api.util.FocusWrapper;
import thecodex6824.thaumicaugmentation.common.capability.provider.SimpleCapabilityProvider;
import thecodex6824.thaumicaugmentation.common.util.ItemHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemAwakenedShield extends ItemIchoriumShield implements ICaster, IArchitect {
    public static final String TAG_FOCUS = "storedFocus";
    public static final String TAG_BLOCK = "pickedBlock";
    protected static final Method CASTER_IS_ON_COOLDOWN;
    protected static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#######.#");
    private static final UUID UUID_SHIELD_HEALTH = UUID.fromString("89be1774-fa98-4bfe-a98a-01ffd59e6bab");

    public ItemAwakenedShield() {
        super("awakened_ichorweave_shield");
    }

    public static boolean isShieldEquipped(@NotNull EntityPlayer player, boolean checkFocus) {
        return isItemShield(player.getHeldItemMainhand(), checkFocus) || isItemShield(player.getHeldItemOffhand(), checkFocus);
    }

    public static boolean isItemShield(ItemStack stack, boolean checkFocus) {
        if(!stack.isEmpty() && stack.getItem() instanceof ItemAwakenedShield) {
            if(checkFocus) {
                ItemStack focusStack = ((ItemAwakenedShield) stack.getItem()).getFocusStack(stack);
                FocusPackage focusPackage = ((ItemAwakenedShield) stack.getItem()).getFocusPackage(focusStack);
                return !focusStack.isEmpty() && focusPackage != null;
            }
            return true;
        }
        return false;
    }

    public static void castWithShield(EntityPlayer player) {
        if(isItemShield(player.getHeldItemMainhand(), false)) {
            ItemStack stack = player.getHeldItemMainhand();
            ((ItemAwakenedShield) stack.getItem()).castWithShield(player.world, player, EnumHand.MAIN_HAND, stack);
        } else if (isItemShield(player.getHeldItemOffhand(), false)) {
            ItemStack stack = player.getHeldItemOffhand();
            ((ItemAwakenedShield) stack.getItem()).castWithShield(player.world, player, EnumHand.OFF_HAND, stack);
        }
    }

    public void castWithShield(World world, EntityPlayer player, EnumHand hand, ItemStack shield) {
        if(player.isSpectator() || player.getCooldownTracker().hasCooldown(this)) {
            return;
        }

        if(this.hasFocusStack(shield) && !this.isCasterOnCooldown(player)) {
            ItemStack focusStack = this.getFocusStack(shield);
            FocusPackage focusPackage = this.getFocusPackage(focusStack);
            ItemStack copy = shield.copy();
            int count = copy.getCount();
            int meta = copy.getMetadata();
            ActionResult<ItemStack> result = this.castShieldFocusSpell(world, player, hand, shield, focusStack, focusPackage);
            ItemStack resultStack = result.getResult();

            if ((!ItemStack.areItemStacksEqual(copy, resultStack) || count != resultStack.getCount() || meta != resultStack.getMetadata()) && result.getType() != EnumActionResult.FAIL) {
                player.setHeldItem(hand, resultStack);
                if(player.isCreative()) {
                    resultStack.setCount(copy.getCount());
                    if(resultStack.isItemStackDamageable()) {
                        resultStack.setItemDamage(copy.getItemDamage());
                    }
                }

                if(resultStack.isEmpty()) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
            }
        }
    }

    protected ActionResult<ItemStack> castShieldFocusSpell(World world, EntityPlayer player, EnumHand hand, ItemStack shield, ItemStack focusStack, FocusPackage focusPackage) {
        if(!focusStack.isEmpty() && focusPackage != null) {
            //Resetting Focus block picker
            if (player.isSneaking()) {
                for (IFocusElement element : focusPackage.nodes) {
                    if (element instanceof IFocusBlockPicker) {
                        return new ActionResult<>(EnumActionResult.PASS, shield);
                    }
                }
            }

            float visCost = ((ItemFocus) focusStack.getItem()).getVisCost(focusStack) * this.getConsumptionModifier(shield, player, false);
            int activationTime = ((ItemFocus) focusStack.getItem()).getActivationTime(focusStack);

            boolean consumeVis = this.consumeVis(shield, player, visCost, false, true);
            if (consumeVis) {
                boolean isSuccess;
                if (ModIds.thaumic_augmentation.isLoaded) {
                    isSuccess = this.castFocusSpellTA(world, player, shield, focusPackage, activationTime, visCost);
                } else {
                    isSuccess = this.castFocusSpell(world, player, shield, focusPackage, activationTime, visCost);
                }

                if (isSuccess) {
                    if (!world.isRemote && !player.isActiveItemStackBlocking()) {
                        player.swingArm(hand);
                    }
                    return new ActionResult<>(EnumActionResult.SUCCESS, shield);
                }
            }
            return new ActionResult<>(EnumActionResult.FAIL, shield);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        if(ModIds.thaumic_augmentation.isLoaded) {
            SimpleCapabilityProvider<IAugmentableItem> provider = new SimpleCapabilityProvider<>(new AugmentableItem(1), CapabilityAugmentableItem.AUGMENTABLE_ITEM);
            if (nbt != null && nbt.hasKey("Parent", Constants.NBT.TAG_COMPOUND)) {
                provider.deserializeNBT(nbt.getCompoundTag("Parent"));
            }
            return provider;
        }
        return super.initCapabilities(stack, nbt);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if(ModIds.thaumic_augmentation.isLoaded) {
            if (tab == this.getCreativeTab() || tab == CreativeTabs.SEARCH) {
                ItemStack stack = new ItemStack(this);
                mod.emt.kami.utils.helpers.ItemHelper.setUnbreakable(stack);
                if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && !ThaumicAugmentation.proxy.isSingleplayer()) {
                    stack.setTagInfo("cap", ((AugmentableItem) stack.getCapability(CapabilityAugmentableItem.AUGMENTABLE_ITEM, null)).serializeNBT());
                }
                items.add(stack);
            }
        } else {
            super.getSubItems(tab, items);
        }
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(!worldIn.isRemote && entityIn.ticksExisted % 10 == 0 && entityIn instanceof EntityPlayerMP) {
            for(ItemStack heldStack : entityIn.getHeldEquipment()) {
                if(!heldStack.isEmpty() && heldStack.getItem() instanceof ICaster) {
                    AuraChunk chunk = AuraHandler.getAuraChunk(worldIn.provider.getDimension(), entityIn.chunkCoordX, entityIn.chunkCoordZ);
                    if(chunk != null) {
                        NetworkHandlerTAPI.INSTANCE.sendTo(new MessageAuraToClient(chunk), (EntityPlayerMP) entityIn);
                    }
                }
            }
        }
    }

    protected boolean castFocusSpell(World world, EntityPlayer player, ItemStack casterStack, FocusPackage focusPackage, int activationTime, float visCost) {
        if(!world.isRemote) {
            this.consumeVis(casterStack, player, visCost, false, false);
            FocusEngine.castFocusPackage(player, focusPackage);
        }
        return true;
    }

    @Optional.Method(modid = ModIds.ConstIds.thaumic_augmentation)
    protected boolean castFocusSpellTA(World world, EntityPlayer player, ItemStack casterStack, FocusPackage focusPackage, int activationTime, float visCost) {
        CastEvent.Pre preEvent = new CastEvent.Pre(player, casterStack, new FocusWrapper(focusPackage, activationTime, visCost));
        MinecraftForge.EVENT_BUS.post(preEvent);
        if(!preEvent.isCanceled()) {
            if(world.isRemote) {
                CasterManager.setCooldown(player, preEvent.getFocus().getCooldown());
            } else {
                this.consumeVis(casterStack, player, visCost, false, false);
                FocusUtils.replaceAndFixFoci(focusPackage, player);
                FocusEngine.castFocusPackage(player, focusPackage);
                //This was the problem right here. For some reason copying the focus package results
                // in the projectiles hitting the player.
                //FocusEngine.castFocusPackage(player, focusPackage, true);
                CasterManager.setCooldown(player, preEvent.getFocus().getCooldown());
                MinecraftForge.EVENT_BUS.post(new CastEvent.Post(player, casterStack, preEvent.getFocus()));
            }
            return true;
        }
        return false;
    }

    protected boolean isCasterOnCooldown(EntityLivingBase entityLiving) {
        try {
            return (Boolean)CASTER_IS_ON_COOLDOWN.invoke(null, entityLiving);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            FMLCommonHandler.instance().raiseException(ex, "Failed to invoke Thaumcraft's CasterManager#isOnCooldown", true);
            return true;
        }
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(@NotNull EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof IInteractWithCaster && ((IInteractWithCaster) state.getBlock()).onCasterRightClick(world, heldStack, player, pos, side, hand)) {
            return EnumActionResult.SUCCESS;
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof IInteractWithCaster && ((IInteractWithCaster) tile).onCasterRightClick(world, heldStack, player, pos, side, hand)) {
                return EnumActionResult.SUCCESS;
            } else if(CasterTriggerRegistry.hasTrigger(state)) {
                return CasterTriggerRegistry.performTrigger(world, heldStack, player, pos, side, state) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            } else if(this.hasFocusStack(heldStack)) {
                ItemStack focusStack = this.getFocusStack(heldStack);
                FocusPackage focusPackage = this.getFocusPackage(focusStack);
                if(focusPackage != null) {
                    for(IFocusElement element : focusPackage.nodes) {
                        if(element instanceof IFocusBlockPicker && player.isSneaking() && tile == null) {
                            if(!world.isRemote) {
                                RayTraceResult trace = PlayerHelper.rayTrace(player, 0);
                                ItemStack toStore = state.getBlock().getPickBlock(state, trace, world, pos, player);
                                if(toStore.isEmpty() && state.getBlock().canSilkHarvest(world, pos, state, player)) {
                                    toStore = BlockUtils.getSilkTouchDrop(state).copy();
                                }
                                if(!toStore.isEmpty()) {
                                    focusStack.setTagInfo(TAG_BLOCK, toStore.writeToNBT(new NBTTagCompound()));
                                    return EnumActionResult.SUCCESS;
                                }
                            }
                            player.swingArm(hand);
                            return EnumActionResult.PASS;
                        }
                    }
                }
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public @NotNull Multimap<String, AttributeModifier> getAttributeModifiers(@NotNull EntityEquipmentSlot slot, @NotNull ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if(slot == EntityEquipmentSlot.OFFHAND) {
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(UUID_SHIELD_HEALTH, "Shield modifier", 20.0, Constants.AttributeModifierOperation.ADD));
        }
        return multimap;
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
        if(!slotChanged && oldStack.getItem() instanceof ICaster && newStack.getItem() instanceof ICaster && this.hasFocusStack(oldStack) && this.hasFocusStack(newStack)) {
            ItemStack oldFocus = this.getFocusStack(oldStack);
            ItemStack newFocus = this.getFocusStack(newStack);
            if ((oldFocus.isEmpty() && !newFocus.isEmpty()) || (!oldFocus.isEmpty() && newFocus.isEmpty())) {
                return true;
            } else {
                return ((ItemFocus) oldFocus.getItem()).getSortingHelper(oldFocus).hashCode() != ((ItemFocus) newFocus.getItem()).getSortingHelper(newFocus).hashCode();
            }
        } else {
            return slotChanged || oldStack.getItem() != newStack.getItem() || oldStack.getMetadata() != newStack.getMetadata();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        if(this.hasFocusStack(stack)) {
            int keyId = KeyBindingsKami.castShieldFocus.getKeyCode();
            if(keyId > 0) {
                tooltip.add(I18n.format("tooltip.kami.awakened_shield.keybind", Keyboard.getKeyName(keyId)));
                tooltip.add("");
            }

            ItemStack focusStack = this.getFocusStack(stack);
            ItemFocus focus = (ItemFocus) focusStack.getItem();
            float visCost = focus.getVisCost(focusStack) * this.getConsumptionModifier(stack, Minecraft.getMinecraft().player, false);
            if(visCost > 0) {
                tooltip.add(TextFormatting.AQUA + I18n.format("tc.vis.cost") + " " +
                        TextFormatting.RESET + DECIMAL_FORMATTER.format(visCost) + " " +
                        I18n.format("item.Focus.cost1"));
            }
            tooltip.add(TextFormatting.GREEN + focusStack.getDisplayName());
            focus.addFocusInformation(focusStack, worldIn, tooltip, flagIn);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @Nullable NBTTagCompound getNBTShareTag(@NotNull ItemStack stack) {
        if(ModIds.thaumic_augmentation.isLoaded) {
            NBTTagCompound tag = new NBTTagCompound();
            if(stack.hasTagCompound()) {
                NBTTagCompound itemTag = stack.getTagCompound().copy();
                if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && !ThaumicAugmentation.proxy.isSingleplayer()) {
                    itemTag.removeTag("cap");
                }
                tag.setTag("item", itemTag);
            }
            NBTTagCompound cap = ItemHelper.tryMakeCapabilityTag(stack, CapabilityAugmentableItem.AUGMENTABLE_ITEM);
            if(cap != null) {
                tag.setTag("cap", cap);
            }
            return tag;
        }
        return super.getNBTShareTag(stack);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void readNBTShareTag(@NotNull ItemStack stack, @Nullable NBTTagCompound tag) {
        if(ModIds.thaumic_augmentation.isLoaded) {
            if(tag != null) {
                if(tag.hasKey("cap", Constants.NBT.TAG_COMPOUND)) {
                    ((AugmentableItem) stack.getCapability(CapabilityAugmentableItem.AUGMENTABLE_ITEM, null)).deserializeNBT(tag.getCompoundTag("cap"));
                }
                if(tag.hasKey("item", Constants.NBT.TAG_COMPOUND)) {
                    stack.setTagCompound(tag.getCompoundTag("item"));
                } else if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                    tag.removeTag("cap");
                    if(!tag.isEmpty()) {
                        stack.setTagCompound(tag);
                    }
                }

                if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && !ThaumicAugmentation.proxy.isSingleplayer()) {
                    stack.setTagInfo("cap", tag.getCompoundTag("cap"));
                }
            }
        } else {
            super.readNBTShareTag(stack, tag);
        }
    }

    //##########################################################
    // ICaster


    @Override
    public float getConsumptionModifier(ItemStack caster, EntityPlayer player, boolean crafting) {
        return player != null ? (1.0f - CasterManager.getTotalVisDiscount(player)) : 1.0f;
    }

    @Override
    public boolean consumeVis(ItemStack casterStack, EntityPlayer player, float visDrain, boolean crafting, boolean simulate) {
        //Do not call getConsumptionModifier() here. It is already called and calculated prior to this method being fired.
        BlockPos pos = player.getPosition();
        float aura = AuraHandler.getTotalAura(player.world, pos);
        if(aura >= visDrain) {
            if(!simulate) {
                AuraHandler.drainVis(player.world, pos, visDrain, false);
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public ItemFocus getFocus(ItemStack casterStack) {
        ItemStack focusStack = this.getFocusStack(casterStack);
        return !focusStack.isEmpty() && focusStack.getItem() instanceof ItemFocus ? (ItemFocus) focusStack.getItem() : null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemStack getFocusStack(ItemStack casterStack) {
        if(casterStack.hasTagCompound() && casterStack.getTagCompound().hasKey(TAG_FOCUS)) {
            return new ItemStack(casterStack.getTagCompound().getCompoundTag(TAG_FOCUS));
        }
        return ItemStack.EMPTY;
    }

    /**
     * Returns the FocusPackage of the ItemFocus. The ItemStack passed to this method can either be
     * the caster ItemStack or the focus ItemStack.
     *
     * @param stack the caster or focus ItemStack instance
     * @return the focus package associated with the passed ItemStack
     */
    @Nullable
    public FocusPackage getFocusPackage(ItemStack stack) {
        if(!stack.isEmpty()) {
            if (!(stack.getItem() instanceof ItemFocus)) {
                stack = this.getFocusStack(stack);
            }
            return ItemFocus.getPackage(stack);
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setFocus(ItemStack caster, ItemStack focus) {
        if(focus != null && !focus.isEmpty()) {
            caster.setTagInfo(TAG_FOCUS, focus.writeToNBT(new NBTTagCompound()));
        } else if(caster.hasTagCompound()) {
            caster.getTagCompound().removeTag(TAG_FOCUS);
        }
    }

    public boolean hasFocusStack(ItemStack stack) {
        return stack.getItem() instanceof ICaster && this.getFocus(stack) instanceof ItemFocus;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemStack getPickedBlock(ItemStack stack) {
        ItemStack ret = ItemStack.EMPTY;
        if(stack != null && !stack.isEmpty()) {
            ItemStack focusStack = this.getFocusStack(stack);
            FocusPackage focusPackage = this.getFocusPackage(focusStack);
            if (!focusStack.isEmpty() && focusPackage != null) {
                for (IFocusElement element : focusPackage.nodes) {
                    if (element instanceof IFocusBlockPicker) {
                        try {
                            return new ItemStack(focusStack.getTagCompound().getCompoundTag(TAG_BLOCK));
                        } catch (Exception e) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
        }
        return ret;
    }

    //##########################################################
    // IArchitect

    @Override
    public RayTraceResult getArchitectMOP(ItemStack stack, World world, EntityLivingBase user) {
        if(this.hasFocusStack(stack)) {
            FocusPackage focusPackage = this.getFocusPackage(stack);
            if(focusPackage != null && FocusEngine.doesPackageContainElement(focusPackage, "thaumcraft.PLAN")) {
                return ((IArchitect)FocusEngine.getElement("thaumcraft.PLAN")).getArchitectMOP(stack, world, user);
            }
        }
        return null;
    }

    @Override
    public boolean useBlockHighlight(ItemStack itemStack) {
        return false;
    }

    @Override
    public ArrayList<BlockPos> getArchitectBlocks(ItemStack stack, World world, BlockPos pos, EnumFacing facing, EntityPlayer player) {
        ItemStack focusStack = this.getFocusStack(stack);
        FocusPackage focusPackage = this.getFocusPackage(focusStack);
        if (!focusStack.isEmpty() && focusPackage != null) {
            for (IFocusElement element : focusPackage.nodes) {
                if (element instanceof IArchitect) {
                    return ((IArchitect) element).getArchitectBlocks(stack, world, pos, facing, player);
                }
            }
        }
        return null;
    }

    @Override
    public boolean showAxis(ItemStack stack, World world, EntityPlayer player, EnumFacing facing, IArchitect.EnumAxis axis) {
        ItemStack focusStack = this.getFocusStack(stack);
        FocusPackage focusPackage = this.getFocusPackage(focusStack);
        if (!focusStack.isEmpty() && focusPackage != null) {
            for (IFocusElement element : focusPackage.nodes) {
                if (element instanceof IArchitect) {
                    return ((IArchitect) element).showAxis(stack, world, player, facing, axis);
                }
            }
        }
        return false;
    }

    static {
        Method cooldown;

        try {
            cooldown = CasterManager.class.getDeclaredMethod("isOnCooldown", EntityLivingBase.class);
            cooldown.setAccessible(true);
        } catch (Exception ex) {
            ThaumicAPIMod.LOGGER.error("Failed to access Thaumcraft's CasterManager#isOnCooldown");
            throw new RuntimeException(ex);
        }

        CASTER_IS_ON_COOLDOWN = cooldown;
    }
}
