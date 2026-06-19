package mod.emt.kami.compat.tinkers.traits;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTraits;

public class TraitGeocentricDisintegration extends AbstractTrait {
    public TraitGeocentricDisintegration() {
        super("kami_geocentric_disintegration", 0xFF910C);
    }

    @Override
    public boolean canApplyTogether(IToolMod toolmod) {
        // Incompatible with Squeaky, Silk Touch, and Autosmelt
        return !toolmod.getIdentifier().equals(TinkerTraits.squeaky.getIdentifier())
                && !toolmod.getIdentifier().equals(TinkerModifiers.modSilktouch.getIdentifier())
                && !toolmod.getIdentifier().equals(TinkerTraits.autosmelt.getIdentifier())
                && !toolmod.getIdentifier().equals(TinkerModifiers.modAutosmelt.getIdentifier());
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        ItemStack stack = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
        if (ToolHelper.hasCategory(stack, Category.HARVEST)) {
            event.setNewSpeed(event.getOriginalSpeed() * 1000.0F);
        }
    }
}
