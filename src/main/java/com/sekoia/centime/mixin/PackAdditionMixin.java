package com.sekoia.centime.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sekoia.centime.EntityTypeModelMap;
import com.sekoia.centime.model.feature.FeaturePacketDeserializer;
import com.sekoia.centime.model.feature.FeaturePacket;
import com.sekoia.centime.raw.RawModel;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Mixin(ReloadableResourceManagerImpl.class)
public class PackAdditionMixin {
    private static final Gson GSON;
    static {
         GSON = new GsonBuilder().setLenient().registerTypeAdapter(FeaturePacket.class, new FeaturePacketDeserializer()).create();
    }

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "addPack", at = @At("TAIL"))
    private void addModels(ResourcePack resourcePack, CallbackInfo ci) {
        for (Identifier id : resourcePack.findResources(ResourceType.CLIENT_RESOURCES, "minecraft", "models", 1, s -> true)) {
            if (!id.toString().endsWith(".cem")) {
                continue;
            }
            LOGGER.info("Loading: {}", id);
            InputStream input;
            try {
                input = resourcePack.open(ResourceType.CLIENT_RESOURCES, id);
            } catch (IOException e) {
                LOGGER.error("Error loading {}. Please report this to the tracker including the resource pack you used.", id);
                continue;
            }
            Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            RawModel model = GSON.fromJson(reader, RawModel.class);
            Identifier fixed_identifier = new Identifier(id.getNamespace(), id.getPath().substring(7, id.getPath().length()-4));
            EntityTypeModelMap.addEntry(fixed_identifier, model.createCustomModel());
        }
    }
}
