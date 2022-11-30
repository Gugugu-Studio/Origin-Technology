package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.block.BlockState;
import com.gugugu.oritech.util.math.BlockPos;
import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.block.Block;
import com.gugugu.oritech.world.World;
import com.gugugu.oritech.world.chunk.gen.ChunkGens;
import com.gugugu.oritech.entity.PlayerEntity;

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
    private final BlockState[][][] blocks;
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
        blocks = new BlockState[height][width][depth];

        ChunkGens.PLAIN.generate(world, this, blocks,
            width, height, depth,
            chunkX, chunkY, chunkZ);

        for (int y = 0 ; y < height ; y ++) {
            for (int x = 0 ; x < height ; x ++) {
                for (int z = 0 ; z < depth ; z ++) {
                    blocks[y][x][z].setPos(new BlockPos(x, y, z));
                    blocks[y][x][z].setChunk(this);
                }
            }
        }
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return blocks[y][x][z];
    }

    @Override
    public void setBlock(BlockState block, int x, int y, int z) {
        blocks[y][x][z] = block;
        block.setChunk(this);
        block.setPos(new BlockPos(x, y, z));
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
        world.dirtyChunks.add(this);
    }

    public BlockState[][][] getBlocks() {
        return blocks;
    }

    @Override
    public float distanceSqr(PlayerEntity player) {
        return player.position.distanceSquared(x, y, z);
    }

    @Override
    public World getWorld() {
        return world;
    }
}
