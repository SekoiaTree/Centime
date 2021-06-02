package com.sekoia.centime.raw;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.sekoia.centime.model.CustomModel;
import com.sekoia.centime.model.feature.FeaturePacket;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class RawModel {
    private final String texture;
    private int[] textureSize;
    private float shadowSize;
    private final RawModelPart[] models;
    private FeaturePacket[] features;

    public RawModel(String texture, int[] textureSize, float shadowSize, RawModelPart[] models) {
        this.texture = texture;
        this.textureSize = textureSize;
        this.shadowSize = shadowSize;
        this.models = models;
    }

    @SuppressWarnings("UnstableApiUsage")
    public<T extends Entity> CustomModel<T> createCustomModel() {
        Multimap<String, ModelPart> animatedParts = MultimapBuilder.hashKeys().hashSetValues().build();
        CustomModel<T> model = new CustomModel<T>(new ArrayList<>(models.length), new Identifier(texture), animatedParts);
        if (textureSize != null) {
            model.textureWidth = textureSize[0];
            model.textureHeight = textureSize[1];
        }

        if (features != null) {
            for (FeaturePacket feature : features) {
                // FIXME: god I hate generics. This might possibly cause a crash? Prob not tho.
                model.addFeature((FeatureRenderer<T, CustomModel<T>>) feature.parse(model));
            }
        }

        for (RawModelPart i : models) {
            ModelPart part = i.createModelPart(model);
            model.addModelPart(part);
            if (i.getAnimation() != null) {
                animatedParts.get(i.getAnimation()).add(part);
            }
        }
        return model;
    }
}
