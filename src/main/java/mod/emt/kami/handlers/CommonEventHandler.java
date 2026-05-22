package mod.emt.kami.handlers;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IAreaBreakTool;
import mod.emt.kami.items.armor.ItemAwakenedArmor;
import mod.emt.kami.items.armor.ItemIchorweaveArmor;
import mod.emt.kami.items.tools.ItemIchoriumAxe;
import mod.emt.kami.items.tools.ItemIchoriumPickaxe;
import mod.emt.kami.items.tools.ItemIchoriumShovel;
import mod.emt.kami.items.tools.ItemIchoriumSword;
import mod.emt.kami.registry.ModItemsKAMI;
import mod.emt.kami.utils.helpers.PlayerHelper;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import thaumcraft.common.items.baubles.ItemCloudRing;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Kami.MOD_ID)
public class CommonEventHandler {
    public static final Set<UUID> FLYING_PLAYERS = new HashSet<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockHarvestPost(BlockEvent.HarvestDropsEvent event) {
        if(event.getHarvester() instanceof EntityPlayer && !event.getHarvester().world.isRemote) {
            EntityPlayer player = event.getHarvester();
            World world = player.world;
            ItemStack heldStack = player.getHeldItemMainhand();
            if(heldStack.getItem() instanceof IAreaBreakTool && ((IAreaBreakTool) heldStack.getItem()).spawnDropsAtPlayer(player, heldStack)) {
                //Repositioning drops below player.
                double posX = player.posX;
                double posY = player.posY + 0.5;
                double posZ = player.posZ;
                for(ItemStack drop : event.getDrops()) {
                    EntityItem entityItem = new EntityItem(world, posX, posY, posZ, drop);
                    entityItem.motionX = 0;
                    entityItem.motionZ = 0;
                    world.spawnEntity(entityItem);
                }
                event.getDrops().clear();
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreakSpeed(BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if(!helm.isEmpty() && helm.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD) {
            double newSpeed = event.getOriginalSpeed();
            if(player.isInsideOfMaterial(Material.WATER)) {
                //Underwater break speed is 20% of normal speed
                newSpeed = event.getOriginalSpeed() / 0.2;
            }
            if(!player.onGround) {
                //Airborne break speed is 20% of normal speed
                newSpeed = event.getOriginalSpeed() / 0.2;
            }

            if(newSpeed > event.getNewSpeed()) {
                event.setNewSpeed((float) newSpeed);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        float distance = event.getDistance();
        ItemStack legs = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack boots = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if(event.getEntityLiving() instanceof EntityPlayer) {
            if (!legs.isEmpty() && legs.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS && ItemCloudRing.jumpList.containsKey(event.getEntityLiving().getName())) {
                distance = (distance / 3.0f) - 2.0f;
            }
        }
        if(!boots.isEmpty() && boots.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS) {
            distance -= 2.0f;
        }
        event.setDistance(Math.max(0, distance));
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        //Disable flying for players without chest
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            UUID playerId = PlayerHelper.getUUIDFromPlayer(player);
            if(!player.isCreative() && player.capabilities.allowFlying && FLYING_PLAYERS.contains(playerId)) {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if(chestStack.isEmpty() && chestStack.getItem() != ModItemsKAMI.AWAKENED_ICHORWEAVE_ROBE) {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                    FLYING_PLAYERS.remove(playerId);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEquipmentChanged(LivingEquipmentChangeEvent event) {
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        if(event.getSlot() == EntityEquipmentSlot.HEAD) {
            if(from.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD && (to.isEmpty() && to.getItem() != ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD)) {
                PotionEffect effect = event.getEntityLiving().getActivePotionEffect(MobEffects.NIGHT_VISION);
                if(effect != null && effect.getDuration() <= ItemAwakenedArmor.POTION_DURATION_MAX) {
                    event.getEntityLiving().removePotionEffect(MobEffects.NIGHT_VISION);
                }
            }
        } else if(event.getSlot() == EntityEquipmentSlot.LEGS) {
            if(from.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS && (to.isEmpty() && to.getItem() != ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS)) {
                PotionEffect effect = event.getEntityLiving().getActivePotionEffect(MobEffects.FIRE_RESISTANCE);
                if(effect != null && effect.getDuration() <= ItemAwakenedArmor.POTION_DURATION_MAX) {
                    event.getEntityLiving().removePotionEffect(MobEffects.FIRE_RESISTANCE);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        ItemStack leggings = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if(!leggings.isEmpty() && leggings.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS) {
            if(event.getSource() == DamageSource.HOT_FLOOR) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
     public static void onPotionApplied(PotionEvent.PotionApplicableEvent event) {
        if(event.getResult() != Event.Result.DENY) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            ItemStack leggings = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            ItemStack boots = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            //Leggings remove Poison and Wither.
            if(!leggings.isEmpty() && leggings.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS) {
                PotionEffect effect = event.getPotionEffect();
                if(effect.getPotion() == MobEffects.POISON || effect.getPotion() == MobEffects.WITHER) {
                    event.setResult(Event.Result.DENY);
                }
            }
            //Boots remove Slowness
            if(!boots.isEmpty() && boots.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS) {
                PotionEffect effect = event.getPotionEffect();
                if(effect.getPotion() == MobEffects.SLOWNESS) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
     }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
        UUID playerId = PlayerHelper.getUUIDFromPlayer(event.player);
        FLYING_PLAYERS.remove(playerId);
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        // Unbreaking still applies to items on anvils regardless of whether the items accept it in enchantment tables or not
        // This event should fix that
        if (event.getLeft().isEmpty() || event.getRight().isEmpty()) {
            return;
        }

        if (event.getLeft().getItem() instanceof ItemIchoriumAxe || event.getLeft().getItem() instanceof ItemIchoriumPickaxe || event.getLeft().getItem() instanceof ItemIchoriumShovel
                || event.getLeft().getItem() instanceof ItemIchoriumSword || event.getLeft().getItem() instanceof ItemIchorweaveArmor) {
            if (EnchantmentHelper.getEnchantments(event.getRight()).keySet().stream().anyMatch(e -> e == Enchantments.UNBREAKING)) {
                event.setCanceled(true);
            }
        }
    }
}
