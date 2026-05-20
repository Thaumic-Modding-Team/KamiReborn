package mod.emt.kami.compat.datafixers;

import mod.emt.kami.Kami;
import net.minecraft.block.Block;
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

public class BlockDataFixer implements IFixableData {
    private static final Map<ResourceLocation, ResourceLocation> BLOCK_NAME_MAPPINGS = new HashMap<>();

    static {
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "ichor_block"), new ResourceLocation(Kami.MOD_ID, "ichor_block"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation("tconstruct", "molten_molten_ichorium"), new ResourceLocation("tconstruct", "molten_ichorium"));

        // These blocks do not exist in this mod, turn them to something else
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "bedrock_portal"), new ResourceLocation("minecraft", "bedrock"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation("thaumictinkerer", "warp_gate"), new ResourceLocation(Thaumcraft.MODID, "paving_stone_travel"));
    }

    public BlockDataFixer() {
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
    public void missingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = BLOCK_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Block newBlock = ForgeRegistries.BLOCKS.getValue(newName);
                if (newBlock != null) {
                    entry.remap(newBlock);
                }
            }
        }
    }

    @SubscribeEvent
    public void missingItemBlockMapping(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = BLOCK_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                if (newItem != null) {
                    entry.remap(newItem);
                }
            }
        }
    }
}
