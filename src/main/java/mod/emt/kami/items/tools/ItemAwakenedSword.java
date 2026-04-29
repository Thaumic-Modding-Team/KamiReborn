package mod.emt.kami.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import mod.emt.kami.registry.ModItemsKAMI;
import mod.emt.kami.registry.ModSoundsKAMI;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.api.entities.ITaintedMob;

import java.util.List;
import java.util.function.Predicate;

public class ItemAwakenedSword extends ItemIchoriumSword {
    public static Predicate<EntityLivingBase> ABOMINATION_PREDICATE = entity -> entity instanceof ITaintedMob || entity instanceof IEldritchMob;
    public static Predicate<EntityLivingBase> DRAGON_PREDICATE = entity -> {
        if(entity instanceof EntityDragon) {
            return true;
        }
        EntityEntry entityEntry = EntityRegistry.getEntry(entity.getClass());
        if(entityEntry != null && entityEntry.getRegistryName() != null) {
            if(entityEntry.getRegistryName().toString().contains("dragon") || entityEntry.getRegistryName().toString().contains("draconic")) {
                return true;
            }
        }
        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if(slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                ItemStack armorStack = entity.getItemStackFromSlot(slot);
                if(!armorStack.isEmpty() && armorStack.getItem().getRegistryName() != null && armorStack.getItem().getRegistryName().toString().contains("draconic")) {
                    return true;
                }
            }
        }
        return false;
    };
    public static Predicate<EntityLivingBase> UNDEAD_PREDICATE = EntityLivingBase::isEntityUndead;
    public static Predicate<EntityLivingBase> LIVING_PREDICATE = entity -> !UNDEAD_PREDICATE.test(entity) && !DRAGON_PREDICATE.test(entity) && !ABOMINATION_PREDICATE.test(entity);

    public ItemAwakenedSword() {
        super("awakened_ichorium_sword");
        this.addPropertyOverride(new ResourceLocation("slayer_mode"), ((stack, worldIn, entityIn) -> EnumSlayerMode.getMode(stack).ordinal()));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, @NotNull EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            EnumSlayerMode mode = EnumSlayerMode.getMode(heldStack).nextMode();
            EnumSlayerMode.setMode(heldStack, mode);
            world.playSound(null, player.getPosition(), ModSoundsKAMI.ITEM_ICHOR_TOGGLE.getSoundEvent(), SoundCategory.PLAYERS, 1.0f, 1.5f);
            player.sendStatusMessage(new TextComponentTranslation("tooltip.kami.tool.slayer_mode." + mode).setStyle(new Style().setColor(mode.getTextColor())), true);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public @NotNull Multimap<String, AttributeModifier> getAttributeModifiers(@NotNull EntityEquipmentSlot slot, @NotNull ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND && EnumSlayerMode.getMode(stack) == EnumSlayerMode.NORMAL) {
            multimap = HashMultimap.create();
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 15.0, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }
        return multimap;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        EnumSlayerMode mode = EnumSlayerMode.getMode(stack);
        tooltip.add(mode.getTextColor() + I18n.format("tooltip.kami.tool.slayer_mode." + mode));
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        if(event.getSource().getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase player = (EntityLivingBase) event.getSource().getTrueSource();
            ItemStack heldStack = player.getHeldItemMainhand();
            if(!heldStack.isEmpty() && heldStack.getItem() == ModItemsKAMI.AWAKENED_ICHORIUM_SWORD) {
                EnumSlayerMode mode = EnumSlayerMode.getMode(heldStack);
                EntityLivingBase target = event.getEntityLiving();
                if(mode.isEntityValid(target)) {
                    float damage = event.getAmount();
                    float percentDamage = target.getMaxHealth() * 0.2f;
                    event.getSource().setDamageBypassesArmor();
                    if(percentDamage > damage) {
                        event.setAmount(percentDamage);
                    }
                }
            }
        }

    }

    public enum EnumSlayerMode {
        NORMAL(TextFormatting.GRAY, entityLivingBase -> false),
        LIVING(TextFormatting.DARK_RED, LIVING_PREDICATE),
        UNDEAD(TextFormatting.DARK_GREEN, UNDEAD_PREDICATE),
        DRAGON(TextFormatting.BLUE, DRAGON_PREDICATE),
        ABOMINATION(TextFormatting.DARK_PURPLE, ABOMINATION_PREDICATE);

        private final TextFormatting textColor;
        private final Predicate<EntityLivingBase> entityPredicate;

        EnumSlayerMode(TextFormatting textColor, Predicate<EntityLivingBase> entityPredicate) {
            this.textColor = textColor;
            this.entityPredicate = entityPredicate;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public TextFormatting getTextColor() {
            return this.textColor;
        }

        public boolean isEntityValid(EntityLivingBase entity) {
            return this.entityPredicate.test(entity);
        }

        public EnumSlayerMode nextMode() {
            EnumSlayerMode[] values = EnumSlayerMode.values();
            return values[(this.ordinal() + 1) % values.length];
        }

        public static EnumSlayerMode getMode(ItemStack stack) {
            EnumSlayerMode[] values = EnumSlayerMode.values();
            int ordinal = stack.getTagCompound() != null ? stack.getTagCompound().getInteger("mode") : 0;
            ordinal = MathHelper.clamp(ordinal, 0, values.length - 1);
            return values[ordinal];
        }

        public static void setMode(ItemStack stack, EnumSlayerMode mode) {
            stack.setTagInfo("mode", new NBTTagInt(mode.ordinal()));
        }

    }
}
