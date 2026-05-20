package mod.emt.kami.config;

import net.minecraftforge.common.config.Config;

public class ConfigHandlerKami {
    public static ArmorCategory armor = new ArmorCategory();
    @Config.Ignore
    public static ToolCategory tool = new ToolCategory();
    public static EnchantmentsCategory enchantments = new EnchantmentsCategory();
    public static IntegrationsCategory integrations = new IntegrationsCategory();

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
        @Config.Name("Construct's Armory")
        @Config.Comment("Enables Construct's Armory Ichorium armor parts. Requires Tinkers' Construct integration to be enabled.")
        public boolean constructsArmory = true;
    }

    public static class ToolCategory {

    }
}
