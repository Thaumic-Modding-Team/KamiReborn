package mod.emt.kami.handlers;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IAreaBreakTool;
import mod.emt.kami.registry.ModItemsKAMI;
import mod.emt.kami.utils.helpers.PlayerHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.*;

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
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        //Disable flying for players without chest
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            UUID playerId = PlayerHelper.getUUIDFromPlayer(player);
            if(!player.isCreative() && player.capabilities.allowFlying && FLYING_PLAYERS.contains(playerId)) {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if(chestStack.isEmpty() && chestStack.getItem() != ModItemsKAMI.AWAKENED_ICHORWEAVE_ROBES) {
                    player.capabilities.allowFlying = false;
                    FLYING_PLAYERS.remove(playerId);
                }
            }
        }
    }

//    @SubscribeEvent
//    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
//        if(event.getEntityLiving() instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
//            ItemStack bootStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
//            if(!bootStack.isEmpty() && bootStack.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS) {
//                player.motionY += 0.30;
//            }
//        }
//    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID playerId = PlayerHelper.getUUIDFromPlayer(event.player);
        FLYING_PLAYERS.remove(playerId);
    }
}
