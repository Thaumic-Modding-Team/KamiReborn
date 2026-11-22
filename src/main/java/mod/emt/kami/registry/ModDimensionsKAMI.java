package mod.emt.kami.registry;

import mod.emt.kami.dimension.WaysBetweenProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class ModDimensionsKAMI {
    // TODO: Make ID configurable
    public static final int WAYS_BETWEEN_ID = 42;
    public static DimensionType WAYS_BETWEEN;

    public static void init() {
        registerDimensionTypes();
        registerDimensions();
    }

    private static void registerDimensionTypes() {
        WAYS_BETWEEN = DimensionType.register("ways_between", "_ways_between", WAYS_BETWEEN_ID, WaysBetweenProvider.class, false);
    }

    private static void registerDimensions() {
        DimensionManager.registerDimension(WAYS_BETWEEN_ID, WAYS_BETWEEN);
    }
}