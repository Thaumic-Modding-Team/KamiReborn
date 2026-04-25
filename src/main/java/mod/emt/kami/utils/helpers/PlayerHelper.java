package mod.emt.kami.utils.helpers;

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
    public static RayTraceResult getPlayerTrace(EntityPlayer player, float partialTicks) {
        double reachDistance = getReachDistance(player);
        Vec3d vec3d = player.getPositionEyes(partialTicks);
        Vec3d vec3d1 = player.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.add(vec3d1.x * reachDistance, vec3d1.y * reachDistance, vec3d1.z * reachDistance);
        return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
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
