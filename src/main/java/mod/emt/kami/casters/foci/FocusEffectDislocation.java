package mod.emt.kami.casters.foci;

import mod.emt.kami.Kami;
import mod.emt.kami.utils.misc.SerializableBlock;
import mod.emt.kami.utils.misc.SerializableEntity;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.ICaster;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;

public class FocusEffectDislocation extends FocusEffect {
    private static final String TAG_STORAGE = "FociDislocation";
    private static final String TAG_TYPE = "FociDislocationType";

    @Override
    public Aspect getAspect() {
        return Aspect.VOID;
    }

    @Override
    public String getKey() {
        return "focus." + Kami.MOD_ID + ".dislocation";
    }

    @Override
    public NodeSetting[] createSettings() {
        int[] storeObjects = {0, 1, 2};
        String[] storeObjectsDesc = {"focus.common.store_objects_blocks", "focus.common.store_objects_entities", "focus.common.store_objects_both"};
        return new NodeSetting[]{new NodeSetting("store_objects", "focus.common.store_objects", new NodeSetting.NodeSettingIntList(storeObjects, storeObjectsDesc))};
    }

    @Override
    public int getComplexity() {
        return 20;
    }

    @Override
    public String getResearch() {
        return "KAMI_FOCUS_DISLOCATION";
    }

