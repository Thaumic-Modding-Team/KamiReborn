package mod.emt.kami.utils.misc;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class SerializableEntity implements INBTSerializable<NBTTagCompound> {
    public static SerializableEntity EMPTY = new SerializableEntity(new NBTTagCompound());

    private EntityEntry entityEntry;
    private NBTTagCompound entityTag;


    public SerializableEntity(Entity entity) {
        this.entityEntry = EntityRegistry.getEntry(entity.getClass());
        this.entityTag = entity.writeToNBT(new NBTTagCompound());
    }

    public SerializableEntity(NBTTagCompound tag) {
        this.deserializeNBT(tag);
    }

    public boolean isEmpty() {
        return this == EMPTY || this.entityEntry == null || this.entityEntry.getRegistryName() == null || this.entityTag.isEmpty();
    }

    @Nullable
    public EntityEntry getEntityEntry() {
        return entityEntry;
    }

    public boolean spawnEntity(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
        if(!this.isEmpty()) {
            Entity entity = this.entityEntry.newInstance(world);
            entity.readFromNBT(this.entityTag);
            entity.setPosition(posX, posY, posZ);
            entity.motionX = motionX;
            entity.motionY = motionY;
            entity.motionZ = motionZ;
            return world.spawnEntity(entity);
        }
        return false;
    }

    public boolean spawnEntity(World world, double posX, double posY, double posZ) {
        return spawnEntity(world, posX, posY, posZ, 0, 0, 0);
    }

    public boolean spawnEntity(World world, BlockPos pos) {
        return this.spawnEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }

    @Nullable
    public Entity getEntity(World world) {
        if (!this.isEmpty()) {
            Entity entity = this.entityEntry.newInstance(world);
            entity.readFromNBT(this.entityTag);
            return entity;
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if(!this.isEmpty()) {
            tag.setString("entityId", this.entityEntry.getRegistryName().toString());
            tag.setTag("entityTag", this.entityTag);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString("entityId")));
        this.entityTag = entityEntry != null ? tag.getCompoundTag("entityTag") : new NBTTagCompound();
    }
}
