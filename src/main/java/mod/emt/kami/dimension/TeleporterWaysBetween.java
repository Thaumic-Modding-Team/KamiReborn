package mod.emt.kami.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

// TODO: This is a placeholder to get the item teleport to work
// It should be reworked when the actual way to get to the dimension is done
public class TeleporterWaysBetween implements ITeleporter {
    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        double x = Math.floor(entity.posX) + 0.5D;
        double z = Math.floor(entity.posZ) + 0.5D;
        int y = world.getTopSolidOrLiquidBlock(new net.minecraft.util.math.BlockPos(x, 0, z)).getY();
        entity.setLocationAndAngles(x, y, z, yaw, entity.rotationPitch);
        entity.motionX = 0.0D;
        entity.motionY = 0.0D;
        entity.motionZ = 0.0D;
    }
}
