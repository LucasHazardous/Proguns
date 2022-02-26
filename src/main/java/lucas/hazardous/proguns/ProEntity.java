package lucas.hazardous.proguns;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class ProEntity extends PathAwareEntity {
    protected ProEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new WanderAroundGoal(this, 0.8f, 1));
    }
}
