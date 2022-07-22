package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.client.render.Frustum;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
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
    public final int chunkX, chunkY, chunkZ;
    private final Block[][][] blocks;
    private final int width, height, depth;
    public Batch batch;
    private boolean isDirty = true;
    private boolean hasRendered = false;
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
        chunkX = getChunkPos(x0);
        chunkY = getChunkPos(y0);
        chunkZ = getChunkPos(z0);
        width = x1 - x0;
        height = y1 - y0;
        depth = z1 - z0;
        blocks = new Block[height][width][depth];
        int layer;
        for (int y = 0; y < height; y++) {
            layer = getAbsolutePos(chunkY, y);
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    if (layer < 0) {
                        blocks[y][x][z] = Blocks.STONE;
                    } else if (layer < 4) {
                        blocks[y][x][z] = Blocks.DIRT;
                    } else if (layer == 4) {
                        blocks[y][x][z] = Blocks.GRASS_BLOCK;
                    } else {
                        blocks[y][x][z] = Blocks.AIR;
                    }
                }
            }
        }
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return blocks[y][x][z];
    }

    @Override
    public void setBlock(Block block, int x, int y, int z) {
        blocks[y][x][z] = block;
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
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
        //int blocks = 0;
        boolean rendered = false;
        batch.begin();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    Block block = getBlock(x, y, z);
                    if (!block.isAir()) {
                        boolean b = block.render(batch,
                            world,
                            getAbsolutePos(chunkX, x),
                            getAbsolutePos(chunkY, y),
                            getAbsolutePos(chunkZ, z));
                        if (b) {
                            rendered = true;
                        }
                        //++blocks;
                    }
                }
            }
        }
        hasRendered = rendered;
        //if (blocks > 0) {
        //    hasBlock = true;
        //}
        batch.end();
        isDirty = false;
    }

    public boolean testFrustum() {
        return Frustum.test(x0, y0, z0, x1, y1, z1);
    }

    public void render() {
        if (hasRendered && batch != null && testFrustum()) {
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
