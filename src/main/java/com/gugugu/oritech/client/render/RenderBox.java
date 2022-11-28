package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.util.math.Direction;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class RenderBox {
    public Vector3f min;
    public Vector3f max;
    public static class UVGroup {
        public Vector2d uv_min;
        public Vector2d uv_max;
    }
    public Map<Direction, UVGroup> uvGroups;

    public RenderBox(Vector3f min, Vector3f max, Map<Direction, UVGroup> uvGroups) {
        this.min = min;
        this.max = max;
        this.uvGroups = uvGroups;
    }

    public void renderFace(Batch batch, Direction face, int x, int y, int z, Vector2d uv0, Vector2d uv1, TextureAtlas atlas, int granularity) {
        float x0 = min.x + (float) x;
        float y0 = min.y + (float) y;
        float z0 = min.z + (float) z;
        float x1 = max.x + (float) x;
        float y1 = max.y + (float) y;
        float z1 = max.z + (float) z;

        float u_len = (float) (uv1.x - uv0.x);
        float v_len = (float) (uv1.y - uv0.y);
        float scale_u = u_len / granularity;
        float scale_v = v_len / granularity;
        UVGroup group = uvGroups.get(face);
        float u0 = (float) ((uv0.x + group.uv_min.x * scale_u) / atlas.width());
        float v0 = (float) ((uv0.y + group.uv_min.y * scale_v) / atlas.height());
        float u1 = (float) ((uv0.x + group.uv_max.x * scale_u) / atlas.width());
        float v1 = (float) ((uv0.y + group.uv_max.y * scale_v) / atlas.height());

        switch (face) {
            case WEST -> batch.quadIndices()
                .texCoords(u0, v0).vertex(x0, y1, z0)
                .texCoords(u0, v1).vertex(x0, y0, z0)
                .texCoords(u1, v1).vertex(x0, y0, z1)
                .texCoords(u1, v0).vertex(x0, y1, z1);
            case EAST -> batch.quadIndices()
                .texCoords(u0, v0).vertex(x1, y1, z1)
                .texCoords(u0, v1).vertex(x1, y0, z1)
                .texCoords(u1, v1).vertex(x1, y0, z0)
                .texCoords(u1, v0).vertex(x1, y1, z0);
            case DOWN -> batch.quadIndices()
                .texCoords(u0, v0).vertex(x0, y0, z1)
                .texCoords(u0, v1).vertex(x0, y0, z0)
                .texCoords(u1, v1).vertex(x1, y0, z0)
                .texCoords(u1, v0).vertex(x1, y0, z1);
            case UP -> batch.quadIndices()
                .texCoords(u0, v0).vertex(x0, y1, z0)
                .texCoords(u0, v1).vertex(x0, y1, z1)
                .texCoords(u1, v1).vertex(x1, y1, z1)
                .texCoords(u1, v0).vertex(x1, y1, z0);
            case NORTH -> batch.quadIndices()
                .texCoords(u0, v0).vertex(x1, y1, z0)
                .texCoords(u0, v1).vertex(x1, y0, z0)
                .texCoords(u1, v1).vertex(x0, y0, z0)
                .texCoords(u1, v0).vertex(x0, y1, z0);
            case SOUTH -> batch.quadIndices()
                .texCoords(u0, v0).vertex(x0, y1, z1)
                .texCoords(u0, v1).vertex(x0, y0, z1)
                .texCoords(u1, v1).vertex(x1, y0, z1)
                .texCoords(u1, v0).vertex(x1, y1, z1);
        }
    }

    public static class Builder {
        public Vector3f min;
        public Vector3f max;
        public Map<Direction, RenderBox.UVGroup> uvGroups = new HashMap<>();

        public Builder() {
        }

        public Builder min(Vector3f min) {
            this.min = min;
            return this;
        }

        public Builder max(Vector3f max) {
            this.max = max;
            return this;
        }

        public Builder addUVGroup(Direction dir, UVGroup uvGroup) {
            this.uvGroups.put(dir, uvGroup);
            return this;
        }

        public Builder allGroup(UVGroup uvGroup) {
            return this
                .addUVGroup(Direction.UP, uvGroup)
                .addUVGroup(Direction.DOWN, uvGroup)
                .addUVGroup(Direction.WEST, uvGroup)
                .addUVGroup(Direction.EAST, uvGroup)
                .addUVGroup(Direction.NORTH, uvGroup)
                .addUVGroup(Direction.SOUTH, uvGroup);
        }

        public RenderBox build() {
            return new RenderBox(min, max, uvGroups);
        }
    }
}
