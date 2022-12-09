package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.Chunk;
import com.gugugu.oritech.world.chunk.noise.SimplexNoise;

/**
 * @author theflysong
 * @since 1.0
 */
public class PlainChunkGen implements IChunkGen {
    @Override
    public void generate(ServerWorld world, Chunk chunk, BlockState[][][] blocks,
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
                        blocks[y][x][z] = new BlockState(Blocks.STONE);
                    } else if (abs_y < grass_height) {
                        blocks[y][x][z] = new BlockState(Blocks.DIRT);
                    } else if (abs_y == grass_height) {
                        blocks[y][x][z] = new BlockState(Blocks.GRASS_BLOCK);
                    } else {
                        blocks[y][x][z] = new BlockState(Blocks.AIR);
                    }
                }
            }
        }
    }
}
