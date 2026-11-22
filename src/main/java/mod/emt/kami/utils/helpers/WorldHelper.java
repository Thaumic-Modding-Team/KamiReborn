package mod.emt.kami.utils.helpers;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.jetbrains.annotations.Nullable;

public class WorldHelper {

    @Nullable
    public static World getWorldObject(int dimId, boolean forceLoad) {
        WorldServer world = DimensionManager.getWorld(dimId);
        if(world == null && forceLoad) {
            DimensionManager.initDimension(dimId);
            world = DimensionManager.getWorld(dimId);
        }
        return world;
    }
}
