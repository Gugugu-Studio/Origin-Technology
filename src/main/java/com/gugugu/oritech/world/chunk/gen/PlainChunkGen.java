package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.Chunk;
import com.gugugu.oritech.world.chunk.noise.SimplexNoise;
import org.joml.Random;

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
        SimplexNoise dirt_noise = new SimplexNoise(world.seed);
        SimplexNoise stone_noise = new SimplexNoise(world.seed + 64);
        for (int y = 0; y < height; y++) {
            int abs_y = Chunk.getAbsolutePos(chunkY, y);
            for (int x = 0; x < width; x++) {
                int abs_x = Chunk.getAbsolutePos(chunkX, x);
                for (int z = 0; z < depth; z++) {
                    int abs_z = Chunk.getAbsolutePos(chunkZ, z);
                    long dirt_depth = dirt_noise.randWithin(abs_x, abs_z, 3);
                    long stone_height = stone_noise.randWithin(abs_x, abs_z, 4);
                    long grass_height = stone_height + dirt_depth + 1;
                    if (abs_y < stone_height) {
                        blocks[y][x][z] = Blocks.STONE;
                    } else if (abs_y < grass_height) {
                        blocks[y][x][z] = Blocks.DIRT;
                    } else if (abs_y == grass_height) {
                        blocks[y][x][z] = Blocks.GRASS_BLOCK;
                    } else {
                        blocks[y][x][z] = Blocks.AIR;
                    }
                }
            }
        }
    }
}
