package mod.emt.kami.registry;

import mod.emt.kami.blocks.BlockMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

public class ModBlocksKAMI {
    public static Block ICHOR_BLOCK;
    public static Block ICHORIUM_BLOCK;

    public static final List<Block> MOD_BLOCKS = new ArrayList<>();

    public static void initBlocks() {
        MOD_BLOCKS.add(ICHOR_BLOCK = new BlockMaterial("ichor_block", Material.IRON, MapColor.GOLD, 5.0F, SoundType.METAL, "blockIchor"));
        MOD_BLOCKS.add(ICHORIUM_BLOCK = new BlockMaterial("ichorium_block", Material.IRON, MapColor.GOLD, 5.0F, SoundType.METAL, "blockIchorium"));
    }
}
