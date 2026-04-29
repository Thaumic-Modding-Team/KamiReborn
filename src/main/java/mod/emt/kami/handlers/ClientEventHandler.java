package mod.emt.kami.handlers;

import baubles.api.BaublesApi;
import com.google.common.collect.ImmutableList;
import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IAreaBreakTool;
import mod.emt.kami.client.KeyBindingsKami;
import mod.emt.kami.network.PacketHandler;
import mod.emt.kami.network.packets.PacketOpenPouchGui;
import mod.emt.kami.registry.ModItemsKAMI;
import mod.emt.kami.utils.helpers.PlayerHelper;
import mod.emt.kami.utils.helpers.TreeHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Kami.MOD_ID, value = Side.CLIENT)
public class ClientEventHandler {
    public static final ResourceLocation WIDGETS = new ResourceLocation(Kami.MOD_ID, "textures/gui/widgets.png");
    private static final TextureAtlasSprite[] blockDamageIcons = new TextureAtlasSprite[10];

    @SubscribeEvent
    public static void renderExtraBlockBreak(RenderWorldLastEvent event) {
        PlayerControllerMP controllerMP = Minecraft.getMinecraft().playerController;
        if (controllerMP == null)
            return;

        EntityPlayer player = Minecraft.getMinecraft().player;
        Entity renderEntity = Minecraft.getMinecraft().getRenderViewEntity();
        ItemStack stack = player.getHeldItemMainhand();

        if (renderEntity != null && !stack.isEmpty() && stack.getItem() instanceof IAreaBreakTool) {
            IAreaBreakTool aoeTool = (IAreaBreakTool) stack.getItem();
            RayTraceResult trace = renderEntity.rayTrace(PlayerHelper.getReachDistance(player), event.getPartialTicks());

            if (trace != null) {
                ImmutableList<BlockPos> extraBlocks = aoeTool.getBreakAreaPositions(player, stack, trace.getBlockPos(), false);
                //Render bounding boxes
                for (BlockPos pos : extraBlocks) {
                    event.getContext().drawSelectionBox(player, new RayTraceResult(new Vec3d(0, 0, 0), trace.sideHit, pos), 0, event.getPartialTicks());
                }

                //Render block breaking
                if (controllerMP.getIsHittingBlock()) {
                    if (!stack.isEmpty() && stack.getItem() instanceof IAreaBreakTool) {
                        drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), player, event.getPartialTicks(), player.getEntityWorld(), extraBlocks);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            if(KeyBindingsKami.openFocusPouch.isPressed()) {
                int pouchSlot = BaublesApi.isBaubleEquipped(player, ModItemsKAMI.FOCUS_POUCH);
                if (pouchSlot > -1) {
                    PacketHandler.INSTANCE.sendToServer(new PacketOpenPouchGui());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL && Minecraft.getMinecraft().player != null) {
            Minecraft mc = Minecraft.getMinecraft();
            ItemStack mhStack = mc.player.getHeldItemMainhand();
            if(mhStack.getItem() == ModItemsKAMI.AWAKENED_ICHORIUM_AXE) {
                RayTraceResult trace = PlayerHelper.rayTrace(mc.player, 0);
                if(trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && TreeHelper.isTreeStructure(mc.world, trace.getBlockPos())) {
                    renderTimberOverlay(mc, event.getResolution());
                }
            }
        }
    }

    public static void renderTimberOverlay(Minecraft mc, ScaledResolution resolution) {
        int width = 16;
        int height = 16;
        int texX = mc.player.isSneaking() ? 16 : 0;
        int xc = resolution.getScaledWidth() / 2 + 8;
        int yc = resolution.getScaledHeight() / 2 - 8;

        mc.getTextureManager().bindTexture(WIDGETS);
        GlStateManager.enableBlend();
        mc.ingameGUI.drawTexturedModalRect(xc, yc, texX, 0, width, height);
        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(Gui.ICONS);
    }

    public static void drawBlockDamageTexture(Tessellator tessellatorIn, BufferBuilder bufferIn, Entity entityIn, float partialTicks, World world, List<BlockPos> blocks) {
        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;

        TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;

        PlayerControllerMP controllerMP = Minecraft.getMinecraft().playerController;
        int progress = (int) (controllerMP.curBlockDamageMP * 10.0F) - 1;

        if (progress < 0)
            return;

        renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        preRenderDamagedBlocks();

        bufferIn.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        bufferIn.setTranslation(-d0, -d1, -d2);
        bufferIn.noColor();

        for (BlockPos blockPos : blocks) {
            TileEntity tile = world.getTileEntity(blockPos);

            if (!(tile != null && tile.canRenderBreaking())) {
                IBlockState state = world.getBlockState(blockPos);
                if (state.getMaterial() != Material.AIR) {
                    if(blockDamageIcons[0] == null)
                        getBlockDamageIcons();
                    Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockDamage(state, blockPos, blockDamageIcons[progress], world);
                }
            }
        }
        tessellatorIn.draw();
        bufferIn.setTranslation(0.0D, 0.0D, 0.0D);
        postRenderDamagedBlocks();
    }

    /**
     * Copy from RenderGlobal preRenderDamagedBlocks
     */
    private static void preRenderDamagedBlocks() {
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        GlStateManager.doPolygonOffset(-3.0F, -3.0F);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
    }

    /**
     * Copy from RenderGlobal postRenderDamagedBlocks
     */
    private static void postRenderDamagedBlocks() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0F, 0.0F);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    public static void getBlockDamageIcons() {
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        for (int i = 0; i < blockDamageIcons.length; ++i) {
            blockDamageIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
        }
    }
}
