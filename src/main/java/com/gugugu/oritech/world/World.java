package com.gugugu.oritech.world;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.chunk.Chunk;

import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class World {
    public static final int LIMIT = 2097152;
    public static final int CHUNK_LIMIT = LIMIT / Chunk.CHUNK_SIZE;
    public static final int Y_LIMIT = CHUNK_LIMIT * 8;

    public abstract Block getBlock(int x, int y, int z);

    public abstract boolean setBlock(Block block, int x, int y, int z);

    public abstract Chunk getChunk(int x, int y, int z);

    public Chunk getChunkByBlockPos(int x, int y, int z) {
        return getChunk(Chunk.getChunkPos(x),
            Chunk.getChunkPos(y),
            Chunk.getChunkPos(z));
    }

    public abstract List<AABBox> getCubes(AABBox origin);

    public boolean canBlockPlaceOn(Block block, int x, int y, int z, Direction face) {
        return block.canPlaceOn(getBlock(x, y, z), this, x, y, z, face);
    }

    public boolean isInsideWorld(int x, int y, int z) {
        return x >= -LIMIT && x < LIMIT &&
               y >= -Y_LIMIT && y < Y_LIMIT &&
               z >= -LIMIT && z < LIMIT;
    }

    public boolean isOutsideWorld(int x, int y, int z) {
        return !isInsideWorld(x, y, z);
    }
}
