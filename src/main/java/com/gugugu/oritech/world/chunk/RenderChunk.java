package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.client.render.Frustum;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.entity.PlayerEntity;

/**
 * @author squid233
 * @since 1.0
 */
public class RenderChunk extends Chunk implements AutoCloseable {
    private final ClientWorld world;
    private final int x0, y0, z0;
    private final int x1, y1, z1;
    private final float x, y, z;
    public Batch batch;
    private boolean isDirty = true;
    private boolean hasBlock = false;
    public double dirtiedTime = 0.0;

    public RenderChunk(ClientWorld world,
                       int x0, int y0, int z0,
                       int x1, int y1, int z1) {
        this.world = world;
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        x = (x0 + x1) * 0.5f;
        y = (y0 + y1) * 0.5f;
        z = (z0 + z1) * 0.5f;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void markDirty() {
        if (!isDirty) {
            dirtiedTime = Timer.getTime();
        }
        isDirty = true;
    }

    public void rebuild() {
        if (batch == null) {
            batch = new Batch();
        }
        int blocks = 0;
        batch.begin();
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                for (int z = z0; z < z1; z++) {
                    Block block = world.getBlock(x, y, z);
                    if (!block.isAir()) {
                        block.render(batch, world, x, y, z);
                        ++blocks;
                    }
                }
            }
        }
        if (blocks > 0) {
            hasBlock = true;
        }
        batch.end();
        isDirty = false;
    }

    public boolean testFrustum() {
        return Frustum.test(x0, y0, z0, x1, y1, z1);
    }

    public void render() {
        if (hasBlock && batch != null && testFrustum()) {
            batch.render();
        }
    }

    @Override
    public float distanceSqr(PlayerEntity player) {
        return player.position.distanceSquared(x, y, z);
    }

    @Override
    public void close() {
        if (batch != null) {
            batch.free();
        }
    }
}
