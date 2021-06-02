package com.sekoia.centime.mixin;

import com.sekoia.centime.EntityTypeModelMap;
import com.sekoia.centime.ModelRendererRebuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public class ResourceReloadMixin {
    @Inject(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;createResourcePacks()Ljava/util/List;", shift = At.Shift.BEFORE))
    private void clearCustomModels(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        System.out.println("Clearing model map");
        EntityTypeModelMap.clearEntries();
    }

    @Inject(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V"))
    private void reloadRenderDispatcher(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        System.out.println("Reloading models");
        ((ModelRendererRebuilder) MinecraftClient.getInstance().getEntityRenderDispatcher()).rebuild();
    }

    // Because Minecraft doesn't actually call reloadResources at the start, we need to do this.
    @Inject(method = "<init>", at = @At("TAIL"))
    private void loadRenderDispatcher(RunArgs args, CallbackInfo ci) {
        ((ModelRendererRebuilder) MinecraftClient.getInstance().getEntityRenderDispatcher()).rebuild();

    }
}
