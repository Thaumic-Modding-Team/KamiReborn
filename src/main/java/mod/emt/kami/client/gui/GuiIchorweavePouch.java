package mod.emt.kami.client.gui;

import mod.emt.kami.Kami;
import mod.emt.kami.inventory.containers.ContainerIchorweavePouch;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIchorweavePouch extends GuiContainer {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Kami.MOD_ID, "textures/gui/ichorweave_pouch.png");

    public GuiIchorweavePouch(Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 256;
        this.ySize = 256;
    }

    public ContainerIchorweavePouch getContainer() {
        return (ContainerIchorweavePouch) this.inventorySlots;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.renderEngine.bindTexture(TEXTURE);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int xStart = (this.width - this.xSize) / 2;
        int yStart = (this.height - this.ySize) / 2;
        GlStateManager.enableBlend();
        drawTexturedModalRect(xStart, yStart, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
