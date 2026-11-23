package mod.emt.kami.handlers;

import mod.emt.kami.client.gui.GuiCelestialTranslocator;
import mod.emt.kami.client.gui.GuiIchorweavePouch;
import mod.emt.kami.inventory.containers.ContainerCelestialTranslocator;
import mod.emt.kami.inventory.containers.ContainerIchorweavePouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class GuiHandlerKami implements IGuiHandler {
    public static final int ID_ICHORWEAVE_POUCH = 0;
    public static final int ID_ICHORWEAVE_POUCH_BAUBLE = 1;
    public static final int ID_CELESTIAL_TELEPORT = 2;
    public static final int ID_CELESTIAL_TRANSLOCATOR = 3;


    @Override
    public @Nullable Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ID_ICHORWEAVE_POUCH:
                return new ContainerIchorweavePouch(player, player.getHeldItemMainhand());
            case ID_ICHORWEAVE_POUCH_BAUBLE:
                return new ContainerIchorweavePouch(player);
            case ID_CELESTIAL_TELEPORT:
                return null;
            case ID_CELESTIAL_TRANSLOCATOR:
                return new ContainerCelestialTranslocator(player, player.getHeldItemMainhand());
        }
        return null;
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ID_ICHORWEAVE_POUCH:
                return new GuiIchorweavePouch(new ContainerIchorweavePouch(player, player.getHeldItemMainhand()));
            case ID_ICHORWEAVE_POUCH_BAUBLE:
                return new GuiIchorweavePouch(new ContainerIchorweavePouch(player));
            case ID_CELESTIAL_TELEPORT:
                return null;
            case ID_CELESTIAL_TRANSLOCATOR:
                return new GuiCelestialTranslocator(new ContainerCelestialTranslocator(player, player.getHeldItemMainhand()));
        }
        return null;
    }
}
