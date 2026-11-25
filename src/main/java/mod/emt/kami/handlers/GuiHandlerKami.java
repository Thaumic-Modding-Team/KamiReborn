package mod.emt.kami.handlers;

import mod.emt.kami.client.gui.GuiCelestialTeleport;
import mod.emt.kami.client.gui.GuiCelestialTranslocator;
import mod.emt.kami.client.gui.GuiIchorweavePouch;
import mod.emt.kami.inventory.containers.ContainerCelestialTeleport;
import mod.emt.kami.inventory.containers.ContainerCelestialTranslocator;
import mod.emt.kami.inventory.containers.ContainerIchorweavePouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class GuiHandlerKami implements IGuiHandler {
    public static final int ID_ICHORWEAVE_POUCH_MH = 0;
    public static final int ID_ICHORWEAVE_POUCH_OH = 1;
    public static final int ID_ICHORWEAVE_POUCH_BAUBLE = 2;
    public static final int ID_CELESTIAL_TELEPORT_MH = 3;
    public static final int ID_CELESTIAL_TELEPORT_OH = 4;
    public static final int ID_CELESTIAL_TRANSLOCATOR_MH = 5;
    public static final int ID_CELESTIAL_TRANSLOCATOR_OH = 6;


    @Override
    public @Nullable Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ID_ICHORWEAVE_POUCH_MH:
                return new ContainerIchorweavePouch(player, player.getHeldItemMainhand());
            case ID_ICHORWEAVE_POUCH_OH:
                return new ContainerIchorweavePouch(player, player.getHeldItemOffhand());
            case ID_ICHORWEAVE_POUCH_BAUBLE:
                return new ContainerIchorweavePouch(player);
            case ID_CELESTIAL_TELEPORT_MH:
                return new ContainerCelestialTeleport(player.getHeldItemMainhand(), EntityEquipmentSlot.MAINHAND);
            case ID_CELESTIAL_TELEPORT_OH:
                return new ContainerCelestialTeleport(player.getHeldItemOffhand(), EntityEquipmentSlot.OFFHAND);
            case ID_CELESTIAL_TRANSLOCATOR_MH:
                return new ContainerCelestialTranslocator(player, player.getHeldItemMainhand());
            case ID_CELESTIAL_TRANSLOCATOR_OH:
                return new ContainerCelestialTranslocator(player, player.getHeldItemOffhand());
        }
        return null;
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ID_ICHORWEAVE_POUCH_MH:
                return new GuiIchorweavePouch(new ContainerIchorweavePouch(player, player.getHeldItemMainhand()));
            case ID_ICHORWEAVE_POUCH_OH:
                return new GuiIchorweavePouch(new ContainerIchorweavePouch(player, player.getHeldItemOffhand()));
            case ID_ICHORWEAVE_POUCH_BAUBLE:
                return new GuiIchorweavePouch(new ContainerIchorweavePouch(player));
            case ID_CELESTIAL_TELEPORT_MH:
                return new GuiCelestialTeleport(new ContainerCelestialTeleport(player.getHeldItemMainhand(), EntityEquipmentSlot.MAINHAND));
            case ID_CELESTIAL_TELEPORT_OH:
                return new GuiCelestialTeleport(new ContainerCelestialTeleport(player.getHeldItemOffhand(), EntityEquipmentSlot.OFFHAND));
            case ID_CELESTIAL_TRANSLOCATOR_MH:
                return new GuiCelestialTranslocator(new ContainerCelestialTranslocator(player, player.getHeldItemMainhand()));
            case ID_CELESTIAL_TRANSLOCATOR_OH:
                return new GuiCelestialTranslocator(new ContainerCelestialTranslocator(player, player.getHeldItemOffhand()));
        }
        return null;
    }
}
