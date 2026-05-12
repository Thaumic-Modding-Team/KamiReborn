package mod.emt.kami.compat.tinkers.modifiers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class ModDivineMandate extends ModifierTrait {
    public ModDivineMandate() {
        super("kami_divine_mandate", 0xFF910C);
        MinecraftForge.EVENT_BUS.register(this);

        addAspects(new ModifierAspect.SingleAspect(this), new ModifierAspect.DataAspect(this), ModifierAspect.harvestOnly);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockHarvestPost(BlockEvent.HarvestDropsEvent event) {
        if(event.getHarvester() instanceof EntityPlayer && !event.getHarvester().world.isRemote) {
            EntityPlayer player = event.getHarvester();
            World world = player.world;
            ItemStack heldStack = player.getHeldItemMainhand();
            if(TinkerUtil.hasModifier(TagUtil.getTagSafe(heldStack), this.identifier)) {
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
