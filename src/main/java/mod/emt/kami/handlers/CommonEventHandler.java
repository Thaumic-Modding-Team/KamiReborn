package mod.emt.kami.handlers;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IAreaBreakTool;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
}
