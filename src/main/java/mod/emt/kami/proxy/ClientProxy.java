package mod.emt.kami.proxy;

import com.invadermonky.thaumicapi.api.item.IDyeableItem;
import mod.emt.kami.blocks.fire.BlockElementalFire;
import mod.emt.kami.client.KeyBindingsKami;
import mod.emt.kami.compat.KamiCompatHandler;
import mod.emt.kami.items.misc.ItemElementalFireBottle;
import mod.emt.kami.registry.ElementalFireRegistry;
import mod.emt.kami.registry.ModBlocksKAMI;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.ICaster;
import thaumcraft.common.items.casters.ItemFocus;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        KamiCompatHandler.preInitClient();
    }

    @Override
    public void init() {
        super.init();
        KeyBindingsKami.init();

        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        IItemColor dyeableColorHandler = (stack, tintIndex) -> {
            if (tintIndex == 1 && stack.getItem() instanceof IDyeableItem) {
                return ((IDyeableItem) stack.getItem()).getDyedColor(stack);
            } else {
                return -1;
            }
        };
        IItemColor gauntletColorHandler = (stack, tintIndex) -> {
            if (tintIndex == 1 && stack.getItem() instanceof ICaster && ((ICaster) stack.getItem()).getFocus(stack) != null)
                return ((ItemFocus) ((ICaster) stack.getItem()).getFocus(stack)).getFocusColor(((ICaster) stack.getItem()).getFocusStack(stack));
            else if (tintIndex == 2 && stack.getItem() instanceof IDyeableItem) {
                return ((IDyeableItem) stack.getItem()).getDyedColor(stack);
            } else {
                return -1;
            }
        };
        IItemColor itemEssentiaColourHandler = (stack, tintIndex) -> {
            if(tintIndex == 0) {
                Aspect aspect = ElementalFireRegistry.getAspectFromBottle(stack);
                if(aspect != null) {
                    return aspect.getColor();
                }
            }
            return 0xffffff;
        };
        IBlockColor elementalFireColorHandler = (state, world, pos, tintIndex) -> {
            if (state.getBlock() instanceof BlockElementalFire) {
                Aspect aspect = ((BlockElementalFire) state.getBlock()).getFireAspect();
                if(aspect != null) {
                    return aspect.getColor();
                }
            }

            return 0xffffff;
        };

        //Dyeables
        itemColors.registerItemColorHandler(dyeableColorHandler, ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS);
        itemColors.registerItemColorHandler(dyeableColorHandler, ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD);
        itemColors.registerItemColorHandler(dyeableColorHandler, ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS);
        itemColors.registerItemColorHandler(dyeableColorHandler, ModItemsKAMI.AWAKENED_ICHORWEAVE_ROBE);
        itemColors.registerItemColorHandler(dyeableColorHandler, ModItemsKAMI.ICHORWEAVE_BOOTS);
        itemColors.registerItemColorHandler(dyeableColorHandler, ModItemsKAMI.ICHORWEAVE_HOOD);
        itemColors.registerItemColorHandler(dyeableColorHandler, ModItemsKAMI.ICHORWEAVE_LEGGINGS);
        itemColors.registerItemColorHandler(dyeableColorHandler, ModItemsKAMI.ICHORWEAVE_ROBE);
        //Casters
        itemColors.registerItemColorHandler(gauntletColorHandler, ModItemsKAMI.ICHORIUM_CASTER);
        //Colored Items
        itemColors.registerItemColorHandler(itemEssentiaColourHandler, ModItemsKAMI.ELEMENTAL_FIRE_BOTTLE);
        KamiCompatHandler.initClient();
        // Fire Blocks
        blockColors.registerBlockColorHandler(elementalFireColorHandler, ModBlocksKAMI.IGNIS_FIRE);
    }

    @Override
    public void postInit() {
        super.postInit();
        KamiCompatHandler.postInitClient();
    }
}
