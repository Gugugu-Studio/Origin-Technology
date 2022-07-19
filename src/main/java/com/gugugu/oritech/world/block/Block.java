package com.gugugu.oritech.world.block;

import com.gugugu.oritech.renderer.Batch;
import com.gugugu.oritech.util.math.Direction;
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
        batch.color(1.0f, 1.0f, 1.0f);
        switch (face) {
            case WEST -> batch.quadIndices()
                .vertex(x0, y1, z1)
                .vertex(x0, y0, z1)
                .vertex(x0, y0, z0)
                .vertex(x0, y1, z0);
            case EAST -> batch.quadIndices()
                .vertex(x1, y1, z0)
                .vertex(x1, y0, z0)
                .vertex(x1, y0, z1)
                .vertex(x1, y1, z1);
            case DOWN -> batch.quadIndices()
                .vertex(x0, y0, z1)
                .vertex(x0, y0, z0)
                .vertex(x1, y0, z0)
                .vertex(x1, y0, z1);
            case UP -> batch.quadIndices()
                .vertex(x0, y1, z0)
                .vertex(x0, y1, z1)
                .vertex(x1, y1, z1)
                .vertex(x1, y1, z0);
            case NORTH -> batch.quadIndices()
                .vertex(x1, y1, z0)
                .vertex(x1, y0, z0)
                .vertex(x0, y0, z0)
                .vertex(x0, y1, z0);
            case SOUTH -> batch.quadIndices()
                .vertex(x0, y1, z1)
                .vertex(x0, y0, z1)
                .vertex(x1, y0, z1)
                .vertex(x1, y1, z1);
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
