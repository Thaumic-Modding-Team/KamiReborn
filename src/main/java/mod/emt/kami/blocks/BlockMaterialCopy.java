package mod.emt.kami.blocks;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IOreDictProvider;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public class BlockMaterialCopy extends Block implements IOreDictProvider {
    private final Block toCopyBlock;
    private final int toCopyMeta;
    private String[] oreDicts;

    public BlockMaterialCopy(String unlocName, Block toCopyBlock, int toCopyMeta) {
        super(Material.ROCK);
        this.setRegistryName(Kami.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
        this.setHardness(3.0f);
        this.setResistance(5.0f);
        this.setSoundType(SoundType.STONE);
        this.toCopyBlock = toCopyBlock;
        this.toCopyMeta = toCopyMeta;
        this.oreDicts = null;
    }

    public BlockMaterialCopy setOreDicts(String... oreDicts) {
        this.oreDicts = oreDicts;
        return this;
    }

    @SuppressWarnings("deprecation")
    public IBlockState getCopiedState() {
        return this.toCopyBlock.getStateFromMeta(this.toCopyMeta);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull Material getMaterial(@NotNull IBlockState state) {
        return this.getCopiedState().getMaterial();
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getBlockHardness(@NotNull IBlockState blockState, @NotNull World worldIn, @NotNull BlockPos pos) {
        try {
            return this.getCopiedState().getBlockHardness(worldIn, pos);
        } catch (Exception ignored) {
            return super.getBlockHardness(blockState, worldIn, pos);
        }
    }

    @Override
    public float getExplosionResistance(@NotNull World world, @NotNull BlockPos pos, @Nullable Entity exploder, @NotNull Explosion explosion) {
        try {
            return this.getCopiedState().getBlock().getExplosionResistance(world, pos, exploder, explosion);
        } catch (Exception ignored) {
            return super.getExplosionResistance(world, pos, exploder, explosion);
        }
    }

    @Override
    public @NotNull SoundType getSoundType(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @Nullable Entity entity) {
        try {
            return this.getCopiedState().getBlock().getSoundType(state, world, pos, entity);
        } catch (Exception ignored) {
            return super.getSoundType(state, world, pos, entity);
        }
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        Item item = this.getCopiedState().getBlock().getItemDropped(this.getCopiedState(), rand, fortune);
        Item blockItem = Item.getItemFromBlock(this.getCopiedState().getBlock());
        return item != blockItem ? item : Item.getItemFromBlock(this);
    }

    @Override
    public int quantityDropped(@NotNull IBlockState state, int fortune, @NotNull Random random) {
        return this.getCopiedState().getBlock().quantityDropped(this.getCopiedState(), fortune, random);
    }

    @Override
    public void dropBlockAsItemWithChance(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, float chance, int fortune) {
        try {
            this.getCopiedState().getBlock().dropBlockAsItemWithChance(worldIn, pos, this.getCopiedState(), chance, fortune);
        } catch (Exception ignored) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSilkHarvest(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player) {
        try {
            return this.getCopiedState().getBlock().canSilkHarvest(world, pos, state, player);
        } catch (Exception ignored) {
            //TODO: This needs an AT, mixin or reflection to access
            //  return this.getCopiedState().getBlock().canSilkHarvest();
            return super.canSilkHarvest(world, pos, state, player);
        }
    }

    @Override
    public @Nullable String getHarvestTool(@NotNull IBlockState state) {
        return this.getCopiedState().getBlock().getHarvestTool(state);
    }

    @Override
    public boolean isToolEffective(@NotNull String type, @NotNull IBlockState state) {
        return this.getCopiedState().getBlock().isToolEffective(type, this.getCopiedState());
    }

    @Override
    public void registerOreDicts() {
        if(this.oreDicts == null) {
            ItemStack toCopyStack = new ItemStack(this.getCopiedState().getBlock(), 1, this.getCopiedState().getBlock().getMetaFromState(this.getCopiedState()));
            for (int oreId : OreDictionary.getOreIDs(toCopyStack)) {
                OreDictionary.registerOre(OreDictionary.getOreName(oreId), this);
            }
        } else {
            for (String oreDict : this.oreDicts) {
                OreDictionary.registerOre(oreDict, this);
            }
        }
    }
}
