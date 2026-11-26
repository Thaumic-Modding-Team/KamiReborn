package mod.emt.kami.entities;

import mod.emt.kami.registry.ElementalFireRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EntityElementalFireBottle extends EntityThrowable {
    private static final DataParameter<String> ASPECT = EntityDataManager.createKey(EntityElementalFireBottle.class, DataSerializers.STRING);
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityElementalFireBottle.class, DataSerializers.ITEM_STACK);
    private static final Set<BlockPos> IMPACT_OFFSETS;

    public EntityElementalFireBottle(World worldIn) {
        super(worldIn);
    }

    public EntityElementalFireBottle(World worldIn, EntityLivingBase throwerIn, ItemStack thrownItem, Aspect aspect) {
        super(worldIn, throwerIn);
        this.setItem(thrownItem);
        this.setAspect(aspect);
    }

    public EntityElementalFireBottle(World worldIn, double x, double y, double z, ItemStack thrownItem, Aspect aspect) {
        super(worldIn, x, y, z);
        this.setItem(thrownItem);
        this.setAspect(aspect);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(ASPECT, Aspect.AIR.getTag());
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("Aspect", this.getAspect().getTag());
        compound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setAspect(Aspect.getAspect(compound.getString("Aspect")));
        this.setItem(new ItemStack(compound.getCompoundTag("Item")));
    }

    @Override
    protected void onImpact(@NotNull RayTraceResult result) {
        Aspect aspect = this.getAspect();
        IBlockState fireState = ElementalFireRegistry.getElementalFireBlock(aspect);
        if(fireState.getBlock() != Blocks.AIR) {
            if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos impact = result.getBlockPos().offset(result.sideHit);
                this.createFire(impact, fireState);
                if(!this.world.isRemote) {
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.AMBIENT, 1.0f, 0.5f);
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, SoundCategory.AMBIENT, 1.0f, 0.1f + (float) (world.rand.nextGaussian() * 0.05f));
                    this.setDead();
                }
            }
        }
    }

    public void createFire(BlockPos impact, IBlockState fireState) {
        for(BlockPos offset : IMPACT_OFFSETS) {
            BlockPos checkPos = impact.add(offset);
            IBlockState checkState = this.world.getBlockState(checkPos);
            if(checkState.getBlock().isAir(checkState, this.world, checkPos)) {
                if(!this.world.isRemote) {
                    this.world.setBlockState(checkPos, fireState);
                } else {
                    //TODO: Impact effect
                }
            }
        }
    }

    public boolean hasAdjacentBlock(BlockPos checkPos, IBlockState fireState) {
        for(EnumFacing facing : EnumFacing.VALUES) {
            IBlockState state = this.world.getBlockState(checkPos.offset(facing));
            if(state.getBlock() != Blocks.AIR && !fireState.equals(state)) {
                return true;
            }
        }
        return false;
    }

    public Aspect getAspect() {
        return Aspect.getAspect(this.getDataManager().get(ASPECT));
    }

    public void setAspect(Aspect aspect) {
        this.getDataManager().set(ASPECT, aspect.getTag());
        this.getDataManager().setDirty(ASPECT);
    }

    public ItemStack getItem() {
        return this.getDataManager().get(ITEM);
    }

    public void setItem(ItemStack stack) {
        this.getDataManager().set(ITEM, stack);
        this.getDataManager().setDirty(ITEM);
    }

    static {
        IMPACT_OFFSETS = Collections.unmodifiableSet(new HashSet<BlockPos>() {{
            for (int y = -1; y <= 1; y++) {
                add(new BlockPos(-2, y, -2));
                add(new BlockPos(-2, y, 2));
                add(new BlockPos(2, y, -2));
                add(new BlockPos(2, y, 2));
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        add(new BlockPos(x, y, z));
                    }
                }
            }
        }});
    }
}
