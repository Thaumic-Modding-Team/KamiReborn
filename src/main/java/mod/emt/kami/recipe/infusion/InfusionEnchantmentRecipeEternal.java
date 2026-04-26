package mod.emt.kami.recipe.infusion;

import mod.emt.kami.utils.helpers.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.InfusionEnchantmentRecipe;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.List;
import java.util.Random;

public class InfusionEnchantmentRecipeEternal extends InfusionEnchantmentRecipe {
    public EnumInfusionEnchantment enchantmentKami;

    public InfusionEnchantmentRecipeEternal(EnumInfusionEnchantment ench, AspectList as, Object... components) {
        super(ench, as, components);
        this.enchantmentKami = ench;
    }

    public InfusionEnchantmentRecipeEternal(EnumInfusionEnchantment ench, InfusionEnchantmentRecipe recipe, ItemStack in) {
        super(recipe, in);
        this.enchantmentKami = ench;
    }

    @Override
    public boolean matches(List<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
        return central.isItemStackDamageable() && central.getItem().isRepairable();
    }

    @Override
    public Object getRecipeOutput(EntityPlayer player, ItemStack input, List<ItemStack> comps) {
        if (input == null) {
            return null;
        } else {
            ItemStack output = input.copy();
            int level = EnumInfusionEnchantment.getInfusionEnchantmentLevel(output, this.enchantmentKami);
            if (level >= this.enchantmentKami.maxLevel) {
                return null;
            } else {
                List<EnumInfusionEnchantment> el = EnumInfusionEnchantment.getInfusionEnchantments(input);
                Random rand = new Random(System.nanoTime());
                if (rand.nextInt(10) < el.size()) {
                    int base = 1;
                    if (input.getTagCompound() != null) {
                        base += input.getTagCompound().getByte("TC.WARP");
                    }

                    output.setTagInfo("TC.WARP", new NBTTagByte((byte)base));
                }

                EnumInfusionEnchantment.addInfusionEnchantment(output, this.enchantmentKami, level + 1);
                if(output.isItemDamaged()) {
                    output.setItemDamage(0);
                }
                ItemHelper.setUnbreakable(output);
                return output;
            }
        }
    }
}
