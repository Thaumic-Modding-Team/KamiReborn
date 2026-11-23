package mod.emt.kami.utils.helpers;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.text.WordUtils;
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

    public static String getDimensionName(int dimensionId) {
        if (!DimensionManager.isDimensionRegistered(dimensionId)) {
            return Integer.toString(dimensionId);
        }
        DimensionType type = DimensionManager.getProviderType(dimensionId);
        if (type == null) {
            return Integer.toString(dimensionId);
        }
        String name = type.getName();
        int[] dims = DimensionManager.getDimensions(type);
        if (dims != null && dims.length > 1) {
            name += " " + dimensionId;
        }
        return WordUtils.capitalizeFully(name.replace("_", " "));
    }
}
