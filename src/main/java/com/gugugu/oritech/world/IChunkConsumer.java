package com.gugugu.oritech.world;

import com.gugugu.oritech.world.chunk.Chunk;

/**
 * @author squid233
 * @since 1.0
 */
@FunctionalInterface
public interface IChunkConsumer {
    void accept(Chunk chunk, int x, int y, int z);
}
