package mod.emt.kami.registry;

import com.invadermonky.thaumicapi.api.ThaumicAPI;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

public class ModEnchantsKAMI {
    public static EnumInfusionEnchantment ETERNAL = ThaumicAPI.registerInfusionEnchantment(
            "KAMI_ETERNAL", 1, "KAMI_ETERNAL_INFUSION", "weapon", "pickaxe", "shovel", "hoe", "axe");
}
