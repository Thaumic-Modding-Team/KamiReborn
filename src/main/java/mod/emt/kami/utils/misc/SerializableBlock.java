package mod.emt.kami.utils.misc;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class SerializableBlock implements INBTSerializable<NBTTagCompound> {
    public static final SerializableBlock EMPTY = new SerializableBlock(Blocks.AIR.getDefaultState());

    private IBlockState state;
    private Block block;
    private int meta;
    private NBTTagCompound tileTag;

    public SerializableBlock(IBlockState state, @Nullable TileEntity tile) {
        this.state = state;
        this.block = state.getBlock();
        this.meta = state.getBlock().getMetaFromState(state);
        this.tileTag = tile != null ? tile.writeToNBT(new NBTTagCompound()) : new NBTTagCompound();
    }

    public SerializableBlock(World world, BlockPos pos) {
        this(world.getBlockState(pos), world.getTileEntity(pos));
    }

    public SerializableBlock(IBlockState state) {
        this(state, null);
    }

    public SerializableBlock(NBTTagCompound tag) {
        this.deserializeNBT(tag);
    }

    public boolean isEmpty() {
        return this == EMPTY || this.block == Blocks.AIR || this.block.getRegistryName() == null || this.state.equals(Blocks.AIR.getDefaultState());
    }

    public IBlockState getState() {
        return state;
    }

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }

    @Nullable
    protected TileEntity getTileEntity(World world) {
        if(!this.isEmpty() && !this.tileTag.isEmpty() && this.block.hasTileEntity(this.state)) {
            TileEntity tile = this.block.createTileEntity(world, this.state);
             if(tile != null) {
                 tile.readFromNBT(this.tileTag);
             }
            return tile;
        }
        return null;
    }

    /**
     * Spawns the block at the specified position. This method does not perform any logic checks
     * pertaining to whether this block should go in the position.
     */
    public boolean placeBlockInWorld(World world, BlockPos pos) {
        if(!this.isEmpty()) {
            world.setBlockState(pos, this.state);
            TileEntity tile = this.getTileEntity(world);
            if (tile != null) {
                world.setTileEntity(pos, tile);
                tile.validate();
            }
            world.notifyNeighborsOfStateChange(pos, this.block, true);
            return true;
        }
        return false;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if(!this.isEmpty()) {
            tag.setString("block", this.block.getRegistryName().toString());
            tag.setInteger("meta", this.meta);
            tag.setTag("tileTag", this.tileTag);
        }
        return tag;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("block")));
        if(this.block == null || this.block == Blocks.AIR) {
            this.block = Blocks.AIR;
            this.state = Blocks.AIR.getDefaultState();
            this.meta = 0;
            this.tileTag = new NBTTagCompound();
        } else {
            this.meta = tag.getInteger("meta");
            this.state = this.block.getStateFromMeta(this.meta);
            this.tileTag = tag.getCompoundTag("tileTag");
        }
    }
}
