package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.gen.ChunkGens;
import com.gugugu.oritech.world.chunk.gen.SmoothChunkGen;
import com.gugugu.oritech.world.entity.PlayerEntity;

/**
 * @author squid233
 * @since 1.0
 */
public class LogicChunk extends Chunk {
    private final ServerWorld world;
    public final int x0, y0, z0;
    public final int x1, y1, z1;
    private final float x, y, z;
    public final int chunkX, chunkY, chunkZ;
    private final Block[][][] blocks;
    public final int width, height, depth;
    private boolean isDirty = true;
    public long dirtiedTime = 0L;

    public LogicChunk(ServerWorld world,
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

        ChunkGens.PLAIN.generate(this, blocks,
            width, height, depth,
            chunkX, chunkY, chunkZ);
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
            dirtiedTime = System.currentTimeMillis();
        }
        isDirty = true;
    }

    @Override
    public float distanceSqr(PlayerEntity player) {
        return player.position.distanceSquared(x, y, z);
    }
}
