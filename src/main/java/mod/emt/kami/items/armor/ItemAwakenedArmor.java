package mod.emt.kami.items.armor;

import com.google.common.collect.Multimap;
import mod.emt.kami.handlers.CommonEventHandler;
import mod.emt.kami.utils.helpers.PlayerHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemAwakenedArmor extends ItemIchorweaveArmor {
    public final UUID KNOCKBACK_UUID;

    public ItemAwakenedArmor(String unlocName, EntityEquipmentSlot equipmentSlot) {
        super(unlocName, equipmentSlot);
        this.KNOCKBACK_UUID = new UUID((this.getTranslationKey() + this.armorType + "knockback").hashCode(), 0);
    }

    @Override
    public @NotNull Multimap<String, AttributeModifier> getAttributeModifiers(@NotNull EntityEquipmentSlot slot, @NotNull ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if(slot == this.armorType) {
            switch (slot) {
                case HEAD:
                    multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(KNOCKBACK_UUID, "Ichorweave modifier " + this.armorType, 0.2, Constants.AttributeModifierOperation.MULTIPLY));
                    break;
                case CHEST:
                    multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(KNOCKBACK_UUID, "Ichorweave modifier " + this.armorType, 0.3, Constants.AttributeModifierOperation.MULTIPLY));
                    break;
                case LEGS:
                    multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(KNOCKBACK_UUID, "Ichorweave modifier " + this.armorType, 0.3, Constants.AttributeModifierOperation.MULTIPLY));
                    break;
                case FEET:
                    multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(KNOCKBACK_UUID, "Ichorweave modifier " + this.armorType, 0.2, Constants.AttributeModifierOperation.MULTIPLY));
                    break;
            }
        }
        return multimap;
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
                case FEET:
                    this.tickLeggings(world, player, itemStack);
                    break;
                case LEGS:
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
                if(effect == null || effect.getDuration() <= 241) {
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 250, 0, false, false));
                }
            } else {
                if(effect != null && effect.getDuration() <= 250) {
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
            world.setSkylightSubtracted(10);
            playerLight = world.getLightFromNeighbors(playerPos);
            world.setSkylightSubtracted(skyLight);
        }
        if (playerLight < 10) {
            return true;
        } else {
            RayTraceResult trace = PlayerHelper.rayTrace(entityLiving, 24, 0, true);
            if (trace != null) {
                switch (trace.typeOfHit) {
                    case BLOCK:
                        return world.getLight(trace.getBlockPos().offset(trace.sideHit)) < 10;
                    case MISS:
                    case ENTITY:
                        return world.getLight(trace.getBlockPos()) < 10;
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

    }

    protected void tickBoots(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
    }

    protected void tickArmorSet(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {

    }
}
