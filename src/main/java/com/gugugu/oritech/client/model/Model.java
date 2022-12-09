package com.gugugu.oritech.client.model;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class Model {
    public List<RenderBox> boxes;
    public Map<Direction, ResLocation> textures;
    public int granularity;

    public List<ResLocation> getAllTextures() {
        List<ResLocation> text = new ArrayList<>();
        for (ResLocation p : textures.values()) {
            if (! text.contains(p)) {
                text.add(p);
            }
        }
        return text;
    }

    public Model(List<RenderBox> boxes, Map<Direction, ResLocation> textures, int granularity) {
        this.boxes = boxes;
        this.textures = textures;
        this.granularity = granularity;
    }

    public void renderFace(Tesselator t, Direction face) {
        String texName = textures.get(face).toString();

        TextureAtlas atlas = OriTechClient.getClient().blockAtlas;
        Vector2d uv0 = new Vector2d(atlas.getU0(texName), atlas.getV0(texName));
        Vector2d uv1 = new Vector2d(atlas.getU1(texName), atlas.getV1(texName));

        for (RenderBox renderBox : boxes) {
            renderBox.renderFace(t, face, uv0, uv1, atlas, granularity);
        }
    }

    public static class Builder {
        public List<RenderBox> boxes = new ArrayList<>();
        public Map<Direction, ResLocation> textures = new HashMap<>();
        public int granularity;

        public Builder() {
        }

        public Builder addTextures(Direction dir, ResLocation texture) {
            textures.put(dir, texture);
            return this;
        }

        public Builder addBox(RenderBox box) {
            boxes.add(box);
            return this;
        }

        public Builder granularity(int granularity) {
            this.granularity = granularity;
            return this;
        }

        public Builder allTextures(ResLocation texture) {
            return this
                .addTextures(Direction.UP, texture)
                .addTextures(Direction.DOWN, texture)
                .addTextures(Direction.WEST, texture)
                .addTextures(Direction.EAST, texture)
                .addTextures(Direction.NORTH, texture)
                .addTextures(Direction.SOUTH, texture);
        }

        public Model build() {
            return new Model(boxes, textures, granularity);
        }
    }
}
