package com.sekoia.centime.model.animation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.UnmodifiableIterator;
import com.sekoia.centime.CentimeInit;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.Collection;

public class AnimationManagers {
    public static void registerAnimationManagers() {
        registerSetAngleManagers();
        registerAnimateModelManagers();
    }

    private static void registerSetAngleManagers() {
        // TODO: a lot of these can be optimized by doing a value on init, since the value is never changed.
        registerSetAngles(new Identifier("minecraft", "spider"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart head : parts.get("head")) {
                head.yaw = headYaw * 0.017453292F;
                head.pitch = headPitch * 0.017453292F;
            }
            final float f = 0.7853982F;
            for (ModelPart rightBackLeg : parts.get("rightLeg4")) {
                rightBackLeg.roll = -f;
                rightBackLeg.yaw = f;
            }
            for (ModelPart leftBackLeg : parts.get("leftLeg4")) {
                leftBackLeg.roll = f;
                leftBackLeg.yaw = -f;
            }
            for (ModelPart rightFrontLeg : parts.get("rightLeg1")) {
                rightFrontLeg.roll = -f;
                rightFrontLeg.yaw = -f;
            }
            for (ModelPart leftFrontLeg : parts.get("leftLeg1")) {
                leftFrontLeg.roll = f;
                leftFrontLeg.yaw = f;
            }
            for (ModelPart rightBackMiddleLeg : parts.get("rightLeg3")) {
                rightBackMiddleLeg.roll = -0.58119464F;
                rightBackMiddleLeg.yaw = f/2;
            }
            for (ModelPart leftBackMiddleLeg : parts.get("leftLeg3")) {
                leftBackMiddleLeg.roll = 0.58119464F;
                leftBackMiddleLeg.yaw = -f/2;
            }
            for (ModelPart rightFrontMiddleLeg : parts.get("rightLeg2")) {
                rightFrontMiddleLeg.roll = -0.58119464F;
                rightFrontMiddleLeg.yaw = -f/2;
            }
            for (ModelPart leftFrontMiddleLeg : parts.get("leftLeg2")) {
                leftFrontMiddleLeg.roll = 0.58119464F;
                leftFrontMiddleLeg.yaw = f/2;
            }
            UnmodifiableIterator<Collection<ModelPart>> partNames = ImmutableList.of(
                    parts.get("rightLeg4"), parts.get("leftLeg4"),
                    parts.get("rightLeg2"), parts.get("leftLeg2"),
                    parts.get("rightLeg3"), parts.get("leftLeg3"),
                    parts.get("rightLeg1"), parts.get("leftLeg1")).iterator();
            for (int i = 0; i < 4; i++) {
                float sin = Math.abs(MathHelper.sin(limbAngle * 0.6662F + i*1.5707964F) * 0.4F) * limbDistance;
                float cos = -MathHelper.cos(limbAngle * 1.3324f + i*1.5707964F) * 0.4F * limbDistance;
                for (ModelPart m : partNames.next()) {
                    m.roll += sin;
                    m.yaw += cos;
                }
                for (ModelPart m : partNames.next()) {
                    m.roll -= sin;
                    m.yaw -= cos;
                }
            }
        });
        registerSetAngles(new Identifier("minecraft", "creeper"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("head")) {
                part.yaw = headYaw * 0.017453292F;
                part.pitch = headPitch * 0.017453292F;
            }
            float f = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            for (ModelPart part : parts.get("rightBackLeg")) {
                part.pitch = f;
            }
            for (ModelPart part : parts.get("leftBackLeg")) {
                part.pitch = -f;
            }
            for (ModelPart part : parts.get("rightFrontLeg")) {
                part.pitch = f;
            }
            for (ModelPart part : parts.get("leftFrontLeg")) {
                part.pitch = f;
            }
        });


        registerSetAngles(new Identifier("minecraft", "armor_stand"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            ArmorStandEntity armorStandEntity = (ArmorStandEntity) entity;
            for (ModelPart part : parts.get("head")) {
                part.pitch = 0.017453292F * armorStandEntity.getHeadRotation().getPitch();
                part.yaw = 0.017453292F * armorStandEntity.getHeadRotation().getYaw();
                part.roll = 0.017453292F * armorStandEntity.getHeadRotation().getRoll();
                part.setPivot(0.0F, 1.0F, 0.0F);
            }
            for (ModelPart part : parts.get("leftArm")) {
                part.pitch = 0.017453292F * armorStandEntity.getLeftArmRotation().getPitch();
                part.yaw = 0.017453292F * armorStandEntity.getLeftArmRotation().getYaw();
                part.roll = 0.017453292F * armorStandEntity.getLeftArmRotation().getRoll();
                part.visible = armorStandEntity.shouldShowArms();
            }
            for (ModelPart part : parts.get("leftLeg")) {
                part.pitch = 0.017453292F * armorStandEntity.getLeftLegRotation().getPitch();
                part.yaw = 0.017453292F * armorStandEntity.getLeftLegRotation().getYaw();
                part.roll = 0.017453292F * armorStandEntity.getLeftLegRotation().getRoll();
                part.setPivot(1.9F, 12.0F, 0.0F);
            }
            for (ModelPart part : parts.get("plate")) {
                part.visible = !armorStandEntity.shouldHideBasePlate();
            }
            for (ModelPart part : parts.get("rightArm")) {
                part.pitch = 0.017453292F * armorStandEntity.getRightArmRotation().getPitch();
                part.yaw = 0.017453292F * armorStandEntity.getRightArmRotation().getYaw();
                part.roll = 0.017453292F * armorStandEntity.getRightArmRotation().getRoll();
                part.visible = armorStandEntity.shouldShowArms();
            }
            for (ModelPart part : parts.get("rightLeg")) {
                part.pitch = 0.017453292F * armorStandEntity.getRightLegRotation().getPitch();
                part.yaw = 0.017453292F * armorStandEntity.getRightLegRotation().getYaw();
                part.roll = 0.017453292F * armorStandEntity.getRightLegRotation().getRoll();
                part.setPivot(-1.9F, 12.0F, 0.0F);
            }
            for (ModelPart part : parts.get("torso")) {
                part.pitch = 0.017453292F * armorStandEntity.getBodyRotation().getPitch();
                part.yaw = 0.017453292F * armorStandEntity.getBodyRotation().getYaw();
                part.roll = 0.017453292F * armorStandEntity.getBodyRotation().getRoll();
            }
        });
        registerSetAngles(new Identifier("minecraft", "bat"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            if (((BatEntity) entity).isRoosting()) {
                for (ModelPart part : parts.get("body")) {
                    part.pitch = 3.1415927F;
                }
                for (ModelPart part : parts.get("head")) {
                    part.pitch = headPitch * 0.017453292F;
                    part.yaw = 3.1415927F - headYaw * 0.017453292F;
                    part.roll = 3.1415927F;
                    part.setPivot(0.0F, -2.0F, 0.0F);
                }
                for (ModelPart part : parts.get("leftWing")) {
                    part.pitch = -0.15707964F;
                    part.yaw = 1.2566371F;
                    part.setPivot(3.0F, 0.0F, 3.0F);
                }
                for (ModelPart part : parts.get("leftWingTip")) {
                    part.yaw = 1.7278761F;
                }
                for (ModelPart part : parts.get("rightWing")) {
                    part.pitch = -0.15707964F;
                    part.yaw = -1.2566371F;
                    part.setPivot(-3.0F, 0.0F, 3.0F);
                }
                for (ModelPart part : parts.get("rightWingTip")) {
                    part.yaw = -1.7278761F;
                }
            } else {
                for (ModelPart part : parts.get("body")) {
                    part.pitch = 0.7853982F + MathHelper.cos(animationProgress * 0.1F) * 0.15F;
                    part.yaw = 0.0F;
                }
                for (ModelPart part : parts.get("head")) {
                    part.pitch = headPitch * 0.017453292F;
                    part.yaw = headYaw * 0.017453292F;
                    part.roll = 0.0F;
                    part.setPivot(0.0F, 0.0F, 0.0F);
                }
                float yaw = MathHelper.cos(animationProgress * 1.3F) * 0.7853982f;
                for (ModelPart part : parts.get("leftWing")) {
                    part.yaw = -yaw;
                    part.setPivot(0.0F, 0.0F, 0.0F);
                }
                for (ModelPart part : parts.get("leftWingTip")) {
                    part.yaw = -yaw * 0.5F;
                }
                for (ModelPart part : parts.get("rightWing")) {
                    part.yaw = yaw;
                    part.setPivot(0.0F, 0.0F, 0.0F);
                }
                for (ModelPart part : parts.get("rightWingTip")) {
                    part.yaw = yaw * 0.5F;
                }
            }
        });
        registerSetAngles(new Identifier("minecraft", "bee"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("body")) {
                part.pitch = 0.0F;
                part.pivotY = 19.0F;
            }
            for (ModelPart part : parts.get("leftAntenna")) {
                part.pitch = 0.0F;
            }
            for (ModelPart part : parts.get("rightAntenna")) {
                part.pitch = 0.0F;
            }
            boolean bl = entity.isOnGround() && entity.getVelocity().lengthSquared() < 1.0E-7D;
            if (bl) {
                for (ModelPart part : parts.get("backLegs")) {
                    part.pitch = 0.0F;
                }
                for (ModelPart part : parts.get("frontLegs")) {
                    part.pitch = 0.0F;
                }
                for (ModelPart part : parts.get("leftWing")) {
                    part.pitch = 0.0F;
                    part.yaw = 0.2618F;
                    part.roll = 0.0F;
                }
                for (ModelPart part : parts.get("middleLegs")) {
                    part.pitch = 0.0F;
                }
                for (ModelPart part : parts.get("rightWing")) {
                    part.yaw = -0.2618F;
                    part.roll = 0.0F;
                }

            } else {
                for (ModelPart part : parts.get("backLegs")) {
                    part.pitch = 0.7853982F;
                }
                for (ModelPart part : parts.get("body")) {
                    part.pitch = 0.0F;
                    part.yaw = 0.0F;
                    part.roll = 0.0F;
                }
                for (ModelPart part : parts.get("frontLegs")) {
                    part.pitch = 0.7853982F;
                }
                for (ModelPart part : parts.get("leftWing")) {
                    part.pitch = 0.0F;
                    part.yaw = 0.0F;
                    part.roll = -MathHelper.cos(animationProgress * 2.1F) * 3.1415927F * 0.15F;
                }
                for (ModelPart part : parts.get("middleLegs")) {
                    part.pitch = 0.7853982F;
                }
                for (ModelPart part : parts.get("rightWing")) {
                    part.pitch = 0.0F;
                    part.yaw = 0.0F;
                    part.roll = MathHelper.cos(animationProgress * 2.1F) * 3.1415927F * 0.15F;
                }

            }
            if (((BeeEntity) entity).hasAngerTime()) {
                for (ModelPart part : parts.get("body")) {
                    part.pitch = 0.0F;
                    part.yaw = 0.0F;
                    part.roll = 0.0F;
                }
                if (!bl) {
                    float l = MathHelper.cos(animationProgress * 0.18F);

                    for (ModelPart part : parts.get("backLegs")) {
                        part.pitch = -l * 0.15707964f + 0.7853982F;
                    }
                    for (ModelPart part : parts.get("body")) {
                        part.pitch = 0.1F + l * 0.07853982f;
                        part.pivotY = 19.0F - MathHelper.cos(animationProgress * 0.18F) * 0.9F;
                    }
                    for (ModelPart part : parts.get("frontLegs")) {
                        part.pitch = -l * 0.31415927f + 0.3926991F;
                    }
                    for (ModelPart part : parts.get("leftAntenna")) {
                        part.pitch = l * 3.1415927F * 0.03F;
                    }
                    for (ModelPart part : parts.get("rightAntenna")) {
                        part.pitch = l * 3.1415927F * 0.03F;
                    }
                }
            }
        });
        registerSetAngles(new Identifier("minecraft", "blaze"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("head")) {
                part.yaw = headYaw * 0.017453292F;
                part.pitch = headPitch * 0.017453292F;
            }

            float f = animationProgress * -0.31415927f;
            for (int k = 0; k < 4; k++) {
                for (ModelPart part : parts.get("rod" + (k+1))) {
                    part.pivotY = -2.0F + MathHelper.cos(((float)(k * 2) + animationProgress) * 0.25F);
                    part.pivotX = MathHelper.cos(f) * 9.0F;
                    part.pivotZ = MathHelper.sin(f) * 9.0F;
                }
                ++f;
            }
            f = 0.7853982F + animationProgress * 0.09424778f;

            for(int k = 4; k < 8; ++k) {
                for (ModelPart part : parts.get("rod" + (k+1))) {
                    part.pivotY = 2.0F + MathHelper.cos(((float) (k * 2) + animationProgress) * 0.25F);
                    part.pivotX = MathHelper.cos(f) * 7.0F;
                    part.pivotZ = MathHelper.sin(f) * 7.0F;
                }
                ++f;
            }

            f = 0.47123894F + animationProgress * 3.1415927F * -0.05F;

            for(int k = 8; k < 12; ++k) {
                for (ModelPart part : parts.get("rods" + (k+1))) {
                    part.pivotY = 11.0F + MathHelper.cos(((float)k * 1.5F + animationProgress) * 0.5F);
                    part.pivotX = MathHelper.cos(f) * 5.0F;
                    part.pivotZ = MathHelper.sin(f) * 5.0F;
                }
                ++f;
            }
        });
        registerSetAngles(new Identifier("minecraft", "cave_spider"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart head : parts.get("head")) {
                head.yaw = headYaw * 0.017453292F;
                head.pitch = headPitch * 0.017453292F;
            }
            final float f = 0.7853982F;
            for (ModelPart rightBackLeg : parts.get("rightLeg4")) {
                rightBackLeg.roll = -f;
                rightBackLeg.yaw = f;
            }
            for (ModelPart leftBackLeg : parts.get("leftLeg4")) {
                leftBackLeg.roll = f;
                leftBackLeg.yaw = -f;
            }
            for (ModelPart rightFrontLeg : parts.get("rightLeg1")) {
                rightFrontLeg.roll = -f;
                rightFrontLeg.yaw = -f;
            }
            for (ModelPart leftFrontLeg : parts.get("leftLeg1")) {
                leftFrontLeg.roll = f;
                leftFrontLeg.yaw = f;
            }
            for (ModelPart rightBackMiddleLeg : parts.get("rightLeg3")) {
                rightBackMiddleLeg.roll = -0.58119464F;
                rightBackMiddleLeg.yaw = f/2;
            }
            for (ModelPart leftBackMiddleLeg : parts.get("leftLeg3")) {
                leftBackMiddleLeg.roll = 0.58119464F;
                leftBackMiddleLeg.yaw = -f/2;
            }
            for (ModelPart rightFrontMiddleLeg : parts.get("rightLeg2")) {
                rightFrontMiddleLeg.roll = -0.58119464F;
                rightFrontMiddleLeg.yaw = -f/2;
            }
            for (ModelPart leftFrontMiddleLeg : parts.get("leftLeg2")) {
                leftFrontMiddleLeg.roll = 0.58119464F;
                leftFrontMiddleLeg.yaw = f/2;
            }
            UnmodifiableIterator<Collection<ModelPart>> partNames = ImmutableList.of(
                    parts.get("rightLeg4"), parts.get("leftLeg4"),
                    parts.get("rightLeg2"), parts.get("leftLeg2"),
                    parts.get("rightLeg3"), parts.get("leftLeg3"),
                    parts.get("rightLeg1"), parts.get("leftLeg1")).iterator();
            for (int i = 0; i < 4; i++) {
                float sin = Math.abs(MathHelper.sin(limbAngle * 0.6662F + i*1.5707964F) * 0.4F) * limbDistance;
                float cos = -MathHelper.cos(limbAngle * 1.3324f + i*1.5707964F) * 0.4F * limbDistance;
                for (ModelPart m : partNames.next()) {
                    m.roll += sin;
                    m.yaw += cos;
                }
                for (ModelPart m : partNames.next()) {
                    m.roll -= sin;
                    m.yaw -= cos;
                }
            }
        });
        registerSetAngles(new Identifier("minecraft", "chicken"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("head")) {
                part.pitch = headPitch * 0.017453292F;
                part.yaw = headYaw * 0.017453292F;
            }
            for (ModelPart part : parts.get("leftLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("leftWing")) {
                part.roll = -animationProgress;
            }
            for (ModelPart part : parts.get("rightLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("rightWing")) {
                part.roll = animationProgress;
            }
            for (ModelPart part : parts.get("torso")) {
                part.pitch = 1.5707964F;
            }
        });
        registerSetAngles(new Identifier("minecraft", "cod"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("tail")) {
                part.yaw = (entity.isTouchingWater()) ? 1.0F : 1.5F *0.45F * MathHelper.sin(0.6f * animationProgress);
            }
        });
        registerSetAngles(new Identifier("minecraft", "cow"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("backLeftLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("backRightLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("frontLeftLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("frontRightLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("head")) {
                part.pitch = headPitch * 0.017453292F;
                part.yaw = headYaw * 0.017453292F;
            }
            for (ModelPart part : parts.get("torso")) {
                part.pitch = 1.5707964F;
            }
        });
        // Head n shit missing
        /*register(new Identifier("minecraft", "dolphin"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("body")) {
                part.pitch = headPitch * 0.017453292F;
                part.yaw = headYaw * 0.017453292F;
            }

            if (Entity.squaredHorizontalLength(entity.getVelocity()) > 1.0E-7D) {
                for (ModelPart part : parts.get("body")) {
                    part.pitch += -0.05F + -0.05F * MathHelper.cos(animationProgress * 0.3F);
                }
                for (ModelPart part : parts.get("flukes")) {
                    part.pitch = -0.2F * MathHelper.cos(animationProgress * 0.3F);
                }
                for (ModelPart part : parts.get("tail")) {
                    part.pitch = -0.1F * MathHelper.cos(animationProgress * 0.3F);
                }
            }
        });*/
        registerSetAngles(new Identifier("minecraft", "endermite"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (int i = 0; i < 4; i++) {
                for (ModelPart part : parts.get("body" + (i + 1))) {
                    part.yaw = MathHelper.cos(animationProgress * 0.9F + (float) i * 0.47123894f) * 0.03141593f * (float) (1 + Math.abs(i - 2));
                    part.pivotX = MathHelper.sin(animationProgress * 0.9F + (float) i * 0.47123894f) * 0.31415927f * (float) Math.abs(i - 2);
                }
            }
        });
        registerSetAngles(new Identifier("minecraft", "ghast"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (int i = 0; i < 9; i++) {
                for (ModelPart part : parts.get("tentacle" + (i + 1))) {
                    part.pitch = 0.2F * MathHelper.sin(animationProgress * 0.3F + i) + 0.4F;
                }
            }
        });

        registerSetAngles(new Identifier("minecraft", "silverfish"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for(int i = 0; i < 7; i++) {
                for (ModelPart part : parts.get("body" + (i + 1))) {
                    part.yaw = MathHelper.cos(animationProgress * 0.9F + (float) i * 0.47123894f) * 0.15707964f * (float) (1 + Math.abs(i - 2));
                    part.pivotX = MathHelper.sin(animationProgress * 0.9F + (float) i * 0.47123894f) * 0.62831855f * (float) Math.abs(i - 2);
                }
            }

            for (ModelPart part : parts.get("scales1")) {
                part.yaw = MathHelper.cos(animationProgress * 0.9F + 0.9424779f) * 0.15707964f;
            }
            for (ModelPart part : parts.get("scales2")) {
                part.yaw = MathHelper.cos(animationProgress * 0.9F + 1.8849558f) * 0.4712389f;
                part.pivotX = MathHelper.sin(animationProgress * 0.9F + 1.8849558f) * 0.62831855f * 2;
            }
            for (ModelPart part : parts.get("scales3")) {
                part.yaw = MathHelper.cos(animationProgress * 0.9F + 0.47123894f) * 0.31415927f;
                part.pivotX = MathHelper.sin(animationProgress * 0.9F + 0.47123894f) * 0.62831855f;
            }
        });
        // TODO these are the same as the cow stuff, but you can't just register it twice. Maybe some way to get around it and reduce duplication?
        registerSetAngles(new Identifier("minecraft", "pig"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("backLeftLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("backRightLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("frontLeftLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("frontRightLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("head")) {
                part.pitch = headPitch * 0.017453292F;
                part.yaw = headYaw * 0.017453292F;
            }
            for (ModelPart part : parts.get("torso")) {
                part.pitch = 1.5707964F;
            }
        });
        registerSetAngles(new Identifier("minecraft", "mooshroom"), (Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Multimap<String, ModelPart> parts) -> {
            for (ModelPart part : parts.get("backLeftLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("backRightLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("frontLeftLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("frontRightLeg")) {
                part.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            }
            for (ModelPart part : parts.get("head")) {
                part.pitch = headPitch * 0.017453292F;
                part.yaw = headYaw * 0.017453292F;
            }
            for (ModelPart part : parts.get("torso")) {
                part.pitch = 1.5707964F;
            }
        });
    }

    private static void registerAnimateModelManagers() {
        registerAnimateModel(new Identifier("minecraft", "bee"), (Entity entity, float limbAngle, float limbDistance, float tickDelta, Multimap<String, ModelPart> parts) -> {
            BeeEntity beeEntity = (BeeEntity) entity;
            for (ModelPart part : parts.get("stinger")) {
                part.visible = !beeEntity.hasStung();
            }
            float bodyPitch = beeEntity.getBodyPitch(tickDelta);
            if (bodyPitch > 0.0F) {
                for (ModelPart part : parts.get("body")) {
                    part.pitch = ModelUtil.interpolateAngle(part.pitch, 3.0915928F, bodyPitch);
                }
            }
        });
    }

    private static void registerSetAngles(Identifier entity, SetAnglesAnimationManager manager) {
        Registry.register(CentimeInit.SET_ANGLES_ANIMATION_MANAGER, entity, manager);
    }

    private static void registerAnimateModel(Identifier entity, AnimateModelAnimationManager manager) {
        Registry.register(CentimeInit.ANIMATE_MODEL_ANIMATION_MANAGER, entity, manager);
    }
}
