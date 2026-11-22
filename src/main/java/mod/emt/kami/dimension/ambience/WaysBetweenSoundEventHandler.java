package mod.emt.kami.dimension.ambience;

import mod.emt.kami.Kami;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraftforge.client.EnumHelperClient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thecodex6824.thaumicaugmentation.ThaumicAugmentation;
import thecodex6824.thaumicaugmentation.api.TASounds;
import thecodex6824.thaumicaugmentation.client.event.IClientResourceReloadDispatcher;
import thecodex6824.thaumicaugmentation.common.util.IResourceReloadDispatcher;

@SideOnly(Side.CLIENT)
public class WaysBetweenSoundEventHandler {
    public static MusicTicker.MusicType WAYS_BETWEEN_AMBIENCE;
    private static WaysBetweenSoundTicker waysBetweenSound = null;

    public static void init() {
        WAYS_BETWEEN_AMBIENCE = EnumHelperClient.addMusicType(Kami.MOD_ID + ":ways_between_ambience",
                TASounds.EMPTINESS_AMBIENCE, Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (waysBetweenSound == null) {
                waysBetweenSound = new WaysBetweenSoundTicker(Minecraft.getMinecraft());
                IResourceReloadDispatcher dispatcher = ThaumicAugmentation.proxy.getResourceReloadDispatcher();
                if (dispatcher instanceof IClientResourceReloadDispatcher) {
                    ((IClientResourceReloadDispatcher) dispatcher).registerListener(waysBetweenSound);
                }
            }

            waysBetweenSound.update();
        }
    }
}
