package com.sekoia.centime.model.animation;

import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.Entity;

public interface AnimateModelAnimationManager {
    void animateModel(Entity entity, float limbAngle, float limbDistance, float tickDelta, Multimap<String, ModelPart> parts);
}