package com.sekoia.centime.model.feature;

import com.google.gson.*;
import com.sekoia.centime.Centime;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class FeaturePacketDeserializer implements JsonDeserializer<FeaturePacket> {
    @Override
    public FeaturePacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObject = (JsonObject) json;
        JsonElement typeObj = jObject.get("type");

        if (typeObj != null){
            return new FeaturePacket(Centime.FEATURE_BUILDER.get(new Identifier(typeObj.getAsString())), jObject);
        }
        return null;
    }
}
