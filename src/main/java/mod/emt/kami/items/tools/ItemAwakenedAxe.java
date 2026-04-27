package mod.emt.kami.items.tools;

import mod.emt.kami.entities.EntityThrownAxe;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemAwakenedAxe extends ItemIchoriumAxe {
    public ItemAwakenedAxe() {
        super("awakened_ichorium_axe");
        this.addPropertyOverride(new ResourceLocation("impact_mode"), ((stack, worldIn, entityIn) -> EnumImpactMode.getImpactMode(stack).ordinal()));
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(World world, @NotNull EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        if(player.isSneaking()) {
            EnumImpactMode mode = EnumImpactMode.getImpactMode(heldStack).nextMode();
            EnumImpactMode.setImpactMode(heldStack, mode);
            player.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.impact_mode." + mode).setStyle(new Style().setColor(mode.getTextColor())), true);
        } else {
            if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
                EntityThrownAxe emb = new EntityThrownAxe(world, player, heldStack.copy());
                IAttributeInstance instance = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
                if(instance != null) {
                    emb.setDamage(instance.getAttributeValue());
                }


                emb.thrownFromSlot = player.inventory.currentItem;
                world.spawnEntity(emb);
                world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 3.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                heldStack.setCount(0);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    public enum EnumImpactMode {
        DAMAGE(TextFormatting.GOLD),
        LIGHTNING(TextFormatting.BLUE),
        DETONATION(TextFormatting.DARK_GREEN),
        EXPLOSION(TextFormatting.DARK_RED);

        private final TextFormatting textColor;

        EnumImpactMode(TextFormatting textColor) {
            this.textColor = textColor;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public TextFormatting getTextColor() {
            return this.textColor;
        }

        public EnumImpactMode nextMode() {
            EnumImpactMode[] values = EnumImpactMode.values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static EnumImpactMode getImpactMode(ItemStack stack) {
            EnumImpactMode[] values = EnumImpactMode.values();
            int ordinal = stack.getTagCompound() != null ? stack.getTagCompound().getInteger("mode") : 0;
            ordinal = MathHelper.clamp(ordinal, 0, values.length - 1);
            return values[ordinal];
        }

        public static void setImpactMode(ItemStack stack, EnumImpactMode mode) {
            stack.setTagInfo("mode", new NBTTagInt(mode.ordinal()));
        }

    }
}
