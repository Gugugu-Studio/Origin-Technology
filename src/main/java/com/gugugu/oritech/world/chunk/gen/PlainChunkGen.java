package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.Chunk;
import com.gugugu.oritech.world.chunk.noise.SimplexNoise;

/**
 * @author theflysong
 * @since 1.0
 */
public class PlainChunkGen implements IChunkGen {
    @Override
    public void generate(ServerWorld world, Chunk chunk, Block[][][] blocks,
                         int height, int width, int depth,
                         int chunkX, int chunkY, int chunkZ)
    {
        SimplexNoise dirtNoise = new SimplexNoise(world.seed);
        SimplexNoise grassNoise = new SimplexNoise(world.seed + 32);
        for (int y = 0; y < height; y++) {
            int abs_y = Chunk.getAbsolutePos(chunkY, y);
            for (int x = 0; x < width; x++) {
                int abs_x = Chunk.getAbsolutePos(chunkX, x);
                for (int z = 0; z < depth; z++) {
                    int abs_z = Chunk.getAbsolutePos(chunkZ, z);
                    long grass_y = grassNoise.randWithin(abs_x, abs_z, 6) + 4;
                    long dirt_depth = dirtNoise.randWithin(abs_x, abs_z, 3) + 1;
                    long rock_y = grass_y - dirt_depth;
                    if (abs_y < rock_y) {
                        blocks[y][x][z] = Blocks.STONE;
                    }
                    else if (abs_y < grass_y) {
                        blocks[y][x][z] = Blocks.DIRT;
                    }
                    else if (abs_y == grass_y) {
                        blocks[y][x][z] = Blocks.GRASS_BLOCK;
                    }
                    else {
                        blocks[y][x][z] = Blocks.AIR;
                    }
                }
            }
        }
    }
}
