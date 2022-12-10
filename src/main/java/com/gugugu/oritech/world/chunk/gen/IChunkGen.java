package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.world.chunk.Chunk;

/**
 * @author theflysong
 * @since 1.0
 */
public interface IChunkGen {
    void generate(ServerWorld world, Chunk chunk, BlockState[][][] blocks,
                  int width, int height, int depth,
                  int chunkX, int chunkY, int chunkZ);
}