    @Override
    public boolean execute(RayTraceResult target, Trajectory trajectory, float finalPower, int num) {
        World world = getPackage().world;

        if (world.isRemote || target == null || !(getPackage().getCaster() instanceof EntityPlayerMP)) {
            return false;
        }

        EntityPlayerMP player = (EntityPlayerMP) getPackage().getCaster();
        ItemStack focus = getFoci(player);

        if (focus.isEmpty()) {
            return false;
        }

        int mode = getSettingValue("store_objects");
        NBTTagCompound focusTag = focus.getTagCompound();
        boolean success = false;

        if (focusTag != null && focusTag.hasKey(TAG_STORAGE, Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound stored = focusTag.getCompoundTag(TAG_STORAGE);
            String type = stored.getString(TAG_TYPE);

            if (type.equals("block") && target.typeOfHit == RayTraceResult.Type.BLOCK) {
                success = placeBlock(world, player, target.getBlockPos(), target.sideHit, focus);
            } else if (type.equals("entity")) {
                BlockPos pos = target.typeOfHit == RayTraceResult.Type.BLOCK ? target.getBlockPos().offset(target.sideHit) : new BlockPos(target.hitVec);
                success = placeEntity(world, pos, focus);
            }
        } else {
            if (target.typeOfHit == RayTraceResult.Type.BLOCK && mode != 1) {
                success = storeBlock(world, player, target.getBlockPos(), focus);
            } else if (target.typeOfHit == RayTraceResult.Type.ENTITY && mode != 0 && target.entityHit instanceof EntityLivingBase) {
                success = storeEntity((EntityLivingBase) target.entityHit, focus);
            }
        }

        if (success) {
            playEffects(world, target);
        }

        return false;
    }

    private ItemStack getFoci(EntityPlayer player) {
        for (ItemStack stack : new ItemStack[]{player.getHeldItemMainhand(), player.getHeldItemOffhand()}) {
            if (!stack.isEmpty() && stack.getItem() instanceof ICaster) {
                return ((ICaster) stack.getItem()).getFocusStack(stack);
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean storeBlock(World world, EntityPlayerMP player, BlockPos pos, ItemStack focus) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);

        //Checking for DESTROY push reaction is a decent way to check for beds and doors, though snow, plants and liquids get caught in the crossfire
        //Checking for PORTAL material prevents the effect from collecting portals
        if (world.isAirBlock(pos) || state.getBlockHardness(world, pos) < 0 || state.getPushReaction() == EnumPushReaction.DESTROY || state.getMaterial() == Material.PORTAL || !world.isBlockModifiable(player, pos)) {
            return false;
        }

        SerializableBlock serializableBlock = new SerializableBlock(state, tile);

        if (!serializableBlock.isEmpty()) {
            if (tile != null) {
                world.removeTileEntity(pos);
            }

            if (!world.setBlockToAir(pos)) {
                return false;
            }

            world.notifyNeighborsOfStateChange(pos, Blocks.AIR, true);
            NBTTagCompound stored = serializableBlock.serializeNBT();
            stored.setString(TAG_TYPE, "block");
            focus.setTagInfo(TAG_STORAGE, stored);
            return true;
        }
        return false;
    }

    private boolean placeBlock(World world, EntityPlayerMP player, BlockPos hitPos, EnumFacing side, ItemStack focus) {
        NBTTagCompound focusTag = focus.getTagCompound();

        if (side == null || focusTag == null || !focusTag.hasKey(TAG_STORAGE, Constants.NBT.TAG_COMPOUND)) {
            return false;
        }

        NBTTagCompound stored = focusTag.getCompoundTag(TAG_STORAGE);

        SerializableBlock serializableBlock = new SerializableBlock(stored);
        if(!serializableBlock.isEmpty()) {
            BlockPos placePos = hitPos;
            IBlockState currState = world.getBlockState(placePos);

            if(!currState.getBlock().isReplaceable(world, placePos)) {
                placePos = placePos.offset(side);
            }

            if(this.isValidPlacementPosition(player, world, placePos, side, focus, serializableBlock.getState())) {
                if(serializableBlock.placeBlockInWorld(world, placePos)) {
                    focusTag.removeTag(TAG_STORAGE);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean storeEntity(EntityLivingBase entityHit, ItemStack focus) {
        if(!entityHit.isEntityAlive() || entityHit instanceof EntityPlayer || !entityHit.isNonBoss()) {
            return false;
        }

        SerializableEntity serializableEntity = new SerializableEntity(entityHit);
        if(!serializableEntity.isEmpty()) {
            NBTTagCompound stored = serializableEntity.serializeNBT();
            stored.setString(TAG_TYPE, "entity");
            focus.setTagInfo(TAG_STORAGE, stored);
            entityHit.setDead();
            return true;
        }

        return false;
    }

    private boolean placeEntity(World world, BlockPos pos, ItemStack focus) {
        NBTTagCompound focusTag = focus.getTagCompound();

        if (focusTag == null || !focusTag.hasKey(TAG_STORAGE, Constants.NBT.TAG_COMPOUND)) {
            return false;
        }

        NBTTagCompound stored = focusTag.getCompoundTag(TAG_STORAGE);
        SerializableEntity serializableEntity = new SerializableEntity(stored);
        if(!serializableEntity.isEmpty() && serializableEntity.spawnEntity(world, pos)) {
            focusTag.removeTag(TAG_STORAGE);
            return true;
        }
        return false;
    }

    private boolean isValidPlacementPosition(EntityPlayer player, World world, BlockPos pos, EnumFacing side, ItemStack toPlace, IBlockState toPlaceState) {
        if(pos.getY() >= 0 && pos.getY() < world.getHeight()) {
            IBlockState placementState = world.getBlockState(pos);
            return world.isBlockModifiable(player, pos)
                    && player.canPlayerEdit(pos, side, toPlace)
                    && placementState.getBlock().isReplaceable(world, pos)
                    && world.mayPlace(toPlaceState.getBlock(), pos, false, side, player);
            }
        return false;
    }

    private void playEffects(World world, RayTraceResult target) {
        PacketHandler.INSTANCE.sendToAllAround(new PacketFXFocusPartImpact(target.hitVec.x, target.hitVec.y, target.hitVec.z, new String[]{getKey()}),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), target.hitVec.x, target.hitVec.y, target.hitVec.z, 64.0D));
        world.playSound(null, target.hitVec.x, target.hitVec.y, target.hitVec.z, SoundsTC.zap, SoundCategory.PLAYERS, 1.0F,
                0.7F + (float) (world.rand.nextGaussian() * 0.05F));
        FXDispatcher.INSTANCE.drawBamf(target.hitVec.x, target.hitVec.y, target.hitVec.z, 15121988, true, true, null);
    }

    @Override
    public void onCast(Entity caster) {
        caster.world.playSound(null, caster.getPosition().up(), SoundsTC.hhoff, SoundCategory.PLAYERS, 0.8F, 0.45F + (float) (caster.world.rand.nextGaussian() * 0.05F));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticleFX(World world, double posX, double posY, double posZ, double velX, double velY, double velZ) {
        FXGeneric fb = new FXGeneric(world, posX, posY, posZ, velX + world.rand.nextGaussian() * 0.01, velY + world.rand.nextGaussian() * 0.01, velZ + world.rand.nextGaussian() * 0.01);
        fb.setMaxAge(9);
        fb.setRBGColorF(0.25f + world.rand.nextFloat() * 0.25f, 0.25f + world.rand.nextFloat() * 0.25f, 0.25f + world.rand.nextFloat() * 0.25f);
        fb.setAlphaF(0.0f, 0.6f, 0.6f, 0.0f);
        fb.setGridSize(64);
        fb.setParticles(448, 9, 1);
        fb.setScale(0.5f, 0.25f);
        fb.setGravity((float) (world.rand.nextGaussian() * 0.009999999776482582));
        fb.setRandomMovementScale(0.0025f, 0.0025f, 0.0025f);
        ParticleEngine.addEffect(world, fb);
    }
}
