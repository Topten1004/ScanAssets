package com.example.ScanAndGo.json;

import com.example.ScanAndGo.dto.AssetsItem;
import com.example.ScanAndGo.dto.CheckBarCode;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckBarCodeDeserializer implements JsonDeserializer<CheckBarCode> {
    @Override
    public CheckBarCode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        CheckBarCode checkBarCode = new CheckBarCode();

        // Handle wrongList
        JsonElement wrongListElement = jsonObject.get("wrongList");
        if (wrongListElement != null && wrongListElement.isJsonArray()) {
            checkBarCode.wrongList = context.deserialize(wrongListElement, new TypeToken<List<AssetsItem>>(){}.getType());
        } else {
            checkBarCode.wrongList = new ArrayList<>();  // Default to empty list if it's not an array
        }

        // Handle missingList
        JsonElement missingListElement = jsonObject.get("missingList");
        if (missingListElement != null && missingListElement.isJsonArray()) {
            checkBarCode.missingList = context.deserialize(missingListElement, new TypeToken<List<AssetsItem>>(){}.getType());
        } else {
            checkBarCode.missingList = new ArrayList<>();  // Default to empty list if it's not an array
        }

        return checkBarCode;
    }
}
