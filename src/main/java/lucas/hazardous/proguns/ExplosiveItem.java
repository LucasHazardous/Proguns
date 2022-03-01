package lucas.hazardous.proguns;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ExplosiveItem extends Item {
    public ExplosiveItem(Settings settings) {
        super(settings);
    }

    public static void spawnProjectile(World world, PlayerEntity player, float speed, float recoil, float roll) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 1f, 1f);

        if(!world.isClient) {
            ExplosiveEntity explosiveEntity = new ExplosiveEntity(player, world);
            explosiveEntity.setItem(new ItemStack(Proguns.ExplosiveItem));
            explosiveEntity.setVelocity(player, player.getPitch(), player.getYaw(), roll, speed, recoil);
            world.spawnEntity(explosiveEntity);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        ExplosiveItem.spawnProjectile(world, player, 1f, 0f, 0f);

        if(!player.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient);
    }
}
