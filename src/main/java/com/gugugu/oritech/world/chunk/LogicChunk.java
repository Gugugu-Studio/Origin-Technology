package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.entity.PlayerEntity;

/**
 * @author squid233
 * @since 1.0
 */
public class LogicChunk extends Chunk {
    @Override
    public Block getBlock(int x, int y, int z) {
        return null;
    }

    @Override
    public void setBlock(Block block, int x, int y, int z) {
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public float distanceSqr(PlayerEntity player) {
        return 0;
    }
}
