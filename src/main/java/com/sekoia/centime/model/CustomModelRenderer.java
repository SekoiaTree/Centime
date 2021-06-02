package com.sekoia.centime.model;

import com.sekoia.centime.mixin.EntityRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class CustomModelRenderer<T extends LivingEntity> extends EntityRenderer<T> {
    private final CustomModel<T> model;
    private final LivingEntityRenderer<T, ?> normalRenderer;

    public CustomModelRenderer(EntityRenderDispatcher dispatcher, CustomModel<T> model, EntityRenderer<T> normalRenderer) {
        super(dispatcher);
        this.model = model;
        this.normalRenderer = (LivingEntityRenderer<T, ?>) normalRenderer;
    }

    @Override
    public Identifier getTexture(T entity) {
        return model.getTexture();
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevBodyYaw, entity.bodyYaw);
        float absHeadYaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevHeadYaw, entity.headYaw);
        //noinspection ConstantConditions
        if (entity.hasVehicle() && entity.getVehicle().isLiving()) {
            LivingEntity vehicle = (LivingEntity)entity.getVehicle();
            bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, vehicle.prevBodyYaw, vehicle.bodyYaw);
            float relHeadYaw = absHeadYaw - bodyYaw;
            float wrappedHeadYaw = MathHelper.clamp(MathHelper.wrapDegrees(relHeadYaw), -85.0F, 85.0F);

            bodyYaw = absHeadYaw - wrappedHeadYaw;
            if (wrappedHeadYaw > 50F) {
                bodyYaw += wrappedHeadYaw * 0.2F;
            }
        }



        if (entity.getPose() == EntityPose.SLEEPING) {
            Direction direction = entity.getSleepingDirection();
            if (direction != null) {
                float height = entity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                matrices.translate((float)(-direction.getOffsetX()) * height, 0.0D, (float)(-direction.getOffsetZ()) * height);
            }
        }
        float animationProgress = ((EntityRendererAccessor) normalRenderer).vanillaAnimationProgress(entity, tickDelta);
        ((EntityRendererAccessor) normalRenderer).setupVanillaTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
        matrices.scale(-1.0F, -1.0F, 1.0F);
        ((EntityRendererAccessor) normalRenderer).vanillaScale(entity, matrices, tickDelta);
        matrices.translate(0.0D, -1.5010000467300415D, 0.0D);

        float limbAngle = 0;
        float limbDistance = 0;
        if (!entity.hasVehicle() && entity.isAlive()) {
            limbDistance = MathHelper.lerp(tickDelta, entity.lastLimbDistance, entity.limbDistance);
            limbAngle = entity.limbAngle - entity.limbDistance * (1.0F - tickDelta);
            if (entity.isBaby()) {
                limbAngle *= 3.0F;
            }

            if (limbDistance > 1.0F) {
                limbDistance = 1.0F;
            }
        }

        this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, absHeadYaw - bodyYaw, MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch));

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean visible = !entity.isInvisible();
        boolean translucentTo = !(visible || entity.isInvisibleTo(minecraftClient.player));
        RenderLayer renderLayer = this.getRenderLayer(entity, visible, translucentTo, minecraftClient.hasOutline(entity));
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
            this.model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, ((EntityRendererAccessor) normalRenderer).vanillaAnimationCounter(entity, tickDelta)), 1.0F, 1.0F, 1.0F, translucentTo ? 0.15F : 1.0F);
        }

        if (!entity.isSpectator()) {
            for (FeatureRenderer<T, CustomModel<T>> feature : model.features) {
                feature.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, absHeadYaw - bodyYaw, MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch));
            }
        }

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Nullable
    protected RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = this.getTexture(entity);
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        } else if (showBody) {
            return this.model.getLayer(identifier);
        } else {
            return showOutline ? RenderLayer.getOutline(identifier) : null;
        }
    }

    public EntityRenderer<T> getNormalRenderer() {
        return normalRenderer;
    }
}
