package mod.emt.kami.config;

import mod.emt.kami.Kami;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Kami.MOD_ID)
public class ConfigHandlerKami {
    public static ArmorCategory armor = new ArmorCategory();
    @Config.Ignore
    public static ToolCategory tools = new ToolCategory();
    public static EnchantmentsCategory enchantments = new EnchantmentsCategory();
    public static IntegrationsCategory integrations = new IntegrationsCategory();
    public static MiscCategory misc = new MiscCategory();

    public static class ArmorCategory {
        @Config.Name("Awakened Boots: Movement Bonus")
        @Config.Comment("The movement speed bonus granted when wearing the Ichorweave Boots of the Biosphere.")
        public double bootsMovementBonus = 0.08;

        @Config.Name("Awakened Boots: Swim Bonus")
        @Config.Comment("The swim speed bonus granted when wearing the Ichorweave Boots of the Biosphere.")
        public double bootsSwimBonus = 0.04;

        @Config.Name("Awakened Boots: Jump Boost")
        @Config.Comment("The jump height boost granted when wearing the Ichorweave Boots of the Biosphere.")
        public double bootsJumpBoost = 0.3;

        @Config.Name("Awakened Boots: Jump Factor")
        @Config.Comment("The forward momentum speed bonus granted when jumping while wearing the Ichorweave Boots of the Biosphere.")
        public double bootsJumpFactor = 0.03;
    }

    public static class EnchantmentsCategory {
        @Config.RequiresMcRestart
        @Config.Name("Infusion Enchant: Eternal")
        @Config.Comment("Enables the eternal infusion enchant, which makes items unbreakable.")
        public boolean eternal = true;
    }

    public static class IntegrationsCategory {
        @Config.RequiresMcRestart
        @Config.Name("Tinkers' Construct")
        @Config.Comment("Enables Tinkers' Construct Ichorium tool parts and Divine Mandate modifier.")
        public boolean tinkersConstruct = true;

        @Config.RequiresMcRestart
        @Config.Name("Tinkers' Construct: God Complex Parts")
        @Config.Comment("The amount of parts needed for God Complex to make the item unbreakable.")
        public int godComplexParts = 2;

        @Config.RequiresMcRestart
        @Config.Name("Tinkers' Construct: Overpowered Ichorium")
        @Config.Comment("Makes the Ichorium material overpowered to be in the same tier as endgame PlusTiC materials.")
        public boolean overpoweredIchorium = false;

        @Config.RequiresMcRestart
        @Config.Name("Tinkers' Construct: Overpowered Ichorium Trait")
        @Config.Comment("Makes the Ichorium tool material use Geocentric Disintegration instead of God Complex. This trait makes tools mine blocks instantly regardless of hardness.")
        public boolean overpoweredIchoriumTrait = false;

        @Config.RequiresMcRestart
        @Config.Name("Construct's Armory")
        @Config.Comment("Enables Construct's Armory Ichorium armor parts. Requires Tinkers' Construct integration to be enabled.")
        public boolean constructsArmory = true;
    }

    public static class MiscCategory {
        @Config.RequiresMcRestart
        @Config.Name("Alternate Research Background")
        @Config.Comment("Enables an alternate much less colorful research background, highly recommended if the default background is too bright and hard to look at.")
        public boolean alternateResearchBackground = false;
    }

    public static class ToolCategory {
        @Config.RangeDouble(min = 0.0, max = 1.0)
        @Config.Name("Awakened Sword: Titanslayer Damage")
        @Config.Comment({
                "The amount of damage, based on the percentage of max health of the target, a fully charged Titanslayer",
                "strike will deal. A value of 0.2 equates to 20% of the target's maximum health."
        })
        public double titanSlayerDamage = 0.2;
    }

    @Mod.EventBusSubscriber
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(Kami.MOD_ID)) {
                ConfigManager.sync(Kami.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
