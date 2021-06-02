package com.sekoia.centime.raw;

import com.sekoia.centime.model.CustomModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.Entity;

public class RawModelPart {
    private final String animation;
    private final int[] textureSize;
    private float[] pivot;
    private float[] rotation;
    private final RawCuboid[] boxes;
    private final RawModelPart submodel;
    private final RawModelPart[] submodels;

    public RawModelPart(String animation, int[] textureSize, float[] pivot, float[] rotation, RawCuboid[] boxes, RawModelPart submodel, RawModelPart[] submodels) {
        this.animation = animation;
        this.textureSize = textureSize;
        this.pivot = pivot;
        this.rotation = rotation;
        this.boxes = boxes;
        this.submodel = submodel;
        this.submodels = submodels;
    }

    public ModelPart createModelPart(CustomModel<? extends Entity> model) {
        ModelPart part = new ModelPart(model);
        if (textureSize != null ) {
            part.setTextureSize(textureSize[0], textureSize[1]);
        }
        if (pivot == null) {
            pivot = new float[3];
        }
        if (rotation == null) {
            rotation = new float[3];
        }

        part.setPivot(pivot[0], pivot[1], pivot[2]);
        part.pitch = rotation[0]*0.017453289f; // Convert degrees to radians
        part.yaw = rotation[1]*0.017453289f;
        part.roll = rotation[2]*0.017453289f;
        if (boxes != null) {
            for (RawCuboid cuboid : boxes) {
                part.addCuboid(
                            "", // I have to do this because mojang is dumb.
                            cuboid.coordinates[0],
                            cuboid.coordinates[1],
                            cuboid.coordinates[2],
                            (int) Math.floor(cuboid.coordinates[3]),
                            (int) Math.floor(cuboid.coordinates[4]),
                            (int) Math.floor(cuboid.coordinates[5]),
                            cuboid.sizeAdd,
                            cuboid.textureOffset[0],
                            cuboid.textureOffset[1]
                );
            }
        }

        if (submodels != null) {
            for (RawModelPart modelPart : submodels) {
                part.addChild(modelPart.createModelPart(model));
            }
        }
        if (submodel != null) {
            part.addChild(submodel.createModelPart(model));
        }
        return part;
    }

    public String getAnimation() {
        return animation;
    }
}
