package mod.emt.kami.api.block;

import mod.emt.kami.Kami;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import java.util.Objects;

/**
 * A base class for adding blocks. Automatically handles block and item registration.
 */
public abstract class AbstractBlockAddition extends Block implements IBlockAddition {
    public AbstractBlockAddition(String unlocName, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        this.setRegistryName(Kami.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
    }

    public AbstractBlockAddition(String unlocName, Material materialIn) {
        this(unlocName, materialIn, materialIn.getMaterialMapColor());
    }
}
