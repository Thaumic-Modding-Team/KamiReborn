package mod.emt.kami.entities;

import io.netty.buffer.ByteBuf;
import mod.emt.kami.items.tools.ItemAwakenedAxe;
import mod.emt.kami.registry.ModSoundsKAMI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityThrownAxe extends EntityThrowable implements IEntityAdditionalSpawnData {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityThrownAxe.class, DataSerializers.ITEM_STACK);
    public static final int MAX_THROW_TIME = 20;
    public static final float MAX_BOOMERANG_SPEED = 1.0F;
    public static final int BOOMERANG_DAMAGE = 10;
    public Entity owner;
    public int returnTime;
    public int thrownFromSlot;
    public float damage;
    public float speed;

    public EntityThrownAxe(World world, EntityLivingBase entity, ItemStack boomerangThrown) {
        super(world, entity);
        this.setBoomerangItem(boomerangThrown);
        this.isImmuneToFire = true;
        this.returnTime = MAX_THROW_TIME;
        this.noClip = false;
        this.owner = entity;
        this.damage = BOOMERANG_DAMAGE;
        this.speed = MAX_BOOMERANG_SPEED;
    }

    public EntityThrownAxe(World world) {
        super(world);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setSpeedModifier(float modifier) {
        this.speed *= modifier;
    }

    public float getSpeed() {
        return this.speed;
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    public void setBoomerangItem(ItemStack stack) {
        this.getDataManager().set(ITEM, stack);
        this.getDataManager().setDirty(ITEM);
    }

    public ItemStack getBoomerangItem() {
        return this.getDataManager().get(ITEM);
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeInt(this.thrower != null ? this.thrower.getEntityId() : -1);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        this.owner = this.world.getEntityByID(data.readInt());

        if (this.owner instanceof EntityLivingBase) {
            this.thrower = (EntityLivingBase) this.owner;
        }
    }

    @Override
    public void onUpdate() {
        if (!world.isRemote) {
            EntityPlayer thrower = null;
            if (this.owner instanceof EntityPlayer) thrower = (EntityPlayer) this.owner;
            if (thrower != null) {
                if (thrower.capabilities.isCreativeMode) {
                    ItemStack hand = thrower.inventory.mainInventory.get(thrower.inventory.currentItem);
                    if (this.getBoomerangItem().isItemEqual(hand)) {
                        thrower.inventory.mainInventory.set(thrower.inventory.currentItem, new ItemStack(Items.AIR));
                    }
                }

                if (this.returnTime == MAX_THROW_TIME) {
                    thrower.swingArm(EnumHand.MAIN_HAND);
                    this.shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0.0F, this.getSpeed(), 0.0F);
                }

                if (this.returnTime % 3 == 0) {
                    this.world.playSound(null, getPosition(), ModSoundsKAMI.ITEM_ICHOR_THROW.getSoundEvent(), SoundCategory.PLAYERS, 2.0F, 3.0F + 1.0F / (1.0F + getDistance(thrower) / 16));
                }

                this.returnTime--;

                if(this.returnTime == 0) {
                    this.reverseDirection(true);
                }

                if (this.returnTime <= 0) {
                    float currentHeading = (float) Math.atan2(this.motionZ, this.motionX);
                    float headingToThrower = (float) Math.atan2(thrower.posZ - this.posZ, thrower.posX - this.posX);
                    float curveScale = Math.max(0.3f, this.returnTime * 0.007f);
                    float newHeading = this.updateRotationRadians(currentHeading, headingToThrower, curveScale);
                    double currentPitch = Math.atan2(this.motionY, Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));
                    double targetPitch = Math.atan2(thrower.posY + (double) thrower.getEyeHeight() - this.posY, Math.sqrt((thrower.posX - this.posX) * (thrower.posX - this.posX) + (thrower.posZ - this.posZ) * (thrower.posZ - this.posZ)));
                    float newPitch = this.updateRotationRadians((float) currentPitch, (float) targetPitch, curveScale * 0.3F);
                    this.motionX = Math.cos(newHeading);
                    this.motionZ = Math.sin(newHeading);
                    this.motionY = Math.sin(newPitch);
                    this.shoot(this.motionX, this.motionY, this.motionZ, this.getSpeed(), 0.0F);
                }
            }

            if (getRidingEntity() == null) {
                List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(1.5D, 1.5D, 1.5D));
                for (Entity e : entities) {
                    if (e instanceof EntityItem) {
                        e.startRiding(this);
                        break;
                    }
                }
            }

            if (this.returnTime < -100) {
                if(this.owner != null && this.owner.world.provider.getDimension() == this.world.provider.getDimension()) {
                    this.setPositionAndUpdate(this.owner.posX, this.owner.posY, this.owner.posZ);
                } else {
                    this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, this.getBoomerangItem()));
                }
                this.setDead();
            }

            this.rotationPitch = this.returnTime * 50;
            this.prevRotationPitch = (this.returnTime + 1) * 50;
        } else {
            for (int i = 0; i < 4; i++) {
                this.world.spawnParticle(EnumParticleTypes.CRIT,
                        this.posX + this.motionX * (double) i / 4.0D,
                        this.posY + this.motionY * (double) i / 4.0D,
                        this.posZ + this.motionZ * (double) i / 4.0D,
                        -this.motionX, -this.motionY + 0.2D, -this.motionZ);
            }
        }
        super.onUpdate();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

    @Override
    protected void onImpact(@NotNull RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityLivingBase) {
            if (result.entityHit == getThrower() && this.returnTime <= 0 && result.entityHit instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) result.entityHit;
                this.returnBoomerangToPlayer(player);

                if (this.getRidingEntity() != null) {
                    Entity passenger = this.getRidingEntity();
                    passenger.startRiding(this);
                    passenger.posX = player.posX;
                    passenger.posY = player.posY;
                    passenger.posZ = player.posZ;
                }

                if (!this.isDead && this.world instanceof WorldServer) {
                    this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    EntityTracker entitytracker = ((WorldServer) world).getEntityTracker();
                    entitytracker.sendToTracking(this, new SPacketCollectItem(getEntityId(), getThrower().getEntityId(), 1));
                }
                this.setDead();
            }

            if (result.entityHit != getThrower() && getThrower() != null) {
                EntityLivingBase entityLiving = (EntityLivingBase) result.entityHit;
                ItemAwakenedAxe.EnumImpactMode mode = ItemAwakenedAxe.EnumImpactMode.getMode(this.getBoomerangItem());
                mode.onImpact(this, entityLiving);
                this.getBoomerangItem().damageItem(1, getThrower());
                if (this.getBoomerangItem().getCount() == 0) {
                    this.world.playSound(null, getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 0.8F);
                    this.setDead();
                }
                this.reverseDirection(true);
            }
        } else {
            BlockPos blockPos = result.getBlockPos();
            IBlockState blockState = world.getBlockState(blockPos);
            boolean solid = blockState.getCollisionBoundingBox(world, blockPos) != null;
            if (!this.noClip && solid) {
                ItemAwakenedAxe.EnumImpactMode mode = ItemAwakenedAxe.EnumImpactMode.getMode(this.getBoomerangItem());
                mode.onImpact(this, null);
                this.reverseDirection(true);
            }
        }
    }

    @Override
    public void onStruckByLightning(@NotNull EntityLightningBolt lightningBolt) {

    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    private void returnBoomerangToPlayer(EntityPlayer player) {
        if (!this.world.isRemote) {
            ItemStack crowdedItem = player.inventory.getStackInSlot(this.thrownFromSlot);
            player.inventory.mainInventory.set(this.thrownFromSlot, this.getBoomerangItem());
            if (!crowdedItem.isEmpty()) {
                ItemHandlerHelper.giveItemToPlayer(player, crowdedItem);
            }
        }
    }

    private void reverseDirection(boolean noClip) {
        this.returnTime = Math.min(this.returnTime, 0);
        this.noClip = noClip;
        this.motionX *= -1;
        this.motionY *= -1;
        this.motionZ *= -1;
    }

    private float updateRotationRadians(float currHeading, float throwerHeading, float curveScale) {
        float var4 = (throwerHeading - currHeading);
        var4 %= (float) Math.PI * 2;
        if (var4 >= Math.PI) var4 -= (float) Math.PI * 2;
        if (var4 < -Math.PI) var4 += (float) Math.PI * 2;
        if (var4 > curveScale) var4 = curveScale;
        if (var4 < -curveScale) var4 = -curveScale;
        return currHeading + var4;
    }
}
