package com.gugugu.oritech.world.block;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.renderer.Batch;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.World;

/**
 * @author squid233
 * @since 1.0
 */
public class Block {
    public boolean isAir() {
        return false;
    }

    public boolean hasSideTransparency() {
        return false;
    }

    public boolean shouldRenderFace(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).hasSideTransparency();
    }

    public void renderFace(Batch batch, Direction face, int x, int y, int z) {
        float x0 = (float) x;
        float y0 = (float) y;
        float z0 = (float) z;
        float x1 = x0 + 1.0f;
        float y1 = y0 + 1.0f;
        float z1 = z0 + 1.0f;

        TextureAtlas atlas = OriTechClient.getClient().blockAtlas;
        Identifier id = Registry.BLOCK.getId(this);
        String texName = ResLocation.ofTexture(id.namespace(), "block/" + id.path()).toString();
        float u0 = atlas.getU0n(texName);
        float v0 = atlas.getV0n(texName);
        float u1 = atlas.getU1n(texName);
        float v1 = atlas.getV1n(texName);

        batch.color(1.0f, 1.0f, 1.0f);
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

    public void render(Batch batch, World world, int x, int y, int z) {
        for (Direction face : Direction.values()) {
            if (shouldRenderFace(world,
                x + face.getOffsetX(),
                y + face.getOffsetY(),
                z + face.getOffsetZ())) {
                renderFace(batch, face, x, y, z);
            }
        }
    }
}
