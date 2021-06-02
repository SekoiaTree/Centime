package com.sekoia.centime.model.animation;

import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.Entity;

public interface AnimationManager {
    void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts);
}