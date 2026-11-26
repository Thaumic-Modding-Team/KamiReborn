package mod.emt.kami.registry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ElementalFireRegistry {
    public static final String TAG_ASPECT = "fire_aspect";
    private static final Set<Aspect> FIRE_TYPES = new LinkedHashSet<>();
    private final static Map<Aspect, IBlockState> FIRE_STATES = new HashMap<>();

    public static Set<Aspect> getElementalFireTypes() {
        return FIRE_TYPES;
    }

    public static void addElementalFireType(Aspect aspect) {
        FIRE_TYPES.add(aspect);
    }

    public static ItemStack getElementalFireBottle(Aspect aspect) {
        ItemStack bottle = new ItemStack(ModItemsKAMI.ELEMENTAL_FIRE_BOTTLE);
        bottle.setTagInfo(TAG_ASPECT, new NBTTagString(aspect.getTag()));
        return bottle;
    }

    @Nullable
    public static Aspect getAspectFromBottle(ItemStack stack) {
        return !stack.isEmpty() && stack.getTagCompound() != null ? Aspect.getAspect(stack.getTagCompound().getString(TAG_ASPECT)) : null;
    }

    public static IBlockState getElementalFireBlock(Aspect aspect) {
        return FIRE_STATES.getOrDefault(aspect, Blocks.AIR.getDefaultState());
    }

    public static void addElementalFireBlock(Aspect aspect, IBlockState fireState) {
        FIRE_STATES.put(aspect, fireState);
    }

    public static void registerBaseTypes() {
        addElementalFireType(Aspect.AIR);
        addElementalFireType(Aspect.EARTH);
        addElementalFireType(Aspect.FIRE);
        addElementalFireType(Aspect.WATER);
        addElementalFireType(Aspect.ORDER);
        addElementalFireType(Aspect.ORDER);
        addElementalFireType(Aspect.ENTROPY);
    }
}
