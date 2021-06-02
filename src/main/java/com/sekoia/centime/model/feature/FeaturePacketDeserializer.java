package com.sekoia.centime.model.feature;

import com.google.gson.*;
import com.sekoia.centime.CentimeInit;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class FeaturePacketDeserializer implements JsonDeserializer<FeaturePacket> {
    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public FeaturePacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObject = (JsonObject) json;
        JsonElement typeObj = jObject.get("type");

        if (typeObj != null){
            return new FeaturePacket(CentimeInit.FEATURE_BUILDER.get(new Identifier(typeObj.getAsString())), jObject);
        }
        return null;
    }
}
