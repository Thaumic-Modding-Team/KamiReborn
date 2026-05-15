package mod.emt.kami.items.armor;

import com.google.common.collect.Multimap;
import com.invadermonky.thaumicapi.handlers.PlayerMovementAbilityHandler;
import com.invadermonky.thaumicapi.handlers.PlayerMovementAbilityHandler.MovementType;
import mod.emt.kami.Kami;
import mod.emt.kami.handlers.CommonEventHandler;
import mod.emt.kami.registry.ModItemsKAMI;
import mod.emt.kami.registry.ModSoundsKAMI;
import mod.emt.kami.utils.helpers.ItemHelper;
import mod.emt.kami.utils.helpers.PlayerHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.items.baubles.ItemCloudRing;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketPlayerFlagToServer;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ItemAwakenedArmor extends ItemIchorweaveArmor implements ISpecialArmor {
    protected static final String TEXTURE_PATH_1 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_awakened_layer_1.png").toString();
    protected static final String TEXTURE_PATH_2 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_awakened_layer_2.png").toString();
    protected static final String TEXTURE_PATH_DYED_1 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_awakened_layer_1_dyed.png").toString();
    protected static final String TEXTURE_PATH_DYED_2 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_awakened_layer_2_dyed.png").toString();
    protected static final String TEXTURE_PATH_DYED_OVERLAY_1 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_awakened_layer_1_dyed_overlay.png").toString();
    protected static final String TEXTURE_PATH_DYED_OVERLAY_2 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_awakened_layer_2_dyed_overlay.png").toString();

    public static final int POTION_DURATION_MAX = 310;
    public static final int POTION_DURATION_MIN = 301;

    protected static final BiFunction<EntityPlayer, MovementType, Float> MOVEMENT_FUNC = (player, type) -> {
        //TODO: Configs for all of these.
        float boost = 0;
        switch (type) {
            case DRY_GROUND:
                boost = (float) 0.08;
                return player.isSneaking() ? boost / 4.0f : boost;
            case WATER_GROUND:
                boost = (float) Math.max(0.08 / 4.0f, 0.04);
                return player.isSneaking() ? boost / 4.0f : boost;
            case WATER_SWIM:
                boost = (float) 0.04;
                return player.isSneaking() ? boost / 4.0f : boost;
            case JUMP_BEGIN:
                return (float) 0.3;
            case JUMP_FACTOR:
                return (float) 0.03;
            case STEP_HEIGHT:
                return !player.isSneaking() ? (float) 0.75 : 0;
            default:
                return boost;
        }
    };
    protected static final Predicate<EntityPlayer> CONTINUE_FUNC = player -> player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemAwakenedArmor;
    public final UUID MODIFIER_UUID;

    public ItemAwakenedArmor(String unlocName, EntityEquipmentSlot equipmentSlot) {
        super(unlocName, equipmentSlot);
        this.MODIFIER_UUID = new UUID((this.getTranslationKey() + this.armorType).hashCode(), 0);
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            ItemHelper.setUnbreakable(stack);
            items.add(stack);
        }
    }

    @Override
    public @NotNull Multimap<String, AttributeModifier> getAttributeModifiers(@NotNull EntityEquipmentSlot slot, @NotNull ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if(slot == this.armorType) {
            switch (slot) {
                case HEAD:
                    multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(MODIFIER_UUID, "Ichorweave modifier " + this.armorType, 0.2, Constants.AttributeModifierOperation.MULTIPLY));
                    break;
                case CHEST:
                    multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(MODIFIER_UUID, "Ichorweave modifier " + this.armorType, 0.3, Constants.AttributeModifierOperation.MULTIPLY));
                    break;
                case LEGS:
                    multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(MODIFIER_UUID, "Ichorweave modifier " + this.armorType, 0.3, Constants.AttributeModifierOperation.MULTIPLY));
                    break;
                case FEET:
                    multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(MODIFIER_UUID, "Ichorweave modifier " + this.armorType, 0.2, Constants.AttributeModifierOperation.MULTIPLY));
                    break;
            }
        }
        return multimap;
    }

    @Override
    public String getArmorTexture(@NotNull ItemStack stack, @NotNull Entity entity, @NotNull EntityEquipmentSlot slot, @NotNull String type) {
        // If dye is never used on it, it'll use a dyeless texture instead
        if (this.getDyedColor(stack) != getDefaultDyedColorForMeta(stack.getMetadata())) {
            if (slot == EntityEquipmentSlot.LEGS) {
                return type == null ? TEXTURE_PATH_DYED_2 : TEXTURE_PATH_DYED_OVERLAY_2;
            } else {
                return type == null ? TEXTURE_PATH_DYED_1 : TEXTURE_PATH_DYED_OVERLAY_1;
            }
        }

        return slot == EntityEquipmentSlot.LEGS ? TEXTURE_PATH_2 : TEXTURE_PATH_1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack, @NotNull Enchantment enchantment) {
        return enchantment != Enchantments.MENDING && enchantment != Enchantments.UNBREAKING && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag tooltipFlag) {
        tooltip.add(TextFormatting.GREEN + I18n.format("tooltip.kami.awakened"));
        super.addInformation(stack, world, tooltip, tooltipFlag);
        if(Minecraft.getMinecraft().player != null) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            int setCount = this.getEquippedPieces(player);
            tooltip.add("");
            tooltip.add(I18n.format("tooltip.kami.awakened_set.info", setCount));
            if(GuiScreen.isShiftKeyDown()) {
                tooltip.add(I18n.format("tooltip.kami.awakened_set.desc"));
                tooltip.add((hasHelm(player)     ? TextFormatting.LIGHT_PURPLE : TextFormatting.GRAY) + " - " + I18n.format(ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD.getTranslationKey() + ".name"));
                tooltip.add((hasChest(player)    ? TextFormatting.LIGHT_PURPLE : TextFormatting.GRAY) + " - " + I18n.format(ModItemsKAMI.AWAKENED_ICHORWEAVE_ROBE.getTranslationKey() + ".name"));
                tooltip.add((hasLeggings(player) ? TextFormatting.LIGHT_PURPLE : TextFormatting.GRAY) + " - " + I18n.format(ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS.getTranslationKey() + ".name"));
                tooltip.add((hasBoots(player)    ? TextFormatting.LIGHT_PURPLE : TextFormatting.GRAY) + " - " + I18n.format(ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS.getTranslationKey() + ".name"));
            }
        }
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        int priority = 0;
        double ratio = 0;
        if(this.hasArmorSet(player)) {
            priority = 1;
            ratio = 0.2;
        }
        return new ArmorProperties(priority, ratio, Integer.MAX_VALUE);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @NotNull ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @NotNull ItemStack stack, DamageSource source, int damage, int slot) {}

    @Override
    public boolean handleUnblockableDamage(EntityLivingBase entity, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        return this.hasArmorSet(entity);
    }

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
        if(player.isEntityAlive()) {
            switch (this.armorType) {
                case HEAD:
                    this.tickHelmet(world, player, itemStack);
                    break;
                case CHEST:
                    this.tickChestplate(world, player, itemStack);
                    break;
                case LEGS:
                    this.tickLeggings(world, player, itemStack);
                    break;
                case FEET:
                    this.tickBoots(world, player, itemStack);
                    break;
            }
            this.tickArmorSet(world, player, itemStack);
        }
    }

    protected void tickHelmet(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
        //Water Breathing
        boolean activateNightVision = false;
        if(!player.canBreatheUnderwater() && player.isInsideOfMaterial(Material.WATER)) {
            player.setAir(300);
            activateNightVision = true;
        } else {
            activateNightVision |= this.shouldNightVisionActivate(player);
        }

        if(!world.isRemote) {
            PotionEffect effect = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
            if(activateNightVision) {
                if(effect == null || effect.getDuration() <= POTION_DURATION_MIN) {
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 250, 0, false, false));
                }
            } else {
                if(effect != null && effect.getDuration() <= POTION_DURATION_MAX) {
                    player.removePotionEffect(MobEffects.NIGHT_VISION);
                }
            }
        }
    }

    protected boolean shouldNightVisionActivate(EntityLivingBase entityLiving) {
        World world = entityLiving.world;
        BlockPos playerPos = new BlockPos(entityLiving.posX, entityLiving.getEntityBoundingBox().maxY, entityLiving.posZ);
        int playerLight = world.getLightFromNeighbors(playerPos);
        if (world.isThundering()) {
            int skyLight = world.getSkylightSubtracted();
            world.setSkylightSubtracted(7);
            playerLight = world.getLightFromNeighbors(playerPos);
            world.setSkylightSubtracted(skyLight);
        }
        if (playerLight < 7) {
            return true;
        } else {
            RayTraceResult trace = PlayerHelper.rayTrace(entityLiving, 24, 0, true);
            if (trace != null) {
                switch (trace.typeOfHit) {
                    case BLOCK:
                        return world.getLight(trace.getBlockPos().offset(trace.sideHit)) < 7;
                    case MISS:
                    case ENTITY:
                        return world.getLight(trace.getBlockPos()) < 7;
                }
            }
            return false;
        }
    }

    protected void tickChestplate(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
        //Creative Flight
        if(!player.capabilities.allowFlying) {
            player.capabilities.allowFlying = true;
            CommonEventHandler.FLYING_PLAYERS.add(PlayerHelper.getUUIDFromPlayer(player));
        }
        //No fall damage
        player.fallDistance = 0;
    }

    protected void tickLeggings(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
        if(!world.isRemote) {
            PotionEffect effect = player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE);
            if(effect == null || effect.getDuration() <= POTION_DURATION_MIN) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, POTION_DURATION_MAX, 0, false, false));
            }
        } else {
            boolean spacePressed = Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed();
            if (spacePressed && !ItemCloudRing.jumpList.containsKey(player.getName())) {
                ItemCloudRing.jumpList.put(player.getName(), true);
            }

            if (spacePressed && !player.onGround && !player.isInWater() && player.jumpTicks == 0 && ItemCloudRing.jumpList.containsKey(player.getName()) && ItemCloudRing.jumpList.get(player.getName())) {
                FXDispatcher.INSTANCE.drawBamf(player.posX, player.posY + (double)0.5F, player.posZ, 16719133, false, true, EnumFacing.UP);
                player.getEntityWorld().playSound(player.posX, player.posY, player.posZ, ModSoundsKAMI.ITEM_ICHOR_JUMP.getSoundEvent(), SoundCategory.PLAYERS, 0.1F, 1.0F + (float)player.getEntityWorld().rand.nextGaussian() * 0.05F, false);
                ItemCloudRing.jumpList.put(player.getName(), false);
                player.motionY = 0.75F;
                PotionEffect effect = player.getActivePotionEffect(MobEffects.JUMP_BOOST);
                if (effect != null) {
                    player.motionY += (effect.getAmplifier() + 1.0) * 0.1;
                }

                if (player.isSprinting()) {
                    float f = player.rotationYaw * ((float)Math.PI / 180F);
                    player.motionX -= MathHelper.sin(f) * 0.2;
                    player.motionZ += MathHelper.cos(f) * 0.2;
                }

                player.fallDistance = 0.0F;
                PacketHandler.INSTANCE.sendToServer(new PacketPlayerFlagToServer(player, 1));
                ForgeHooks.onLivingJump(player);
            }

            if (player.onGround && player.jumpTicks == 0) {
                ItemCloudRing.jumpList.remove(player.getName());
            }
        }
    }

    protected void tickBoots(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
        //Jump Height and Movement Speed
        if(world.isRemote) {
            boolean apply = !player.capabilities.isFlying && !player.isElytraFlying();
            if (apply && !PlayerMovementAbilityHandler.playerHasAbility(player, MOVEMENT_FUNC, CONTINUE_FUNC)) {
                PlayerMovementAbilityHandler.put(player, MOVEMENT_FUNC, CONTINUE_FUNC);
            } else if (!apply && PlayerMovementAbilityHandler.playerHasAbility(player, MOVEMENT_FUNC, CONTINUE_FUNC)) {
                PlayerMovementAbilityHandler.remove(player, MOVEMENT_FUNC, CONTINUE_FUNC);
            }
        }
    }

    protected void tickArmorSet(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {

    }

    public boolean hasArmorSet(EntityLivingBase player) {
        return this.getEquippedPieces(player) == 4;
    }

    public int getEquippedPieces(EntityLivingBase player) {
        int count = 0;
        if(hasHelm(player))     count++;
        if(hasChest(player))    count++;
        if(hasLeggings(player)) count++;
        if(hasBoots(player))    count++;
        return count;
    }

    public static boolean hasHelm(EntityLivingBase player) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return !stack.isEmpty() && stack.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD;
    }

    public static boolean hasChest(EntityLivingBase player) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        return !stack.isEmpty() && stack.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_ROBE;
    }

    public static boolean hasLeggings(EntityLivingBase player) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        return !stack.isEmpty() && stack.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS;
    }

    public static boolean hasBoots(EntityLivingBase player) {
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        return !stack.isEmpty() && stack.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS;
    }
}
