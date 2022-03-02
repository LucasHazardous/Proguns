package lucas.hazardous.proguns;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ProSniperGun extends Item {
    private static boolean isUsing;

    public ProSniperGun(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPYGLASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0f, 1.0f);
        isUsing = true;
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ItemStack itemStack = user.getStackInHand(user.getActiveHand());

        ExplosiveItem.spawnProjectile(world, (PlayerEntity) user, 10f, 0f, 0f);
        user.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);

        itemStack.damage(1, user, item -> item.sendToolBreakStatus(user.getActiveHand()));

        isUsing = false;
    }

    public static boolean isZooming() {
        return isUsing;
    }
}
