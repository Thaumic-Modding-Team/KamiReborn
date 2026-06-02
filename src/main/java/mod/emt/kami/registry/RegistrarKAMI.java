package mod.emt.kami.registry;

import mod.emt.kami.Kami;
import mod.emt.kami.client.render.RenderThrownAxe;
import mod.emt.kami.entities.EntityThrownAxe;
import mod.emt.kami.recipe.crafting.DyeableItemRecipe;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
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
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        ModItemsKAMI.initItems();
        ModBlocksKAMI.MOD_BLOCKS.forEach(block -> registry.register(new ItemBlock(block)
                        .setRegistryName(Objects.requireNonNull(block.getRegistryName()))
                        .setTranslationKey(block.getTranslationKey())
                        .setCreativeTab(block.getCreativeTab())
        ));
        ModItemsKAMI.MOD_ITEMS.forEach(registry::register);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocksKAMI.MOD_BLOCKS.forEach(block -> {
            ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), "inventory");
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, loc);
        });
        ModItemsKAMI.MOD_ITEMS.forEach(item -> {
            ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, loc);
        });
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ModRecipesKAMI.initRecipes(event);
        event.getRegistry().register(new DyeableItemRecipe().setRegistryName(new ResourceLocation(Kami.MOD_ID, "dyeable_item")));
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(@Nonnull final ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityThrownAxe.class, new RenderThrownAxe.Factory());
    }
}
