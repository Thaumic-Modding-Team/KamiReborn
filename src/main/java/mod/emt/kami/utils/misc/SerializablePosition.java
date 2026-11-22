package mod.emt.kami.utils.misc;

import mod.emt.kami.utils.helpers.WorldHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SerializablePosition implements INBTSerializable<NBTTagCompound> {
    private World world;
    private int dimId;
    private double posX;
    private double posY;
    private double posZ;
    private float pitch;
    private float yaw;

    public SerializablePosition(World world, double posX, double posY, double posZ, float yaw, float pitch) {
        this.world = world;
        this.dimId = world.provider.getDimension();
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SerializablePosition(World world, BlockPos pos) {
        this(world, pos.getX(), pos.getY(), pos.getZ(), 0, 0);
    }

    public SerializablePosition(Entity entity) {
        this(entity.world, entity.posX + 0.5, entity.posY, entity.posZ + 0.5, entity.rotationYaw, entity.rotationPitch);
    }

    public SerializablePosition(NBTTagCompound tag) {
        this.deserializeNBT(tag);
    }

    @Nullable
    public World getWorld(boolean forceLoad) {
        if(this.world == null) {
            this.world = WorldHelper.getWorldObject(this.dimId, forceLoad);
        }
        return this.world;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY, this.posZ);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("dimId", this.dimId);
        tag.setDouble("posX", this.posX);
        tag.setDouble("posY", this.posY);
        tag.setDouble("posZ", this.posZ);
        tag.setFloat("yaw", this.yaw);
        tag.setFloat("pitch", this.pitch);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.dimId = tag.getInteger("dimId");
        this.posX = tag.getDouble("posX");
        this.posY = tag.getDouble("posY");
        this.posZ = tag.getDouble("posZ");
        this.yaw = tag.getFloat("yaw");
        this.pitch = tag.getFloat("pitch");
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SerializablePosition))
            return false;
        SerializablePosition position = (SerializablePosition) object;
        return dimId == position.dimId
                && Double.compare(getPosX(), position.getPosX()) == 0
                && Double.compare(getPosY(), position.getPosY()) == 0
                && Double.compare(getPosZ(), position.getPosZ()) == 0
                && Float.compare(getPitch(), position.getPitch()) == 0
                && Float.compare(getYaw(), position.getYaw()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimId, getPosX(), getPosY(), getPosZ(), getPitch(), getYaw());
    }
}
