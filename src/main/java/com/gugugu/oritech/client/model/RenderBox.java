package com.gugugu.oritech.client.model;

import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import org.joml.Vector2d;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
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

    public void renderFace(Tesselator t, Direction face,
                           double u0, double v0,
                           double u1, double v1,
                           TextureAtlas atlas, int granularity) {
        float x0 = min.x;
        float y0 = min.y;
        float z0 = min.z;
        float x1 = max.x;
        float y1 = max.y;
        float z1 = max.z;

        float u_len = (float) (u1 - u0);
        float v_len = (float) (v1 - v0);
        float scale_u = u_len / granularity;
        float scale_v = v_len / granularity;
        UVGroup group = uvGroups.get(face);
        float fu0 = (float) ((u0 + group.uv_min.x * scale_u) / atlas.width());
        float fv0 = (float) ((v0 + group.uv_min.y * scale_v) / atlas.height());
        float fu1 = (float) ((u0 + group.uv_max.x * scale_u) / atlas.width());
        float fv1 = (float) ((v0 + group.uv_max.y * scale_v) / atlas.height());

        switch (face) {
            case WEST -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(fu0, fv0).normal(-1, 0, 0).vertex(x0, y1, z0).emit();
                t.texCoord(fu0, fv1).normal(-1, 0, 0).vertex(x0, y0, z0).emit();
                t.texCoord(fu1, fv1).normal(-1, 0, 0).vertex(x0, y0, z1).emit();
                t.texCoord(fu1, fv0).normal(-1, 0, 0).vertex(x0, y1, z1).emit();
            }
            case EAST -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(fu0, fv0).normal(1, 0, 0).vertex(x1, y1, z1).emit();
                t.texCoord(fu0, fv1).normal(1, 0, 0).vertex(x1, y0, z1).emit();
                t.texCoord(fu1, fv1).normal(1, 0, 0).vertex(x1, y0, z0).emit();
                t.texCoord(fu1, fv0).normal(1, 0, 0).vertex(x1, y1, z0).emit();
            }
            case DOWN -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(fu0, fv0).normal(0, -1, 0).vertex(x0, y0, z1).emit();
                t.texCoord(fu0, fv1).normal(0, -1, 0).vertex(x0, y0, z0).emit();
                t.texCoord(fu1, fv1).normal(0, -1, 0).vertex(x1, y0, z0).emit();
                t.texCoord(fu1, fv0).normal(0, -1, 0).vertex(x1, y0, z1).emit();
            }
            case UP -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(fu0, fv0).normal(0, 1, 0).vertex(x0, y1, z0).emit();
                t.texCoord(fu0, fv1).normal(0, 1, 0).vertex(x0, y1, z1).emit();
                t.texCoord(fu1, fv1).normal(0, 1, 0).vertex(x1, y1, z1).emit();
                t.texCoord(fu1, fv0).normal(0, 1, 0).vertex(x1, y1, z0).emit();
            }
            case NORTH -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(fu0, fv0).normal(0, 0, 1).vertex(x1, y1, z0).emit();
                t.texCoord(fu0, fv1).normal(0, 0, 1).vertex(x1, y0, z0).emit();
                t.texCoord(fu1, fv1).normal(0, 0, 1).vertex(x0, y0, z0).emit();
                t.texCoord(fu1, fv0).normal(0, 0, 1).vertex(x0, y1, z0).emit();
            }
            case SOUTH -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(fu0, fv0).normal(0, 0, -1).vertex(x0, y1, z1).emit();
                t.texCoord(fu0, fv1).normal(0, 0, -1).vertex(x0, y0, z1).emit();
                t.texCoord(fu1, fv1).normal(0, 0, -1).vertex(x1, y0, z1).emit();
                t.texCoord(fu1, fv0).normal(0, 0, -1).vertex(x1, y1, z1).emit();
            }
        }
    }

    public static class Builder {
        public Vector3f min;
        public Vector3f max;
        public final Map<Direction, RenderBox.UVGroup> uvGroups = new HashMap<>();

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
