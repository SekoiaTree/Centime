package com.sekoia.centime.model.feature;

import com.google.gson.JsonObject;
import com.sekoia.centime.model.CustomModel;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;

public interface FeatureBuilder {
    FeatureRenderer<? extends Entity, ? extends EntityModel<? extends Entity>> parse(JsonObject json, CustomModel<?> model);
}
