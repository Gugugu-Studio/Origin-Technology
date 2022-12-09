package com.gugugu.oritech.resource;

import com.google.gson.*;
import com.gugugu.oritech.client.model.Model;
import com.gugugu.oritech.client.model.RenderBox;
import com.gugugu.oritech.util.math.Direction;
import org.joml.Vector2d;
import org.joml.Vector3f;

public class ModelLoader {
    private static ResLocation parseTextureRL(String str) {
        ResLocation location = new ResLocation(ResType.ASSETS, str);
        location.setPath("textures/" + location.getPath());
        return location;
    }

    private static void parseTextures(Model.Builder model, JsonObject obj) {
        model.granularity(obj.getAsJsonPrimitive("granularity").getAsInt());
        if (obj.has("all")) {
            model.allTextures(parseTextureRL(obj.getAsJsonPrimitive("all").getAsString()));
            return;
        }
        model.addTextures(Direction.UP,
            parseTextureRL(obj.getAsJsonPrimitive("up").getAsString()));
        model.addTextures(Direction.DOWN,
            parseTextureRL(obj.getAsJsonPrimitive("down").getAsString()));
        model.addTextures(Direction.WEST,
            parseTextureRL(obj.getAsJsonPrimitive("west").getAsString()));
        model.addTextures(Direction.EAST,
            parseTextureRL(obj.getAsJsonPrimitive("east").getAsString()));
        model.addTextures(Direction.NORTH,
            parseTextureRL(obj.getAsJsonPrimitive("north").getAsString()));
        model.addTextures(Direction.SOUTH,
            parseTextureRL(obj.getAsJsonPrimitive("south").getAsString()));
    }

    private static RenderBox.UVGroup parseUV(JsonArray uv) {
        RenderBox.UVGroup uvGroup = new RenderBox.UVGroup();
        JsonArray j_min = uv.get(0).getAsJsonArray();
        JsonArray j_max = uv.get(1).getAsJsonArray();
        uvGroup.uv_min = new Vector2d(j_min.get(0).getAsDouble(), j_min.get(1).getAsDouble());
        uvGroup.uv_max = new Vector2d(j_max.get(0).getAsDouble(), j_max.get(1).getAsDouble());
        return uvGroup;
    }

    private static void parseUV(RenderBox.Builder box, JsonObject obj) {
        if (obj.has("all")) {
            box.allGroup(parseUV(obj.getAsJsonArray("all")));
            return;
        }
        box.addUVGroup(Direction.UP,
            parseUV(obj.getAsJsonArray("up")));
        box.addUVGroup(Direction.DOWN,
            parseUV(obj.getAsJsonArray("down")));
        box.addUVGroup(Direction.WEST,
            parseUV(obj.getAsJsonArray("west")));
        box.addUVGroup(Direction.EAST,
            parseUV(obj.getAsJsonArray("east")));
        box.addUVGroup(Direction.NORTH,
            parseUV(obj.getAsJsonArray("north")));
        box.addUVGroup(Direction.SOUTH,
            parseUV(obj.getAsJsonArray("south")));

    }

    private static void parseCube(Model.Builder model, JsonObject obj) {
        RenderBox.Builder box = new RenderBox.Builder();
        JsonArray j_vertex = obj.getAsJsonArray("vertex");
        JsonArray j_min = j_vertex.get(0).getAsJsonArray();
        JsonArray j_max = j_vertex.get(1).getAsJsonArray();
        Vector3f min = new Vector3f(
            j_min.get(0).getAsFloat(),
            j_min.get(1).getAsFloat(),
            j_min.get(2).getAsFloat()
        );
        Vector3f max = new Vector3f(
            j_max.get(0).getAsFloat(),
            j_max.get(1).getAsFloat(),
            j_max.get(2).getAsFloat()
        );
        box.min(min);
        box.max(max);
        parseUV(box, obj.getAsJsonObject("uv"));
        model.addBox(box.build());
    }

    private static void parseCubes(Model.Builder model, JsonArray arr) {
        for (JsonElement ele : arr) {
            parseCube(model, ele.getAsJsonObject());
        }
    }

    private static Model parseModel(JsonObject obj) {
        Model.Builder model = new Model.Builder();
        parseTextures(model, obj.getAsJsonObject("textures"));
        parseCubes(model, obj.getAsJsonArray("cubes"));
        return model.build();
    }

    public static Model loadModel(ResLocation location) {
        String data = ResourceLoader.loadText(location);
        JsonObject object = JsonParser.parseString(data).getAsJsonObject();
        return parseModel(object);
    }
}
