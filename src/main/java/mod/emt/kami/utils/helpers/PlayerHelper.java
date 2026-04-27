package mod.emt.kami.utils.helpers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class PlayerHelper {

    public static double getReachDistance(EntityPlayer player) {
        IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
        return instance != null ? instance.getAttributeValue() : EntityPlayer.REACH_DISTANCE.getDefaultValue();
    }

    @Nullable
    public static RayTraceResult rayTrace(EntityLivingBase player, float partialTicks) {
        return rayTrace(player, player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue(), partialTicks);
    }

    @Nullable
    public static RayTraceResult rayTrace(EntityLivingBase entityLiving, double blockReachDistance, float partialTicks) {
        return rayTrace(entityLiving, blockReachDistance, partialTicks, false);
    }

    @Nullable
    public static RayTraceResult rayTrace(EntityLivingBase entityLiving, double blockReachDistance, float partialTicks, boolean stopOnLiquid) {
        Vec3d height = entityLiving.getPositionEyes(partialTicks);
        Vec3d look = entityLiving.getLook(partialTicks);
        Vec3d reach = height.add(look.x * blockReachDistance, look.y * blockReachDistance, look.z * blockReachDistance);
        return entityLiving.world.rayTraceBlocks(height, reach, stopOnLiquid, false, true);
    }

    public static EnumFacing getPlayerFacing(EntityPlayer player) {
        Vec3d lookVec = player.getLookVec();
        double absX = Math.abs(lookVec.x);
        double absY = Math.abs(lookVec.y);
        double absZ = Math.abs(lookVec.z);
        if(absY > absX && absY > absZ) {
            return absY < 0 ? EnumFacing.DOWN : EnumFacing.UP;
        } else if (absX > absZ) {
            return absX < 0 ? EnumFacing.WEST : EnumFacing.EAST;
        } else {
            return absZ < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH;
        }
    }
}
