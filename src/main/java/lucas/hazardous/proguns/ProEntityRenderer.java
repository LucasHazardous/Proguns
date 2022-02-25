package lucas.hazardous.proguns;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ProEntityRenderer extends MobEntityRenderer<ProEntity, ProEntityModel> {

    public ProEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ProEntityModel(context.getPart(Proguns.PRO_ENTITY_LAYER)), 1f);
    }

    @Override
    public Identifier getTexture(ProEntity entity) {
        return new Identifier("proguns", "textures/entity/pro_entity/pro_entity.png");
    }
}
