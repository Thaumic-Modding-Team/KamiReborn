package mod.emt.kami.casters.foci;

import mod.emt.kami.Kami;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.ICaster;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;

// TODO: Seems to interact weirdly with double blocks like doors and tall plants
// TODO: Placing and storing blocks should emit smoke puff particles
@SuppressWarnings("deprecation")
public class FocusEffectDislocation extends FocusEffect {
    private static final String TAG_STORAGE = "FociDislocation";
    private static final String TAG_BLOCK = "Block";
    private static final String TAG_META = "Meta";
    private static final String TAG_TILE = "Tile";

    @Override
    public Aspect getAspect() {
        return Aspect.VOID;
    }

    @Override
    public String getKey() {
        return "focus." + Kami.MOD_ID + ".dislocation";
    }

    @Override
    public int getComplexity() {
        return 20;
    }

    // TODO: Research
    @Override
    public String getResearch() {
        return "FOCUSHEAL";
    }

    @Override
    public boolean execute(RayTraceResult target, Trajectory trajectory, float finalPower, int num) {
        World world = getPackage().world;

        if (world.isRemote || target == null || target.typeOfHit != RayTraceResult.Type.BLOCK || !(getPackage().getCaster() instanceof EntityPlayerMP)) {
            return false;
        }

        EntityPlayerMP player = (EntityPlayerMP) getPackage().getCaster();
        ItemStack focus = getFoci(player);

        if (focus.isEmpty()) {
            return false;
        }

        NBTTagCompound focusTag = focus.getTagCompound();
        BlockPos pos = target.getBlockPos();
        boolean success = focusTag != null && focusTag.hasKey(TAG_STORAGE, 10) ? placeBlock(world, player, pos, target.sideHit, focus) : storeBlock(world, player, pos, focus);

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
        Block block = state.getBlock();

        if (world.isAirBlock(pos) || state.getBlockHardness(world, pos) < 0 || !world.isBlockModifiable(player, pos)) {
            return false;
        }

        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, player);
        MinecraftForge.EVENT_BUS.post(event);
        ResourceLocation registryName = block.getRegistryName();

        if (event.isCanceled() || registryName == null) {
            return false;
        }

        NBTTagCompound stored = new NBTTagCompound();
        stored.setString(TAG_BLOCK, registryName.toString());
        stored.setInteger(TAG_META, block.getMetaFromState(state));

        TileEntity tile = world.getTileEntity(pos);
        if (tile != null) {
            NBTTagCompound tileNBT = new NBTTagCompound();
            tile.writeToNBT(tileNBT);
            stored.setTag(TAG_TILE, tileNBT);
        }

        if (!world.setBlockToAir(pos)) {
            return false;
        }

        NBTTagCompound focusTag = focus.getTagCompound();
        if (focusTag == null) {
            focusTag = new NBTTagCompound();
            focus.setTagCompound(focusTag);
        }

        focusTag.setTag(TAG_STORAGE, stored);
        return true;
    }

    private boolean placeBlock(World world, EntityPlayerMP player, BlockPos hitPos, EnumFacing side, ItemStack focus) {
        NBTTagCompound focusTag = focus.getTagCompound();

        if (side == null || focusTag == null || !focusTag.hasKey(TAG_STORAGE, 10)) {
            return false;
        }

        NBTTagCompound stored = focusTag.getCompoundTag(TAG_STORAGE);
        ResourceLocation registryName = new ResourceLocation(stored.getString(TAG_BLOCK));
        Block block = ForgeRegistries.BLOCKS.getValue(registryName);

        if (block == null) {
            return false;
        }

        IBlockState state;
        try {
            state = block.getStateFromMeta(stored.getInteger(TAG_META));
        } catch (Exception e) {
            return false;
        }

        BlockPos placePos = hitPos.offset(side);
        if (placePos.getY() < 0 || placePos.getY() >= 256 || !world.isBlockModifiable(player, placePos) || !player.canPlayerEdit(placePos, side, focus) ||
                !world.getBlockState(placePos).getBlock().isReplaceable(world, placePos) || !world.mayPlace(block, placePos, false, side, player) ||
                !world.setBlockState(placePos, state, 3)) {
            return false;
        }

        if (stored.hasKey(TAG_TILE, 10)) {
            NBTTagCompound tileNBT = stored.getCompoundTag(TAG_TILE).copy();
            tileNBT.setInteger("x", placePos.getX());
            tileNBT.setInteger("y", placePos.getY());
            tileNBT.setInteger("z", placePos.getZ());

            TileEntity tile = TileEntity.create(world, tileNBT);
            if (tile != null) {
                tile.setWorld(world);
                tile.setPos(placePos);
                world.setTileEntity(placePos, tile);
                tile.validate();
                tile.markDirty();
            }
        }

        focusTag.removeTag(TAG_STORAGE);
        return true;
    }

    private void playEffects(World world, RayTraceResult target) {
        PacketHandler.INSTANCE.sendToAllAround(new PacketFXFocusPartImpact(target.hitVec.x, target.hitVec.y, target.hitVec.z, new String[]{getKey()}),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), target.hitVec.x, target.hitVec.y, target.hitVec.z, 64.0D));
        world.playSound(null, target.hitVec.x, target.hitVec.y, target.hitVec.z, SoundsTC.zap, SoundCategory.PLAYERS, 1.0F,
                0.7F + (float) (world.rand.nextGaussian() * 0.05F));
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
