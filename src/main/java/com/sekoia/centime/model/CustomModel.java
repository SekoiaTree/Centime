package com.sekoia.centime.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.sekoia.centime.Centime;
import com.sekoia.centime.model.animation.AnimateModelAnimationManager;
import com.sekoia.centime.model.animation.SetAnglesAnimationManager;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class CustomModel<E extends Entity> extends EntityModel<E> implements FeatureRendererContext<E, CustomModel<E>> {
    private final ArrayList<ModelPart> parts;
    private final Identifier texture;
    private final Multimap<String, ModelPart> animatedParts;
    public final List<FeatureRenderer<E, CustomModel<E>>> features = Lists.newArrayList();

    public CustomModel(ArrayList<ModelPart> modelParts, Identifier textureId, Multimap<String, ModelPart> animatedParts) {
        this.texture = textureId;
        this.parts = modelParts;
        this.animatedParts = animatedParts;
    }
    
    public void addModelPart(ModelPart part) {
        parts.add(part);
        accept(part);
    }

    public void addCuboidToModelPart(int idx, float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
        parts.get(idx).addCuboid(x, y, z, sizeX, sizeY, sizeZ);
    }

    public Identifier getTexture() {
        return texture;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        for (ModelPart i : parts) {
            i.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }

    public final boolean addFeature(FeatureRenderer<E, CustomModel<E>> feature) {
        return this.features.add(feature);
    }

    @Override
    public CustomModel<E> getModel() {
        return this;
    }

    @Override
    public Identifier getTexture(E entity) {
        return this.texture;
    }

    @Override
    public void animateModel(E entity, float limbAngle, float limbDistance, float tickDelta) {
        AnimateModelAnimationManager manager = Centime.ANIMATE_MODEL_ANIMATION_MANAGER.get(Registry.ENTITY_TYPE.getId(entity.getType()));
        if (manager == null) {
            Centime.LOGGER.warn(String.format("Animations not implemented for %s, yet CEM is being used!", entity.getType()));
            return;
        }
        manager.animateModel(entity, limbAngle, limbDistance, tickDelta, animatedParts);
    }

    @Override
    public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        SetAnglesAnimationManager manager = Centime.SET_ANGLES_ANIMATION_MANAGER.get(Registry.ENTITY_TYPE.getId(entity.getType()));
        if (manager == null) {
            Centime.LOGGER.warn(String.format("Animations not implemented for %s, yet CEM is being used!", entity.getType()));
            return;
        }
        manager.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch, animatedParts);
    }
}
