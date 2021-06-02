package com.sekoia.centime.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface EntityRendererAccessor {
    @Invoker("getAnimationProgress")
    float vanillaAnimationProgress(LivingEntity entity, float tickDelta);

    @Invoker("setupTransforms")
    void setupVanillaTransforms(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);

    @Invoker("getAnimationCounter")
    float vanillaAnimationCounter(LivingEntity entity, float tickDelta);

    @Invoker("scale")
    void vanillaScale(LivingEntity entity, MatrixStack matrices, float tickDelta);
}
