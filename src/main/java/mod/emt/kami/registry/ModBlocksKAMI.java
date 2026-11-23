package mod.emt.kami.registry;

import mod.emt.kami.Kami;
import mod.emt.kami.blocks.BlockCelestialGateway;
import mod.emt.kami.blocks.BlockMaterial;
import mod.emt.kami.blocks.BlockMaterialCopy;
import mod.emt.kami.blocks.BlockVoidRedstone;
import mod.emt.kami.tiles.TileCelestialGateway;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModBlocksKAMI {
    public static Block CELESTIAL_GATEWAY;
    public static Block ICHOR_BLOCK;
    public static Block ICHORIUM_BLOCK;

    public static final List<Block> MOD_BLOCKS = new ArrayList<>();
    public static final List<BlockMaterialCopy> ORE_BLOCKS = new ArrayList<>();

    public static void initBlocks() {
        MOD_BLOCKS.add(ICHOR_BLOCK = new BlockMaterial("ichor_block", Material.IRON, MapColor.GOLD, 5.0F, SoundType.METAL, "blockIchor"));
        MOD_BLOCKS.add(ICHORIUM_BLOCK = new BlockMaterial("ichorium_block", Material.IRON, MapColor.GOLD, 5.0F, SoundType.METAL, "blockIchorium"));
        MOD_BLOCKS.add(CELESTIAL_GATEWAY = new BlockCelestialGateway());
        addVoidOre("coal", Blocks.COAL_ORE, 0);
        addVoidOre("iron", Blocks.IRON_ORE, 0);
        addVoidOre("gold", Blocks.GOLD_ORE, 0);
        addVoidOre("lapis_lazuli", Blocks.LAPIS_ORE, 0);
        addVoidOre(new BlockVoidRedstone());
        addVoidOre("quartz", Blocks.QUARTZ_ORE, 0);
        addVoidOre("diamond", Blocks.DIAMOND_ORE, 0);
    }

    public static void addVoidOre(BlockMaterialCopy block) {
        MOD_BLOCKS.add(block);
        //TODO: Need to figure out what information is needed for the ore blocks so it can be stored here.
        ORE_BLOCKS.add(block);
    }

    public static void addVoidOre(String type, Block toCopy, int metaToCopy) {
        addVoidOre(new BlockMaterialCopy("void_ore_" + type, toCopy, metaToCopy));
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileCelestialGateway.class, new ResourceLocation(Kami.MOD_ID, "celestial_gateway"));
    }
}
