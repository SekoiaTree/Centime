package com.sekoia.centime.model.feature;

import com.google.gson.JsonObject;
import com.sekoia.centime.model.CustomModel;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;

public class FeaturePacket {
    private final FeatureBuilder builder;
    private final JsonObject metadata;

    public FeaturePacket(FeatureBuilder builder, JsonObject metadata) {
        this.builder = builder;
        this.metadata = metadata;
    }

    public FeatureRenderer<? extends Entity, ? extends EntityModel<? extends Entity>> parse(CustomModel<? extends Entity> model) {
        return builder.parse(metadata, model);
    }
}
