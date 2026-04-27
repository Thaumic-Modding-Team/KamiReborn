package mod.emt.kami.handlers;

import mod.emt.kami.client.gui.GuiIchorweavePouch;
import mod.emt.kami.inventory.containers.ContainerIchorweavePouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class GuiHandlerKami implements IGuiHandler {
    public static final int ID_ICHORWEAVE_POUCH = 0;
    public static final int ID_ICHORWEAVE_POUCH_BAUBLE = 1;


    @Override
    public @Nullable Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new ContainerIchorweavePouch(player, player.getHeldItemMainhand());
            case 1:
                return new ContainerIchorweavePouch(player);
        }
        return null;
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new GuiIchorweavePouch(new ContainerIchorweavePouch(player, player.getHeldItemMainhand()));
            case 1:
                return new GuiIchorweavePouch(new ContainerIchorweavePouch(player));
        }
        return null;
    }
}
