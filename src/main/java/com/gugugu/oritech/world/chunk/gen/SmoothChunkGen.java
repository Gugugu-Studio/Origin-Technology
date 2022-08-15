package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.Chunk;

/**
 * @author theflysong
 * @since 1.0
 */
public class SmoothChunkGen implements IChunkGen {
    @Override
    public void generate(ServerWorld world, Chunk chunk, Block[][][] blocks,
                         int height, int width, int depth,
                         int chunkX, int chunkY, int chunkZ) {
        for (int y = 0; y < height; y++) {
            int abs_y = Chunk.getAbsolutePos(chunkY, y);
            for (int x = 0; x < width; x++) {
                int abs_x = Chunk.getAbsolutePos(chunkX, x);
                for (int z = 0; z < depth; z++) {
                    int abs_z = Chunk.getAbsolutePos(chunkZ, z);
                    if (abs_y < 0) {
                        blocks[y][x][z] = Blocks.STONE;
                    } else if (abs_y < 4) {
                        blocks[y][x][z] = Blocks.DIRT;
                    } else if (abs_y == 4) {
                        blocks[y][x][z] = Blocks.GRASS_BLOCK;
                    } else {
                        blocks[y][x][z] = Blocks.AIR;
                    }
                }
            }
        }
    }
}
