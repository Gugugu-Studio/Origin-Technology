package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.Chunk;

public class PlainChunkGen implements IChunkGen {
    @Override
    public void generate(ServerWorld world, Chunk chunk, Block[][][] blocks,
                         int height, int width, int depth,
                         int chunkX, int chunkY, int chunkZ)
    {
        SimplexNoise noise = new SimplexNoise(world.seed);
        for (int y = 0; y < height; y++) {
            int abs_y = Chunk.getAbsolutePos(chunkY, y);
            for (int x = 0; x < width; x++) {
                int abs_x = Chunk.getAbsolutePos(chunkX, x);
                for (int z = 0; z < depth; z++) {
                    int abs_z = Chunk.getAbsolutePos(chunkZ, z);
                    long grass_depth = noise.randWithin(abs_x, abs_z, 16) + 4;
                    if (abs_y < 0) {
                        blocks[y][x][z] = Blocks.STONE;
                    } else if (abs_y < grass_depth) {
                        blocks[y][x][z] = Blocks.DIRT;
                    } else if (abs_y == grass_depth) {
                        blocks[y][x][z] = Blocks.GRASS_BLOCK;
                    } else {
                        blocks[y][x][z] = Blocks.AIR;
                    }
                }
            }
        }
    }
}
