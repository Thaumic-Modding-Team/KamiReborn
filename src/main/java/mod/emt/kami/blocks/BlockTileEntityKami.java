package mod.emt.kami.blocks;

import mod.emt.kami.Kami;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockTileEntityKami extends BlockContainer {
    private final Class<? extends TileEntity> tileClazz;

    protected BlockTileEntityKami(String unlocName, Material materialIn, MapColor color, Class<? extends TileEntity> tileClazz) {
        super(materialIn, color);
        this.setRegistryName(Kami.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
        this.tileClazz = tileClazz;
    }

    protected BlockTileEntityKami(String unlocName, Material materialIn, Class<? extends TileEntity> tileClazz) {
        super(materialIn);
        this.setRegistryName(Kami.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
        this.tileClazz = tileClazz;
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState state) {
        return this.tileClazz != null;
    }

    @Override
    public @Nullable TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        try {
            return this.tileClazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize TileEntity", e);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
