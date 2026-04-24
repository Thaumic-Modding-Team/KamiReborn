package mod.emt.kami.registry;

import mod.emt.kami.Kami;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Kami.MOD_ID)
public class RegistrarKAMI {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        ModItemsKAMI.initItems();
        ModItemsKAMI.MOD_ITEMS.forEach(registry::register);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItemsKAMI.MOD_ITEMS.forEach(item -> {
            ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, loc);
        });
    }
}
