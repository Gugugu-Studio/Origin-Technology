package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.world.entity.PlayerEntity;

/**
 * @author squid233
 * @since 1.0
 */
public class LogicChunk extends Chunk {
    @Override
    public float distanceSqr(PlayerEntity player) {
        return 0;
    }
}
