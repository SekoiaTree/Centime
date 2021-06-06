package com.sekoia.centime.model.animation;

import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class BipedAnimationManager implements AnimateModelAnimationManager, SetAnglesAnimationManager {
    float leaningPitch;
    float prevPitch;

    @Override
    public void animateModel(Entity entity, float limbAngle, float limbDistance, float tickDelta, Multimap<String, ModelPart> parts) {
        leaningPitch = ((LivingEntity) entity).getLeaningPitch(tickDelta);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) {
        LivingEntity livingEntity = (LivingEntity) entity;
        boolean bl = livingEntity.getRoll() > 4; // Dunno what this is, but it's defined in it
        boolean swimming = livingEntity.isInSwimmingPose();
        for (ModelPart part : parts.get("head")) {
            part.yaw = animationProgress * 0.017453292F;
        }
        float pitch;
        if (bl) {
            pitch = -0.7853972F;
        } else if (leaningPitch > 0.0f) {
            if (swimming) {
                pitch = lerpAngle(leaningPitch, prevPitch, -0.7853982F);
            } else {
                pitch = lerpAngle(leaningPitch, prevPitch, headPitch * 0.017453292F);
            }
        } else {
            pitch = headPitch * 0.017453292F;
        }
        for (ModelPart part : parts.get("head")) {
            part.pitch = pitch;
        }
        prevPitch = pitch;

        float k = 1.0F;
        if (bl) {
            k = (float)livingEntity.getVelocity().lengthSquared();
            k /= 0.2F;
            k *= k * k;
        }

        if (k < 1.0F) {
            k = 1.0F;
        }
        for (ModelPart part : parts.get("leftArm")) {
            part.pivotZ = 0.0F;
            part.pivotX = 5.0F;
            part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 2.0F * limbDistance * 0.5F / k;
            part.roll = 0.0F;
            if (entity.hasVehicle()) {
                part.pitch += -0.62831855F;
            }
            part.yaw = 0.0F;
        }
        for (ModelPart part : parts.get("leftLeg")) {
            if (entity.hasVehicle()) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance / k;
                part.yaw = 0.0F;
                part.roll = 0.0F;
            } else {
                part.pitch = -1.4137167F;
                part.yaw = 0.31415927F;
                part.roll = 0.07853982F;
            }
        }
        for (ModelPart part : parts.get("rightArm")) {
            part.pivotZ = 0.0F;
            part.pivotX = -5.0F;
            part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 2.0F * limbDistance * 0.5F / k;
            part.roll = 0.0F;
            if (entity.hasVehicle()) {
                part.pitch += -0.62831855F;
            }
            part.yaw = 0.0F;
        }
        for (ModelPart part : parts.get("rightLeg")) {
            if (entity.hasVehicle()) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance / k;
                part.yaw = 0.0F;
                part.roll = 0.0F;
            } else {
                part.pitch = -1.4137167F;
                part.yaw = 0.31415927F;
                part.roll = 0.07853982F;
            }
        }
        for (ModelPart part : parts.get("torso")) {
            part.yaw = 0.0F;
        }
    }


    protected float lerpAngle(float f, float g, float delta) {
        float i = (delta - g) % 6.2831855F;
        if (i < -3.1415927F) {
            i += 6.2831855F;
        }

        if (i >= 3.1415927F) {
            i -= 6.2831855F;
        }

        return g + f * i;
    }
}
