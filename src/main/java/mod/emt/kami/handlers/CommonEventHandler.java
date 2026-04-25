package mod.emt.kami.handlers;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IAreaBreakTool;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Kami.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockHarvestPost(BlockEvent.HarvestDropsEvent event) {
        if(event.getHarvester() instanceof EntityPlayer && !event.getHarvester().world.isRemote) {
            EntityPlayer player = event.getHarvester();
            World world = player.world;
            ItemStack heldStack = player.getHeldItemMainhand();
            if(heldStack.getItem() instanceof IAreaBreakTool && ((IAreaBreakTool) heldStack.getItem()).spawnDropsAtPlayer(player, heldStack)) {
                //Repositioning drops below player.
                EnumFacing playerFacing = player.getHorizontalFacing();
                double posX = player.posX + (playerFacing.getXOffset() * 0.5);
                double posY = player.posY + 0.2;
                double posZ = player.posZ + (playerFacing.getZOffset() * 0.5);
                for(ItemStack drop : event.getDrops()) {
                    EntityItem entityItem = new EntityItem(world, posX, posY, posZ, drop);
                    entityItem.motionX = 0;
                    entityItem.motionY = 0.1;
                    entityItem.motionZ = 0;
                    entityItem.velocityChanged = true;
                    entityItem.setDefaultPickupDelay();
                    world.spawnEntity(entityItem);
                }
                event.getDrops().clear();
            }
        }
    }
}
