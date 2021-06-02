package com.sekoia.centime.model.feature;

import com.google.gson.JsonObject;
import com.sekoia.centime.CentimeInit;
import com.sekoia.centime.model.CustomModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FeatureBuilders {
    public static void registerFeatureBuilders() {
        register("emissive", (JsonObject json, CustomModel<?> model) -> new EmissiveFeatureRenderer<>(new Identifier(json.get("texture").getAsString()), model));
    }

    private static void register(String name, FeatureBuilder builder) {
        Registry.register(CentimeInit.FEATURE_BUILDER, name, builder);
    }
}
