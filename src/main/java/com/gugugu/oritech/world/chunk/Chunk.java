package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.entity.PlayerEntity;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class Chunk {
    public static final int CHUNK_SIZE = 32;

    public static int getRelativePos(int p) {
        return p & (CHUNK_SIZE - 1);
    }

    public static int getAbsolutePos(int chunkPos, int p) {
        return chunkPos * CHUNK_SIZE + p;
    }

    public static int getChunkPos(int blockPos) {
        return Math.floorDiv(blockPos, Chunk.CHUNK_SIZE);
    }

    /**
     * Get block by relative position.
     *
     * @param x the pos x
     * @param y the pos y
     * @param z the pos z
     * @return the block
     */
    public abstract Block getBlock(int x, int y, int z);

    public Block getBlockAbsolute(int x, int y, int z) {
        return getBlock(getRelativePos(x), getRelativePos(y), getRelativePos(z));
    }

    /**
     * Set block by relative position.
     *
     * @param block the block
     * @param x     the pos x
     * @param y     the pos y
     * @param z     the pos z
     */
    public abstract void setBlock(Block block, int x, int y, int z);

    public void setBlockAbsolute(Block block, int x, int y, int z) {
        setBlock(block, getRelativePos(x), getRelativePos(y), getRelativePos(z));
    }

    public abstract boolean isDirty();

    public abstract void markDirty();

    public abstract float distanceSqr(PlayerEntity player);
}
