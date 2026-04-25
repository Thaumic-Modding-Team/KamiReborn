package mod.emt.kami.blocks;

import mod.emt.kami.api.block.AbstractBlockAddition;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;

public class BlockMaterial extends AbstractBlockAddition {
    boolean beaconBlock;
    boolean flammable;
    String oreDict;

    public BlockMaterial(String name, Material material, MapColor mapColor, float hardness, float resistance, SoundType soundType, boolean beaconBlock, boolean flammable, String oreDict) {
        super(name, material, mapColor);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(soundType);
        this.beaconBlock = beaconBlock;
        this.flammable = flammable;
        this.oreDict = oreDict;
    }

    public BlockMaterial(String name, Material material, MapColor mapColor, float hardness, SoundType soundType, String oreDict) {
        super(name, material, mapColor);
        this.setHardness(hardness);
        this.setSoundType(soundType);
        this.oreDict = oreDict;
    }

    @Override
    public boolean isBeaconBase(@NotNull IBlockAccess worldObj, @NotNull BlockPos pos, @NotNull BlockPos beacon) {
        return beaconBlock;
    }

    @Override
    public void registerOreDicts() {
        if (oreDict != null) {
            OreDictionary.registerOre(oreDict, this);
        }
    }
}
