package com.gugugu.oritech.util.math;

import com.gugugu.oritech.world.chunk.Chunk;

/**
 * A chunk pos.
 *
 * @param x pos x
 * @param y pos y
 * @param z pos z
 * @author squid233
 * @since 1.0
 */
public record ChunkPos(int x, int y, int z) {
    /**
     * Convert to block pos.
     *
     * @return the block pos
     */
    public BlockPos toBlockPos() {
        return new BlockPos(x * Chunk.CHUNK_SIZE,
            y * Chunk.CHUNK_SIZE,
            z * Chunk.CHUNK_SIZE);
    }
}
