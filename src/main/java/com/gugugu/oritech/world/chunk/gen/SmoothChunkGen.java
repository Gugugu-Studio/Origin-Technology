package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.Chunk;

public class SmoothChunkGen implements IChunkGen {
    @Override
    public void generate(Chunk chunk, Block[][][] blocks,
                         int height, int width, int depth,
                         int chunkX, int chunkY, int chunkZ)
    {
        int layer;
        for (int y = 0; y < height; y++) {
            layer = chunk.getAbsolutePos(chunkY, y);
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    if (layer < 0) {
                        blocks[y][x][z] = Blocks.STONE;
                    } else if (layer < 4) {
                        blocks[y][x][z] = Blocks.DIRT;
                    } else if (layer == 4) {
                        blocks[y][x][z] = Blocks.GRASS_BLOCK;
                    } else {
                        blocks[y][x][z] = Blocks.AIR;
                    }
                }
            }
        }
    }
}
