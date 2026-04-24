package mod.emt.kami.registry;


import mod.emt.kami.Kami;
import mod.emt.kami.items.tools.ItemIchoriumAxe;
import mod.emt.kami.items.tools.ItemIchoriumPickaxe;
import mod.emt.kami.items.tools.ItemIchoriumShovel;
import mod.emt.kami.items.tools.ItemIchoriumSword;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItemsKAMI {
    public static ItemArmor.ArmorMaterial MATERIAL_ICHORCLOTH = EnumHelper.addArmorMaterial("ICHORCLOTH", Kami.MOD_ID + ":ichorcloth",-1, new int[] {3, 6, 8, 3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 3.0f);
    public static Item.ToolMaterial MATERIAL_ICHORIUM = EnumHelper.addToolMaterial("ICHORIUM", 5, -1, 10, 5f, 25);

    public static Item ICHORIUM_AXE;
    public static Item ICHORIUM_PICKAXE;
    public static Item ICHORIUM_SHOVEL;
    public static Item ICHORIUM_SWORD;
    public static Item AWAKENED_ICHORIUM_AXE;
    public static Item AWAKENED_ICHORIUM_PICKAXE;
    public static Item AWAKENED_ICHORIUM_SHOVEL;
    public static Item AWAKENED_ICHORIUM_SWORD;

    public static final List<Item> MOD_ITEMS = new ArrayList<>();

    public static void initItems() {
        MOD_ITEMS.add(ICHORIUM_SWORD = new ItemIchoriumSword());
        MOD_ITEMS.add(ICHORIUM_SHOVEL = new ItemIchoriumShovel());
        MOD_ITEMS.add(ICHORIUM_PICKAXE = new ItemIchoriumPickaxe());
        MOD_ITEMS.add(ICHORIUM_AXE = new ItemIchoriumAxe());
    }
}
