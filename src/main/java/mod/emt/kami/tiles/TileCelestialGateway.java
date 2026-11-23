package mod.emt.kami.tiles;

import mod.emt.kami.blocks.BlockCelestialGateway;
import mod.emt.kami.utils.misc.SerializablePosition;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.casters.IInteractWithCaster;
import thaumcraft.common.lib.SoundsTC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TileCelestialGateway extends TileEntityKami implements IInteractWithCaster {
    public Map<UUID, SerializablePosition> savedPositions = new HashMap<>();

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound posTags = compound.getCompoundTag("savedPositions");
        posTags.getKeySet().forEach(key -> this.savedPositions.put(UUID.fromString(key), new SerializablePosition(posTags.getCompoundTag(key))));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.savedPositions.forEach((playerId, position) -> compound.setTag(playerId.toString(), position.serializeNBT()));
        return compound;
    }

    @Override
    public boolean onCasterRightClick(World world, ItemStack stack, EntityPlayer player, BlockPos pos, EnumFacing facing, EnumHand hand) {
        UUID playerId = player.getGameProfile().getId();
        if(!this.savedPositions.containsKey(playerId)) {
            if(world.isRemote) {
                player.playSound(SoundsTC.craftfail, 1.0f, 0.6f);
            }
        } else {
            SerializablePosition position = this.savedPositions.get(playerId);
            World targetWorld = position.getWorld(true);
            if(targetWorld != null) {
                if(!world.isRemote) {
                    if (world.provider.getDimension() != targetWorld.provider.getDimension()) {
                        player.changeDimension(targetWorld.provider.getDimension(), (world1, entity, yaw) -> {});
                    }
                    player.setPositionAndRotation(position.getPosX(), position.getPosY(), position.getPosZ(), position.getYaw(), position.getPitch());
                    targetWorld.updateEntityWithOptionalForce(player, false);
                    targetWorld.playEvent(player, Constants.WorldEvents.PORTAL_TRAVEL_SOUND, position.getPosition(), 0);
                    this.savedPositions.remove(playerId);
                    this.markDirty();
                }
            }
        }
        return true;
    }

    public void teleportToSavedPosition(EntityPlayer player) {
        if(!this.world.isRemote) {
            UUID playerId = player.getGameProfile().getId();
            SerializablePosition position = new SerializablePosition(player);
            IBlockState tileState = this.world.getBlockState(this.pos);
            EnumFacing tileFacing = tileState.getValue(BlockCelestialGateway.FACING);

            double posX = this.pos.getX() + 0.5;
            double posY = this.pos.getY();
            double posZ = this.pos.getX() + 0.5;

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

            if (this.world.provider.getDimension() != player.world.provider.getDimension()) {
                player.changeDimension(this.world.provider.getDimension(), (world1, entity, yaw) -> {});
            }
            player.setPositionAndUpdate(posX, posY, posZ);
            this.world.playEvent(player, Constants.WorldEvents.PORTAL_TRAVEL_SOUND, this.pos, 0);
            this.savedPositions.put(playerId, position);
            this.markDirty();
        }
    }

    public boolean hasSavedPosition(EntityPlayer player) {
        return this.savedPositions.containsKey(player.getGameProfile().getId());
    }

    public boolean shouldRenderFace(EnumFacing facing) {
        return this.world.getBlockState(this.pos).getValue(BlockCelestialGateway.FACING) == facing;
    }
}
