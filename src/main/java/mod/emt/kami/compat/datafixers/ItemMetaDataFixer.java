package mod.emt.kami.compat.datafixers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import org.jetbrains.annotations.NotNull;

public class ItemMetaDataFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return 1;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        String id = compound.getString("id");

        if("thaumictinkerer:kamiresource".equals(id)) {
            short damage = compound.getShort("Damage");
            if(damage == 0) {
                compound.setString("id", "minecraft:ender_eye");
            } else if(damage == 1) {
                compound.setString("id", "minecraft:ghast_tear");
            } else if(damage == 2) {
                compound.setString("id", "kami:ichor");
            } else if(damage == 3) {
                compound.setString("id", "kami:ichorium_ingot");
            } else if(damage == 4) {
                compound.setString("id", "kami:ichorweave_fabric");
            } else if(damage == 5) {
                compound.setString("id", "kami:ichorium_nugget");
            }
            compound.setShort("Damage", (short) 0);
        }

        return compound;
    }
}
