package com.onenow.hedgefund.nosqlrest;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;
import com.onenow.hedgefund.nosqlclient.DynamoResource;

public class DynamoResourceSerializer implements JsonSerializer<DynamoResource> {
    @Override
    public JsonElement serialize(final DynamoResource value, final Type type,
                                 final JsonSerializationContext context) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(
                JsonElement.class,
                new JsonDeserializer<JsonElement>() {
                    @Override
                    public JsonElement deserialize(JsonElement value,
                                                   Type type,
                                                   JsonDeserializationContext context)
                            throws JsonParseException {
                        return value;
                    }
                });

        Gson gson = gsonBuilder.create();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("item", gson.fromJson(value.item, JsonElement.class));
        jsonObject.add("href", context.serialize(value.href));
        jsonObject.add("URL", context.serialize(value.URL));
        return jsonObject;
    }
}
