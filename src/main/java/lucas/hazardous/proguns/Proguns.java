package lucas.hazardous.proguns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.UUID;

public class Proguns implements ModInitializer {

    public static final String MOD_ID = "proguns";

    public static final Identifier PacketID = new Identifier(Proguns.MOD_ID);

    public static final ItemGroup PRO_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "proguns_group")).build();

    public static final ProgunItem PRO_GUN = new ProgunItem(new FabricItemSettings().group(PRO_GROUP).maxCount(1).maxDamage(200).fireproof());

    public static final ProSmgGunItem PRO_SMG_GUN_ITEM = new ProSmgGunItem(new FabricItemSettings().group(PRO_GROUP).maxCount(1).maxDamage(100).fireproof());

    public static final EntityType<ExplosiveEntity> SNOWBALL_ENTITY_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "explosive"),
            FabricEntityTypeBuilder.<ExplosiveEntity>create(SpawnGroup.MISC, ExplosiveEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(4).trackRangeBlocks(10)
                    .build());

    public static final Item ExplosiveItem = new ExplosiveItem(new Item.Settings().group(PRO_GROUP).maxCount(1));

    public static final EntityType<ProEntity> PRO_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "pro_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ProEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).fireImmune().build());

    public static final EntityModelLayer PRO_ENTITY_LAYER = new EntityModelLayer(new Identifier(MOD_ID, "pro_entity"), "main");

    public static final Item PRO_ENTITY_SPAWN_EGG = new SpawnEggItem(PRO_ENTITY, 1500000, 16500000, new Item.Settings().group(PRO_GROUP).rarity(Rarity.EPIC));

    public static final Item PRO_SNIPER_GUN = new ProSniperGun(new FabricItemSettings().maxCount(1).maxDamage(250).fireproof().group(PRO_GROUP));

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            dispatcher.register(literal("bonk").executes(context -> {
                context.getSource().getPlayer().setHealth(0.5F);
                context.getSource().getPlayer().giveItemStack(new ItemStack(PRO_GUN, 1));
                context.getSource().getPlayer().giveItemStack(new ItemStack(PRO_SMG_GUN_ITEM, 1));
                return 0;
            }));
        }));

        FabricDefaultAttributeRegistry.register(PRO_ENTITY, ProEntity.createMobAttributes());
        EntityRendererRegistry.register(Proguns.PRO_ENTITY, (context) -> new ProEntityRenderer(context));
        EntityModelLayerRegistry.registerModelLayer(PRO_ENTITY_LAYER, ProEntityModel::getTexturedModelData);

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pro_entity_spawn_egg"), PRO_ENTITY_SPAWN_EGG);

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pro_gun"), PRO_GUN);

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pro_smg_gun"), PRO_SMG_GUN_ITEM);

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pro_sniper_gun"), PRO_SNIPER_GUN);

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "explosive_item"), ExplosiveItem);
        EntityRendererRegistry.register(Proguns.SNOWBALL_ENTITY_ENTITY_TYPE, (context) -> new FlyingItemEntityRenderer(context));
        receiveEntityPacket();
    }

    public void receiveEntityPacket() {
        ClientSidePacketRegistryImpl.INSTANCE.register(PacketID, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());

            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);

            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Entity spawn in a null world.");

                Entity e = et.create(MinecraftClient.getInstance().world);

                if (e == null)
                    throw new IllegalStateException("Failed to create " + Registry.ENTITY_TYPE.getId(et));

                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.setPitch(pitch);
                e.setYaw(yaw);
                e.setId(entityId);
                e.setUuid(uuid);

                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }
}