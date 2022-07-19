package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.renderer.Batch;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.block.Block;

/**
 * @author squid233
 * @since 1.0
 */
public class RenderChunk extends Chunk implements AutoCloseable {
    private final ClientWorld world;
    private final int x0, y0, z0;
    private final int x1, y1, z1;
    public Batch batch;
    private boolean isDirty = true;

    public RenderChunk(ClientWorld world) {
        this.world = world;
        x0 = 0;
        y0 = 0;
        z0 = 0;
        x1 = CHUNK_SIZE;
        y1 = CHUNK_SIZE;
        z1 = CHUNK_SIZE;
    }

    public void rebuild() {
        if (!isDirty)
            return;
        if (batch == null) {
            batch = new Batch();
        }
        batch.begin();
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                for (int z = z0; z < z1; z++) {
                    Block block = world.getBlock(x, y, z);
                    if (!block.isAir()) {
                        block.render(batch, world, x, y, z);
                    }
                }
            }
        }
        batch.end();
        isDirty = false;
    }

    public void render() {
        batch.render();
    }

    @Override
    public void close() {
        batch.free();
    }
}
