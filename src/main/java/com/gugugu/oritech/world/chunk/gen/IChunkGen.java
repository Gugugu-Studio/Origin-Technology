package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.chunk.Chunk;

/**
 * @author theflysong
 * @since 1.0
 */
public interface IChunkGen {
    void generate(Chunk chunk, Block[][][] blocks,
                  int height, int width, int depth,
                  int chunkX, int chunkY, int chunkZ);
}
