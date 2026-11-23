package mod.emt.kami.api.tile;

import net.minecraft.world.IWorldNameable;

public interface INameableTile extends IWorldNameable {
    void setCustomName(String displayName);
}
