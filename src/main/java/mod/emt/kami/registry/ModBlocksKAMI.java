package mod.emt.kami.registry;

import mod.emt.kami.Kami;
import mod.emt.kami.blocks.BlockCelestialGateway;
import mod.emt.kami.blocks.BlockMaterial;
import mod.emt.kami.blocks.fire.BlockIgnisFire;
import mod.emt.kami.tiles.TileCelestialGateway;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModBlocksKAMI {
    public static Block CELESTIAL_GATEWAY;
    public static Block ICHOR_BLOCK;
    public static Block ICHORIUM_BLOCK;
    public static Block IGNIS_FIRE;

    public static final List<Block> MOD_BLOCKS = new ArrayList<>();

    public static void initBlocks() {
        MOD_BLOCKS.add(ICHOR_BLOCK = new BlockMaterial("ichor_block", Material.IRON, MapColor.GOLD, 5.0F, SoundType.METAL, "blockIchor"));
        MOD_BLOCKS.add(ICHORIUM_BLOCK = new BlockMaterial("ichorium_block", Material.IRON, MapColor.GOLD, 5.0F, SoundType.METAL, "blockIchorium"));
        MOD_BLOCKS.add(CELESTIAL_GATEWAY = new BlockCelestialGateway());
        MOD_BLOCKS.add(IGNIS_FIRE = new BlockIgnisFire());
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileCelestialGateway.class, new ResourceLocation(Kami.MOD_ID, "celestial_gateway"));
    }
}
