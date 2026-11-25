package mod.emt.kami.registry;

import mod.emt.kami.Kami;
import mod.emt.kami.blocks.fire.BlockElementalFire;
import mod.emt.kami.client.gui.GuiCelestialTeleport;
import mod.emt.kami.client.render.KamiItemTEISR;
import mod.emt.kami.client.render.RenderThrownAxe;
import mod.emt.kami.client.render.TileCelestialGatewayTESR;
import mod.emt.kami.entities.EntityThrownAxe;
import mod.emt.kami.items.base.ItemBlockKami;
import mod.emt.kami.tiles.TileCelestialGateway;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Kami.MOD_ID)
public class RegistrarKAMI {
    private static int entityID = 0;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        ModBlocksKAMI.initBlocks();
        ModBlocksKAMI.MOD_BLOCKS.forEach(registry::register);
        ModBlocksKAMI.registerTileEntities();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        ModItemsKAMI.initItems();
        ModBlocksKAMI.MOD_BLOCKS.stream()
                .filter(block -> !(block instanceof BlockElementalFire))
                .forEach(block -> registry.register(new ItemBlockKami(block)));
        ModItemsKAMI.MOD_ITEMS.forEach(registry::register);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocksKAMI.MOD_BLOCKS.stream()
                .filter(block -> !(block instanceof BlockElementalFire))
                .forEach(block -> {
                    ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), "inventory");
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, loc);
                });
        ModItemsKAMI.MOD_ITEMS.forEach(item -> {
            ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, loc);
        });

        //Entities
        RenderingRegistry.registerEntityRenderingHandler(EntityThrownAxe.class, new RenderThrownAxe.Factory());
        //Items
        ModItemsKAMI.AWAKENED_ICHORWEAVE_SHIELD.setTileEntityItemStackRenderer(new KamiItemTEISR());
        ModItemsKAMI.ICHORWEAVE_SHIELD.setTileEntityItemStackRenderer(new KamiItemTEISR());
        //Tiles
        ClientRegistry.bindTileEntitySpecialRenderer(TileCelestialGateway.class, new TileCelestialGatewayTESR());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        //Registering Celestial Teleport Gui because I'm lazy and don't want to deal with the animation manually
        //The texture atlas can suck it and deal with this big ol' texture
        event.getMap().registerSprite(GuiCelestialTeleport.TEXTURE);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ModRecipesKAMI.initRecipes(event);
    }

    @SubscribeEvent
    public static void onSoundEventRegistry(RegistryEvent.Register<SoundEvent> event) {
        for (ModSoundsKAMI soundEvents : ModSoundsKAMI.values()) {
            event.getRegistry().register(soundEvents.getSoundEvent());
        }
    }

    public static void registerEntity(String name, Class<? extends Entity> clazz, int eggColor1, int eggColor2) {
        EntityRegistry.registerModEntity(new ResourceLocation(Kami.MOD_ID, name), clazz, Kami.MOD_ID + "." + name, entityID++, Kami.instance, 64, 1, true, eggColor1, eggColor2);
    }

    public static void registerEntity(String name, Class<? extends Entity> clazz, int trackingRange, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(Kami.MOD_ID, name), clazz, Kami.MOD_ID + "." + name, entityID++, Kami.instance, trackingRange, 1, sendsVelocityUpdates);
    }

    @SubscribeEvent
    public static void registerEntities(@Nonnull final RegistryEvent.Register<EntityEntry> event) {
        registerEntity("thrown_awakened_ichorium_axe", EntityThrownAxe.class, 64, true);
    }
}
