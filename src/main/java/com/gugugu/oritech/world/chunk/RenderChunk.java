package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.block.BlockState;
import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.client.render.Frustum;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.block.Block;
import com.gugugu.oritech.entity.PlayerEntity;
import com.gugugu.oritech.world.World;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class RenderChunk extends Chunk implements AutoCloseable {
    private final ClientWorld world;
    private final int x0, y0, z0;
    private final int x1, y1, z1;
    private final float x, y, z;
    public final int chunkX, chunkY, chunkZ;
    private final int width, height, depth;
    public Batch batch = new Batch(CHUNK_SIZE * CHUNK_SIZE);
    private boolean isDirty = true;
    private boolean hasRendered = false;
    public double dirtiedTime = 0.0;

    public RenderChunk(ClientWorld world,
                       LogicChunk logicChunk) {
        this.world = world;
        x0 = logicChunk.x0;
        y0 = logicChunk.y0;
        z0 = logicChunk.z0;
        x1 = logicChunk.x1;
        y1 = logicChunk.y1;
        z1 = logicChunk.z1;
        x = (x0 + x1) * 0.5f;
        y = (y0 + y1) * 0.5f;
        z = (z0 + z1) * 0.5f;
        chunkX = logicChunk.chunkX;
        chunkY = logicChunk.chunkY;
        chunkZ = logicChunk.chunkZ;
        width = logicChunk.width;
        height = logicChunk.height;
        depth = logicChunk.depth;
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return OriTechClient.getServer().world
            .getChunk(chunkX, chunkY, chunkZ)
            .getBlock(x, y, z);
    }

    @Override
    public void setBlock(BlockState block, int x, int y, int z) {
        OriTechClient.getServer().world
            .getChunk(chunkX, chunkY, chunkZ)
            .setBlock(block, x, y, z);
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
        //int blocks = 0;
        boolean rendered = false;
        batch.begin();
        batch.matrix.clear();
        batch.matrix.identity();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    BlockState block = getBlock(x, y, z);
                    if (!block.isAir()) {
                        batch.matrix.pushMatrix();
                        batch.matrix.translate(
                            getAbsolutePos(chunkX, x),
                            getAbsolutePos(chunkY, y),
                            getAbsolutePos(chunkZ, z)
                        );
                        boolean b = block.getBlock().render(batch, world,
                            getAbsolutePos(this.chunkX, x),
                            getAbsolutePos(this.chunkY, y),
                            getAbsolutePos(this.chunkZ, z));
                        if (b) {
                            rendered = true;
                        }
                        batch.matrix.popMatrix();
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
        if (hasRendered && batch != null && batch.uploaded() && testFrustum()) {
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

    @Override
    public World getWorld() {
        return world;
    }
}
