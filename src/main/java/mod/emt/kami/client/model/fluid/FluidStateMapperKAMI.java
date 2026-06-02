package mod.emt.kami.client.model.fluid;

import mod.emt.kami.Kami;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class FluidStateMapperKAMI extends StateMapperBase implements ItemMeshDefinition {
    private final ModelResourceLocation location;

    public FluidStateMapperKAMI(Fluid fluid) {
        this.location = new ModelResourceLocation(new ResourceLocation(Kami.MOD_ID, "fluids"), fluid.getName());
    }

    @Override
    protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
        return this.location;
    }

    @Override
    public @NotNull ModelResourceLocation getModelLocation(@NotNull ItemStack stack) {
        return this.location;
    }
}
