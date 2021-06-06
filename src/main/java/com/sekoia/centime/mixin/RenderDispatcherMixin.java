package com.sekoia.centime.mixin;

import com.sekoia.centime.*;
import com.sekoia.centime.model.CustomModelRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class RenderDispatcherMixin implements ModelRendererRebuilder {

    @Shadow @Final private Map<EntityType<?>, EntityRenderer<?>> renderers;

    public RenderDispatcherMixin() {
    }

    @SuppressWarnings("rawtypes")
    @Inject(method = "register", at = @At("TAIL"))
    // This *probably* isn't actually used, because resource packs load later. But it's here just for safety
    private <T extends Entity> void modelOverride(EntityType<T> entityType, EntityRenderer<? super T> entityRenderer, CallbackInfo ci) {
        if (!(entityRenderer instanceof LivingEntityRenderer)) {
            if (EntityTypeModelMap.isModelOverridden(entityType)) {
                Centime.LOGGER.error(String.format("Attempted to do CEM on non-living entity type %s, but this is not allowed!", Registry.ENTITY_TYPE.getId(entityType)));
            }
            return;
        }
        if (EntityTypeModelMap.isModelOverridden(entityType)) {
            this.renderers.put(entityType, new CustomModelRenderer((EntityRenderDispatcher) (Object) this, EntityTypeModelMap.getModel(entityType), entityRenderer));
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void rebuild() {
        for (EntityType<?> type : this.renderers.keySet()) {
            if (!(this.renderers.get(type) instanceof LivingEntityRenderer || this.renderers.get(type) instanceof CustomModelRenderer)) {
                if (EntityTypeModelMap.isModelOverridden(type)) {
                    Centime.LOGGER.error(String.format("Attempted to do CEM on non-living entity type %s, but this is not allowed!", Registry.ENTITY_TYPE.getId(type)));
                }
                continue;
            }
            EntityRenderer<?> normalRenderer;
            if (this.renderers.get(type) instanceof CustomModelRenderer) {
                normalRenderer = ((CustomModelRenderer) this.renderers.get(type)).getNormalRenderer();
            } else {
                normalRenderer = this.renderers.get(type);
            }
            if (EntityTypeModelMap.isModelOverridden(type)) {
                this.renderers.put(type, new CustomModelRenderer((EntityRenderDispatcher) (Object) this, EntityTypeModelMap.getModel(type), normalRenderer));
            } else {
                this.renderers.put(type, normalRenderer);
            }
        }
    }
}
