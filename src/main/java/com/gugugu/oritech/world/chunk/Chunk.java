package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.world.entity.PlayerEntity;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class Chunk {
    public static final int CHUNK_SIZE = 32;

    public abstract float distanceSqr(PlayerEntity player);
}
