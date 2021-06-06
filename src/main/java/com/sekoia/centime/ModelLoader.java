package com.sekoia.centime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sekoia.centime.model.feature.FeaturePacket;
import com.sekoia.centime.model.feature.FeaturePacketDeserializer;
import com.sekoia.centime.raw.RawModel;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ModelLoader implements SimpleResourceReloadListener<Void> {
    private static final Gson GSON;
    static {
         GSON = new GsonBuilder().setLenient().registerTypeAdapter(FeaturePacket.class, new FeaturePacketDeserializer()).create();
    }

    public void addModels(ResourceManager manager) {
        manager.streamResourcePacks().forEach((ResourcePack resourcePack) -> {
            for (String namespace : resourcePack.getNamespaces(ResourceType.CLIENT_RESOURCES)) {
                for (Identifier id : resourcePack.findResources(ResourceType.CLIENT_RESOURCES, namespace, "models", 1,s -> s.endsWith(".cem") || s.endsWith(".ecem"))) {
                    if (id.getPath().endsWith(".cem")) {
                        addSimpleModel(resourcePack, id);
                    } else {
                        addElaborateModel(resourcePack, id);
                    }
                }
            }
        });
    }

    private void addElaborateModel(ResourcePack resourcePack, Identifier id) {
        Centime.LOGGER.info("Loading elaborate model: {}", id);
    }

    private void addSimpleModel(ResourcePack resourcePack, Identifier id) {
        Centime.LOGGER.info("Loading: {}", id);
        InputStream input;
        try {
            input = resourcePack.open(ResourceType.CLIENT_RESOURCES, id);
        } catch (IOException e) {
            Centime.LOGGER.error("Error loading {}: ", id);
            return;
        }
        Reader reader = new InputStreamReader(input);
        RawModel model = GSON.fromJson(reader, RawModel.class);
        Identifier fixed_identifier = new Identifier(id.getNamespace(), id.getPath().substring(7, id.getPath().length()-4));
        EntityTypeModelMap.addEntry(fixed_identifier, model.createCustomModel());
    }

    @Override
    public CompletableFuture<Void> load(ResourceManager manager, Profiler profiler, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            EntityTypeModelMap.clearEntries();
            addModels(manager);
        }, executor);
    }

    @Override
    public CompletableFuture<Void> apply(Void data, ResourceManager manager, Profiler profiler, Executor executor) {
        return CompletableFuture.runAsync(((ModelRendererRebuilder) MinecraftClient.getInstance().getEntityRenderDispatcher())::rebuild, executor);
    }

    public static final Identifier ID = new Identifier("centime", "cem");
    @Override
    public Identifier getFabricId() {
        return ID;
    }
}
