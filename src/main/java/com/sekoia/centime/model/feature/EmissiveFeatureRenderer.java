package com.sekoia.centime.model.feature;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class EmissiveFeatureRenderer<T extends Entity, M extends EntityModel<T>> extends EyesFeatureRenderer<T, M> {
    private final RenderLayer SKIN;
    public EmissiveFeatureRenderer(Identifier id, FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
        SKIN = RenderLayer.getEyes(id);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}
