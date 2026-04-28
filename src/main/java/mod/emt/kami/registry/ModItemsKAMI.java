package mod.emt.kami.registry;


import mod.emt.kami.Kami;
import mod.emt.kami.items.armor.ItemAwakenedArmor;
import mod.emt.kami.items.armor.ItemIchorweaveArmor;
import mod.emt.kami.items.base.ItemBase;
import mod.emt.kami.items.baubles.ItemIchorFocusPouch;
import mod.emt.kami.items.tools.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItemsKAMI {
    public static ItemArmor.ArmorMaterial MATERIAL_ICHORCLOTH = EnumHelper.addArmorMaterial("ICHORCLOTH", Kami.MOD_ID + ":ichorcloth",-1, new int[] {3, 6, 8, 3}, 20, ModSoundsKAMI.EQUIP_ROBE.getSoundEvent(), 3.0F);
    public static Item.ToolMaterial MATERIAL_ICHORIUM = EnumHelper.addToolMaterial("ICHORIUM", 5, -1, 10, 4f, 25);

    public static Item AWAKENED_ICHORIUM_AXE;
    public static Item AWAKENED_ICHORIUM_PICKAXE;
    public static Item AWAKENED_ICHORIUM_SHOVEL;
    public static Item AWAKENED_ICHORIUM_SWORD;
    public static ItemArmor AWAKENED_ICHORWEAVE_BOOTS;
    public static ItemArmor AWAKENED_ICHORWEAVE_HOOD;
    public static ItemArmor AWAKENED_ICHORWEAVE_LEGGINGS;
    public static ItemArmor AWAKENED_ICHORWEAVE_ROBES;
    public static Item FOCUS_POUCH;
    public static Item ICHOR;
    public static Item ICHORIUM_AXE;
    public static Item ICHORIUM_INGOT;
    public static Item ICHORIUM_NUGGET;
    public static Item ICHORIUM_PICKAXE;
    public static Item ICHORIUM_SHOVEL;
    public static Item ICHORIUM_SWORD;
    public static Item ICHORWEAVE_FABRIC;
    public static ItemArmor ICHORWEAVE_BOOTS;
    public static ItemArmor ICHORWEAVE_HOOD;
    public static ItemArmor ICHORWEAVE_LEGGINGS;
    public static ItemArmor ICHORWEAVE_ROBE;

    public static final List<Item> MOD_ITEMS = new ArrayList<>();

    public static void initItems() {
        MOD_ITEMS.add(ICHOR = new ItemBase("ichor", "ichor"));
        MOD_ITEMS.add(ICHORWEAVE_FABRIC = new ItemBase("ichorweave_fabric", "fabricIchor"));
        MOD_ITEMS.add(ICHORIUM_INGOT = new ItemBase("ichorium_ingot", "ingotIchorium"));
        MOD_ITEMS.add(ICHORIUM_NUGGET = new ItemBase("ichorium_nugget", "nuggetIchorium"));
        MOD_ITEMS.add(FOCUS_POUCH = new ItemIchorFocusPouch());
        MOD_ITEMS.add(ICHORWEAVE_HOOD = new ItemIchorweaveArmor("ichorweave_hood", EntityEquipmentSlot.HEAD));
        MOD_ITEMS.add(ICHORWEAVE_ROBE = new ItemIchorweaveArmor("ichorweave_robe", EntityEquipmentSlot.CHEST));
        MOD_ITEMS.add(ICHORWEAVE_LEGGINGS = new ItemIchorweaveArmor("ichorweave_leggings", EntityEquipmentSlot.LEGS));
        MOD_ITEMS.add(ICHORWEAVE_BOOTS = new ItemIchorweaveArmor("ichorweave_boots", EntityEquipmentSlot.FEET));
        MOD_ITEMS.add(AWAKENED_ICHORWEAVE_HOOD = new ItemAwakenedArmor("awakened_ichorweave_hood", EntityEquipmentSlot.HEAD));
        MOD_ITEMS.add(AWAKENED_ICHORWEAVE_ROBES = new ItemAwakenedArmor("awakened_ichorweave_robes", EntityEquipmentSlot.CHEST));
        MOD_ITEMS.add(AWAKENED_ICHORWEAVE_LEGGINGS = new ItemAwakenedArmor("awakened_ichorweave_leggings", EntityEquipmentSlot.LEGS));
        MOD_ITEMS.add(AWAKENED_ICHORWEAVE_BOOTS = new ItemAwakenedArmor("awakened_ichorweave_boots", EntityEquipmentSlot.FEET));
        MOD_ITEMS.add(ICHORIUM_SWORD = new ItemIchoriumSword());
        MOD_ITEMS.add(ICHORIUM_SHOVEL = new ItemIchoriumShovel());
        MOD_ITEMS.add(ICHORIUM_PICKAXE = new ItemIchoriumPickaxe());
        MOD_ITEMS.add(ICHORIUM_AXE = new ItemIchoriumAxe());
        MOD_ITEMS.add(AWAKENED_ICHORIUM_SHOVEL = new ItemAwakenedShovel());
        MOD_ITEMS.add(AWAKENED_ICHORIUM_PICKAXE = new ItemAwakenedPickaxe());
        MOD_ITEMS.add(AWAKENED_ICHORIUM_AXE = new ItemAwakenedAxe());
    }
}
