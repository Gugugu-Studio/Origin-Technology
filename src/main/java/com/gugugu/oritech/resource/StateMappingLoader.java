package com.gugugu.oritech.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gugugu.oritech.client.model.BlockStateModels;
import com.gugugu.oritech.client.model.Model;
import com.gugugu.oritech.client.model.ModelOperators;
import com.gugugu.oritech.client.model.StateMapping;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateMappingLoader {
    private static ResLocation parseModelRL(String str) {
        ResLocation location = new ResLocation(ResType.ASSETS, str);
        location.setPath("models/block/" + location.getPath() + ".json");
        return location;
    }

    private static ModelOperators parseModelOperator(JsonObject jModel) {
        ModelOperators model = new ModelOperators();
        ResLocation modelRL = parseModelRL(jModel.get("model").getAsString());
        model.model = ModelLoader.loadModel(modelRL);
        model.scale = model.rotate = model.translate = null;
        if (jModel.has("rotate")) {
            JsonArray jVec = jModel.getAsJsonArray("rotate");
            model.rotate = new Vector3f(
                jVec.get(0).getAsFloat(),
                jVec.get(1).getAsFloat(),
                jVec.get(2).getAsFloat()
            );
        }
        if (jModel.has("translate")) {
            JsonArray jVec = jModel.getAsJsonArray("translate");
            model.translate = new Vector3f(
                jVec.get(0).getAsFloat(),
                jVec.get(1).getAsFloat(),
                jVec.get(2).getAsFloat()
            );
        }
        if (jModel.has("scale")) {
            JsonArray jVec = jModel.getAsJsonArray("scale");
            model.scale = new Vector3f(
                jVec.get(0).getAsFloat(),
                jVec.get(1).getAsFloat(),
                jVec.get(2).getAsFloat()
            );
        }
        return model;
    }

    private static List<ModelOperators> parseModels(JsonArray jModels) {
        List<ModelOperators> models = new ArrayList<>();
        for (JsonElement jModel : jModels) {
            models.add(parseModelOperator(jModel.getAsJsonObject()));
        }
        return models;
    }

    private static StateMapping parseStateMapping(JsonObject level) {
        String name = level.getAsJsonPrimitive("name").getAsString();
        StateMapping map = new StateMapping(name);
        Set<String> keys = level.keySet();
        keys.remove("name");
        for (String key : keys) {
            JsonElement ele = level.get(key);
            if (ele.isJsonObject()) {
                map.nextLevel.put(key, parseStateMapping(ele.getAsJsonObject()));
            }
            else {
                map.mapping.put(key, ele.getAsInt());
            }
        }
        return map;
    }

    private static StateMapping parseStateMappingRoot(JsonElement root) {
        StateMapping map = new StateMapping("__root__");
        if (root.isJsonObject()) {
            map.nextLevel.put("default", parseStateMapping(root.getAsJsonObject()));
        }
        else {
            map.mapping.put("default", root.getAsInt());
        }
        return map;
    }

    private static BlockStateModels parseBSModel(JsonObject jBSModel) {
        List<ModelOperators> models = parseModels(jBSModel.getAsJsonArray("models"));
        StateMapping mapping = parseStateMappingRoot(jBSModel.get("mapping"));
        return new BlockStateModels(models, mapping);
    }

    public static BlockStateModels loadBSModel(ResLocation location) {
        String data = ResourceLoader.loadText(location);
        JsonObject object = JsonParser.parseString(data).getAsJsonObject();
        return parseBSModel(object);
    }
}
