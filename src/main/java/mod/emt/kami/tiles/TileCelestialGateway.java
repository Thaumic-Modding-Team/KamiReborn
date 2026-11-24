package mod.emt.kami.tiles;

import mod.emt.kami.api.tile.INameableTile;
import mod.emt.kami.blocks.BlockCelestialGateway;
import mod.emt.kami.utils.misc.SerializablePosition;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.casters.IInteractWithCaster;
import thaumcraft.common.lib.SoundsTC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TileCelestialGateway extends TileEntityKami implements IInteractWithCaster, INameableTile {
    public Map<UUID, SerializablePosition> savedPositions = new HashMap<>();
    protected String customName = null;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound posTags = compound.getCompoundTag("savedPositions");
        posTags.getKeySet().forEach(key -> this.savedPositions.put(UUID.fromString(key), new SerializablePosition(posTags.getCompoundTag(key))));
        if(compound.hasKey("customName")) {
            this.customName = compound.getString("customName");
        }
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound posTags = new NBTTagCompound();
        this.savedPositions.forEach((playerId, position) -> posTags.setTag(playerId.toString(), position.serializeNBT()));
        compound.setTag("savedPositions", posTags);
        if(this.hasCustomName()) {
            compound.setString("customName", this.customName);
        }
        return compound;
    }

    @Override
    public boolean onCasterRightClick(World world, ItemStack stack, EntityPlayer player, BlockPos pos, EnumFacing facing, EnumHand hand) {
        if(player.getHeldItem(hand) != stack)
            return false;

        UUID playerId = player.getGameProfile().getId();
        if(!this.savedPositions.containsKey(playerId)) {
            if(world.isRemote) {
                player.playSound(SoundsTC.craftfail, 1.0f, 0.6f);
            }
        } else {
            SerializablePosition position = this.savedPositions.get(playerId);
            this.teleportPlayer(player, position);
            this.savedPositions.remove(playerId);
            this.markDirty();
        }
        return true;
    }

    public void teleportToGateway(EntityPlayer player) {
        if(!this.world.isRemote) {
            UUID playerId = player.getGameProfile().getId();
            SerializablePosition playerPos = new SerializablePosition(player);
            IBlockState tileState = this.world.getBlockState(this.pos);
            EnumFacing tileFacing = tileState.getValue(BlockCelestialGateway.FACING);

            double posX = this.pos.getX() + 0.5;
            double posY = this.pos.getY();
            double posZ = this.pos.getZ() + 0.5;
            float yaw = tileFacing.getHorizontalIndex() >= 0 ? tileFacing.getHorizontalAngle() : player.rotationYaw;
            float pitch = player.rotationPitch;

            if (tileFacing == EnumFacing.DOWN) {
                posY -= 1.0;
            } else if (tileFacing == EnumFacing.UP) {
                posY += 0.125;
            } else {
                BlockPos downPos = this.pos.down();
                IBlockState state = this.world.getBlockState(downPos);
                if(state.getMaterial() == Material.AIR || state.getBlock().isReplaceable(this.world, downPos)) {
                    posY -= 1.0;
                }
            }

            SerializablePosition tilePos = new SerializablePosition(this.world, posX, posY, posZ, yaw, pitch);
            this.teleportPlayer(player, tilePos);
            this.savedPositions.put(playerId, playerPos);
            this.markDirty();
        }
    }

    public boolean teleportPlayer(EntityPlayer player, SerializablePosition targetPos) {
        World targetWorld = targetPos.getWorld(true);
        if(targetWorld != null && player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            if (player.world.provider.getDimension() != targetWorld.provider.getDimension()) {
                player.changeDimension(targetWorld.provider.getDimension(), (world1, entity, yaw) -> {});
            }
            playerMP.connection.setPlayerLocation(targetPos.getPosX(), targetPos.getPosY(), targetPos.getPosZ(), targetPos.getYaw(), targetPos.getPitch());
            targetWorld.playSound(null, targetPos.getPosition(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.6f, 1.0f);
            return true;
        }
        return false;
    }

    public boolean hasSavedPosition(EntityPlayer player) {
        return this.savedPositions.containsKey(player.getGameProfile().getId());
    }

    public boolean shouldRenderFace(EnumFacing facing) {
        return this.world.getBlockState(this.pos).getValue(BlockCelestialGateway.FACING) == facing;
    }

    @Override
    public @NotNull ITextComponent getDisplayName() {
        return  new TextComponentString(this.hasCustomName() ? this.customName : "");
    }

    @Override
    public void setCustomName(String displayName) {
        this.customName = displayName;
        this.markDirty();
    }

    @Override
    public @NotNull String getName() {
        return this.hasCustomName() ? this.customName : "";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }
}
