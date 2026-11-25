package mod.emt.kami.client.gui;

import mod.emt.kami.Kami;
import mod.emt.kami.client.element.GuiButtonCelestialPearl;
import mod.emt.kami.inventory.containers.ContainerCelestialTeleport;
import mod.emt.kami.inventory.handlers.CelestialTranslocatorHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCelestialTeleport extends GuiContainer {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Kami.MOD_ID, "gui/celestial_teleport");

    public GuiCelestialTeleport(ContainerCelestialTeleport inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 128;
        this.ySize = 128;
    }

    public ContainerCelestialTeleport getContainer() {
        return (ContainerCelestialTeleport) this.inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();

        //Populating the buttons
        CelestialTranslocatorHandler handler = this.getContainer().getStackHandler();
        List<Tuple<ItemStack, Integer>> slots = handler.getPearlSlots();
        int buttonId = 0;
        int size = slots.size();
        int radius = 46;

        for(int i = 0; i < size; i++) {
            double angle = Math.toRadians((360.0 / size) * i - 90);
            int xOffset = (this.width / 2) - 8 + (int) Math.round(radius * Math.cos(angle));
            int yOffset = (this.height / 2) - 8 + (int) Math.round(radius * Math.sin(angle));
            Tuple<ItemStack, Integer> tuple = slots.get(i);
            this.addButton(new GuiButtonCelestialPearl(buttonId++, xOffset, yOffset, tuple.getFirst(), tuple.getSecond()));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(TEXTURE.toString());
        int xStart = (this.width - this.xSize) / 2;
        int yStart = (this.height - this.ySize) / 2;

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        this.drawAnimatedBackground(xStart, yStart, this.xSize, this.ySize, sprite.getIconWidth(), sprite.getIconHeight(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
    }

    protected void drawAnimatedBackground(float x, float y, float width, float height, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax) {
        int iterMaxW = (int) (width / (float) iconWidth);
        int iterMaxH = (int) (height / (float) iconHeight);
        float leftoverW = width % (float) iconWidth;
        float leftoverH = height % (float) iconHeight;
        float leftoverWf = leftoverW / (float) iconWidth;
        float leftoverHf = leftoverH / (float) iconHeight;
        float iconUDif = uMax - uMin;
        float iconVDif = vMax - vMin;

        for (int ww = 0; ww < iterMaxW; ++ww) {
            for (int hh = 0; hh < iterMaxH; ++hh) {
                drawTexturedRect(x + (float) (ww * iconWidth), y + (float) (hh * iconHeight), (float) iconWidth, (float) iconHeight, uMin, uMax, vMin, vMax);
            }

            drawTexturedRect(x + (float) (ww * iconWidth), y + (float) (iterMaxH * iconHeight), (float) iconWidth, leftoverH, uMin, uMax, vMin, vMin + iconVDif * leftoverHf);
        }

        if (leftoverW > 0.0F) {
            for (int hh = 0; hh < iterMaxH; ++hh) {
                drawTexturedRect(x + (float) (iterMaxW * iconWidth), y + (float) (hh * iconHeight), leftoverW, (float) iconHeight, uMin, uMin + iconUDif * leftoverWf, vMin, vMax);
            }

            drawTexturedRect(x + (float) (iterMaxW * iconWidth), y + (float) (iterMaxH * iconHeight), leftoverW, leftoverH, uMin, uMin + iconUDif * leftoverWf, vMin, vMin + iconVDif * leftoverHf);
        }
    }

    public void drawTexturedRect(float x, float y, float w, float h, double... uv) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buff = tessellator.getBuffer();
        buff.begin(7, DefaultVertexFormats.POSITION_TEX);
        buff.pos(x, (y + h), 0.0F).tex(uv[0], uv[3]).endVertex();
        buff.pos((x + w), (y + h), 0.0F).tex(uv[1], uv[3]).endVertex();
        buff.pos((x + w), y, 0.0F).tex(uv[1], uv[2]).endVertex();
        buff.pos(x, y, 0.0F).tex(uv[0], uv[2]).endVertex();
        tessellator.draw();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        GuiButton button = this.getHoveredButton();
        if(button instanceof GuiButtonCelestialPearl) {
            List<String> tooltip = ((GuiButtonCelestialPearl) button).getTooltip();
            this.drawHoveringTooltip(mouseX, mouseY, tooltip);
        }
    }

    @Override
    protected void actionPerformed(@NotNull GuiButton button) throws IOException {
        if(button instanceof GuiButtonCelestialPearl) {
            int slot = ((GuiButtonCelestialPearl) button).getHandlerSlot();
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, slot);
            this.mc.addScheduledTask(() -> mc.displayGuiScreen(null));
        }
    }

    @Nullable
    public GuiButton getHoveredButton() {
        for(GuiButton button : this.buttonList) {
            if(button.isMouseOver()) {
                return button;
            }
        }
        return null;
    }

    public void drawHoveringTooltip(int mouseX, int mouseY, List<String> tooltip) {
        if (!tooltip.isEmpty()) {
            FontRenderer renderer = this.mc.fontRenderer;
            boolean uni = renderer.getUnicodeFlag();
            renderer.setUnicodeFlag(false);

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            int k = 0;

            for (String str : tooltip) {
                int l = renderer.getStringWidth(str);
                if (l > k) {
                    k = l;
                }
            }

            int j2 = mouseX + 12;
            int k2 = mouseY - 12;
            int i1 = 8;
            boolean shift = false;
            if (xSize > 0 && j2 + k > xSize) {
                j2 -= 28 + k;
                shift = true;
            }

            if (ySize > 0 && k2 + i1 + 6 > ySize) {
                k2 = ySize - i1 - 6;
                shift = true;
            }

            if (!shift && this.mc.currentScreen != null) {
                if (j2 + k > this.mc.currentScreen.width) {
                    j2 -= 28 + k;
                }

                if (k2 + i1 + 6 > this.mc.currentScreen.height) {
                    k2 = this.mc.currentScreen.height - i1 - 6;
                }
            }

            if (tooltip.size() > 1) {
                i1 += 2 + (tooltip.size() - 1) * 10;
            }

            GlStateManager.translate(0.0F, 0.0F, 300.0F);
            int j1 = -267386864;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);
            GlStateManager.translate(0.0F, 0.0F, -300.0F);

            for (int i2 = 0; i2 < tooltip.size(); ++i2) {
                String s1 = tooltip.get(i2);
                renderer.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();

            renderer.setUnicodeFlag(uni);
        }
    }
}
