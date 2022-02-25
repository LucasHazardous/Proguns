package lucas.hazardous.proguns;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ProgunItem extends Item {

    public ProgunItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        ExplosiveItem.spawnProjectile(world, user);

        itemStack.damage(5, user, item -> item.sendToolBreakStatus(hand));

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}