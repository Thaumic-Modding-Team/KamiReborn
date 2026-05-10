package mod.emt.kami.items.tools;

import com.invadermonky.thaumicapi.api.item.AbstractItemCaster;
import mod.emt.kami.Kami;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.items.casters.CasterManager;

import java.util.Objects;

public class ItemIchoriumCaster extends AbstractItemCaster {
    public ItemIchoriumCaster() {
        super(5);
        this.setRegistryName(Kami.MOD_ID, "ichorium_caster");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
    }

    @Override
    public int getChunkDrainRadius(EntityPlayer entityPlayer, ItemStack itemStack) {
        return 3;
    }

    @Override
    public float getConsumptionModifier(ItemStack itemStack, EntityPlayer player, boolean b) {
        float base = 0.6f;
        if(player != null) {
            base -= CasterManager.getTotalVisDiscount(player);
        }
        return Math.max(0, base);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
