package mod.emt.kami.items.tools;

import mod.emt.kami.entities.EntityThrownAxe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemAwakenedAxe extends ItemIchoriumAxe {
    public ItemAwakenedAxe() {
        super("awakened_ichorium_axe");
        this.addPropertyOverride(new ResourceLocation("aoe_mode"), ((stack, worldIn, entityIn) -> ItemAwakenedPickaxe.EnumAoeMode.getAoeMode(stack).ordinal()));
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(World world, @NotNull EntityPlayer player, @NotNull EnumHand hand)
    {
        if (!world.isRemote)
        {
            EntityThrownAxe emb = new EntityThrownAxe(world, player, player.getHeldItem(hand).copy());
            emb.thrownFromSlot = player.inventory.currentItem;
            world.spawnEntity(emb);
            world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 3.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
            player.getHeldItem(hand).setCount(0);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
