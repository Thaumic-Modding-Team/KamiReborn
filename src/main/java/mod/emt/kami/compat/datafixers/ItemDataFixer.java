package mod.emt.kami.compat.datafixers;

import mod.emt.kami.Kami;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import thaumcraft.Thaumcraft;

import java.util.HashMap;
import java.util.Map;

public class ItemDataFixer implements IFixableData {
    private static final Map<ResourceLocation, ResourceLocation> ITEM_NAME_MAPPINGS  = new HashMap<>();

    static {
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "focus_pouch"), new ResourceLocation(Kami.MOD_ID, "focus_pouch"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichor_block"), new ResourceLocation(Kami.MOD_ID, "ichor_block"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichor_boots"), new ResourceLocation(Kami.MOD_ID, "ichorweave_boots"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichor_chest"), new ResourceLocation(Kami.MOD_ID, "ichorweave_robe"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichor_helm"), new ResourceLocation(Kami.MOD_ID, "ichorweave_hood"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichor_legs"), new ResourceLocation(Kami.MOD_ID, "ichorweave_leggings"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichorium_axe"), new ResourceLocation(Kami.MOD_ID, "ichorium_axe"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichorium_axe_adv"), new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_axe"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichorium_pick"), new ResourceLocation(Kami.MOD_ID, "ichorium_pickaxe"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichorium_pick_adv"), new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_pickaxe"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichorium_shovel"), new ResourceLocation(Kami.MOD_ID, "ichorium_shovel"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichorium_shovel_adv"), new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_shovel"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichorium_sword"), new ResourceLocation(Kami.MOD_ID, "ichorium_sword"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichorium_sword_adv"), new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_sword"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "kami_boots"), new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_boots"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "kami_chest"), new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_robe"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "kami_helm"), new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_hood"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "kami_legs"), new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_leggings"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "kamiresource"), new ResourceLocation(Kami.MOD_ID, "ichor"));

        // These items do not exist in this mod, turn them to something else
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "bedrock_portal"), new ResourceLocation("minecraft", "bedrock"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "placement_mirror"), new ResourceLocation(Thaumcraft.MODID, "hand_mirror"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "proto_clay"), new ResourceLocation("minecraft", "clay_ball"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "sky_pearl"), new ResourceLocation("minecraft", "ender_pearl"));
        ITEM_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "warp_gate"), new ResourceLocation(Thaumcraft.MODID, "paving_stone_travel"));
    }

    public ItemDataFixer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getFixVersion() {
        return 1;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        return compound;
    }

    @SubscribeEvent
    public void missingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = ITEM_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                if (newItem != null) {
                    entry.remap(newItem);
                }
            }
        }
    }
}
