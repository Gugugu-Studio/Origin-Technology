package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.util.math.Direction;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class RenderBox {
    private Vector3f min;
    private Vector3f max;
    private Vector2d uv_min;
    private Vector2d uv_len;

    public RenderBox(Vector3f min, Vector3f max, Vector2d uv_min, Vector2d uv_len) {
        this.min = min;
        this.max = max;
        this.uv_min = uv_min;
        this.uv_len = uv_len;
    }

    public void renderFace(Batch batch, Direction face, int x, int y, int z, Vector2d uv0, Vector2d uv1, TextureAtlas atlas) {
        float x0 = min.x + (float) x;
        float y0 = min.y + (float) y;
        float z0 = min.z + (float) z;
        float x1 = max.x + (float) x;
        float y1 = max.y + (float) y;
        float z1 = max.z + (float) z;

        float u0 = (float) ((uv0.x + uv_min.x) / atlas.width());
        float v0 = (float) ((uv0.y + uv_min.y) / atlas.height());
        float u1 = (float) ((uv0.x + uv_min.x + uv_len.x) / atlas.width());
        float v1 = (float) ((uv0.y + uv_min.y + uv_len.y) / atlas.height());

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
}
