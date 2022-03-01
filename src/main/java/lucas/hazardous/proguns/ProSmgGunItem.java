package lucas.hazardous.proguns;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ProSmgGunItem extends Item {
    public ProSmgGunItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        ExplosiveItem.spawnProjectile(world, user, 5f, 5f, 5f);

        itemStack.damage(1, user, item -> item.sendToolBreakStatus(hand));

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
