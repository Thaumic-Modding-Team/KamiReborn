package mod.emt.kami.config;

import mod.emt.kami.Kami;
import mod.emt.kami.recipe.elementalfire.ElementalFireRecipeHandler;
import mod.emt.kami.registry.ModRecipesKAMI;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Kami.MOD_ID)
public class ConfigHandlerKami {
    public static ArmorCategory armor = new ArmorCategory();
    public static ToolCategory tools = new ToolCategory();
    public static ElementalFireCategory elemental_fire = new ElementalFireCategory();
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

    public static class ElementalFireCategory {
        @Config.Name("!!Disable Research!!")
        @Config.Comment("Disables elemental fire research.")
        public boolean _disableFireResearch = false;

        @Config.Name("!!Disable Tooltip!!")
        @Config.Comment("Disables the warning tooltip on bottles of elemental fire.")
        public boolean _disableTooltip = false;

        @Config.Name("!!Disable Fire Spread!!")
        @Config.Comment("Disables elemental fire spread logic.")
        public boolean _disableFireSpread = false;

        @Config.Name("!!Reset Recipes on Reload")
        @Config.Comment({
                "Enables elemental fire recipe reloading whenever the configuration is changed. This will result in",
                "non-configuration transformation recipes disappearing whenever the config is changed."
        })
        public boolean _reloadReset = false;

        @Config.RequiresMcRestart
        @Config.Name("Aer Transformations")
        @Config.Comment({
                "Aer Elemental Fire transformation recipes.",
                "Format:",
                "targetId:targetMeta;transformedId:transformedMeta;destroyChance",
                "  targetId - The target block registry id",
                "  targetMeta - [Optional] The target block metadata",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed",
                "targetOreDict;transformedId:transformedMeta;destroyChance",
                "  targetOreDict - The target block ore dictionary",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed"
        })
        public String[] aerTransformations = new String[] {
                "dirt;minecraft:sand;0",
                "grass;minecraft:sand;0",
                "logWood;minecraft:sand;0",
                "treeLeaves;minecraft:sandstone;0",
                "minecraft:ice;minecraft:glass;0",
                "minecraft:packed_ice;minecraft:glass;0"
        };

        @Config.RequiresMcRestart
        @Config.Name("Aqua Transformations")
        @Config.Comment({
                "Aqua Elemental Fire transformation recipes.",
                "Format:",
                "targetId:targetMeta;transformedId:transformedMeta;destroyChance",
                "  targetId - The target block registry id",
                "  targetMeta - [Optional] The target block metadata",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed",
                "targetOreDict;transformedId:transformedMeta;destroyChance",
                "  targetOreDict - The target block ore dictionary",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed"
        })
        public String[] aquaTransformations = new String[] {
                "netherrack;minecraft:snow;0",
                "sand;minecraft:packed_ice;0",
                "minecraft:lava;minecraft:obsidian;0",
                "minecraft:soul_sand;minecraft:packed_ice;0"
        };

        @Config.RequiresMcRestart
        @Config.Name("Ignis Transformations")
        @Config.Comment({
                "Ignis Elemental Fire transformation recipes.",
                "Format:",
                "targetId:targetMeta;transformedId:transformedMeta;destroyChance",
                "  targetId - The target block registry id",
                "  targetMeta - [Optional] The target block metadata",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed",
                "targetOreDict;transformedId:transformedMeta;destroyChance",
                "  targetOreDict - The target block ore dictionary",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed"
        })
        public String[] ignisTransformations = new String[] {
                "cobblestone;minecraft:netherrack;0",
                "dirt;minecraft:netherrack;0",
                "grass;minecraft:netherrack;0",
                "sand;minecraft:soul_sand;0",
                "stone;minecraft:netherrack;0",
                "minecraft:clay;minecraft:glowstone;0",
                "minecraft:water;minecraft:lava;0"
        };

        @Config.RequiresMcRestart
        @Config.Name("Ordo Transformations")
        @Config.Comment({
                "Ordo Elemental Fire transformation recipes.",
                "Format:",
                "targetId:targetMeta;transformedId:transformedMeta;destroyChance",
                "  targetId - The target block registry id",
                "  targetMeta - [Optional] The target block metadata",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed",
                "targetOreDict;transformedId:transformedMeta;destroyChance",
                "  targetOreDict - The target block ore dictionary",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed"
        })
        public String[] ordoTransformations = new String[] {
                "blockGlass;minecraft:glass;100",
                "cobblestone;minecraft:cobblestone;100",
                "gravel;minecraft:gravel;100",
                "paneGlass;minecraft:glass_pane;100",
                "sand;minecraft:sand;100",
                "stone;minecraft:stone;100"
        };

        @Config.RequiresMcRestart
        @Config.Name("Terra Transformations")
        @Config.Comment({
                "Terra Elemental Fire transformation recipes.",
                "Format:",
                "targetId:targetMeta;transformedId:transformedMeta;destroyChance",
                "  targetId - The target block registry id",
                "  targetMeta - [Optional] The target block metadata",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed",
                "targetOreDict;transformedId:transformedMeta;destroyChance",
                "  targetOreDict - The target block ore dictionary",
                "  transformedId - The transformed block registry id",
                "  transformedMeta - [Optional] The transformed block metadata",
                "  destroyChance - The chance (0-100) the block will be destroyed instead of transformed"
        })
        public String[] terraTransformations = new String[] {
                "cobblestone;minecraft:dirt;0",
                "gravel;minecraft:clay;0",
                "sand;minecraft:dirt;0",
                "stone;minecraft:dirt;0",
                "minecraft:cactus;minecraft:log;0",
                "minecraft:snow_layer;minecraft:tallgrass;0"
        };
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
        @Config.Name("Tinkers' Construct: Geocentric Disintegration Trait")
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
                if(elemental_fire._reloadReset) {
                    ElementalFireRecipeHandler.clearAllRecipes();
                    ModRecipesKAMI.initElementalFireRecipes();
                }
            }
        }
    }
}
