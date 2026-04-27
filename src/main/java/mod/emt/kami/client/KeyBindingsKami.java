package mod.emt.kami.client;

import baubles.api.BaublesApi;
import mod.emt.kami.Kami;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindingsKami {
    private static final String KEY_CATEGORY = "key.categories." + Kami.MOD_ID;
    public static KeyBinding openFocusPouch;

    public static void init() {
        ClientRegistry.registerKeyBinding(openFocusPouch);
    }

    static {
        openFocusPouch = new KeyBinding("key.kami.open_focus_pouch", new IKeyConflictContext() {
            @Override
            public boolean isActive() {
                EntityPlayer player = Minecraft.getMinecraft().player;
                return player != null && BaublesApi.isBaubleEquipped(player, ModItemsKAMI.FOCUS_POUCH) > -1;
            }

            @Override
            public boolean conflicts(IKeyConflictContext other) {
                return this.isActive() && other.isActive();
            }
        }, Keyboard.KEY_V, KEY_CATEGORY);
    }
}
