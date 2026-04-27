package mod.emt.kami.registry;

import mod.emt.kami.Kami;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum ModSoundsKAMI {
    EQUIP_BAUBLE("equip.bauble"),
    EQUIP_ROBE("equip.robe"),
    ITEM_ICHOR_BURY("item.ichor.bury"),
    ITEM_ICHOR_TOGGLE("item.ichor.toggle"),
    UNEQUIP_BAUBLE("unequip.bauble");

    private final SoundEvent soundEvent;

    ModSoundsKAMI(String path) {
        ResourceLocation resourceLocation = new ResourceLocation(Kami.MOD_ID, path);
        this.soundEvent = new SoundEvent(resourceLocation);
        this.soundEvent.setRegistryName(resourceLocation);
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
}
