package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.block.BlockState;
import com.gugugu.oritech.util.math.BlockPos;
import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.block.Block;
import com.gugugu.oritech.block.Blocks;
import com.gugugu.oritech.world.chunk.Chunk;

/**
 * @author theflysong
 * @since 1.0
 */
public class FlatChunkGen implements IChunkGen {
    @Override
    public void generate(ServerWorld world, Chunk chunk, BlockState[][][] blocks,
                         int width, int height, int depth,
                         int chunkX, int chunkY, int chunkZ) {
        for (int y = 0; y < height; y++) {
            int absY = Chunk.getAbsolutePos(chunkY, y);
            for (int x = 0; x < width; x++) {
//                int absX = Chunk.getAbsolutePos(chunkX, x);
                for (int z = 0; z < depth; z++) {
//                    int absZ = Chunk.getAbsolutePos(chunkZ, z);
                    if (absY < 0) {
                        blocks[y][x][z] = new BlockState(Blocks.STONE);
                    } else if (absY < 4) {
                        blocks[y][x][z] = new BlockState(Blocks.DIRT);
                    } else if (absY == 4) {
                        blocks[y][x][z] = new BlockState(Blocks.GRASS_BLOCK);
                    } else {
                        blocks[y][x][z] = new BlockState(Blocks.AIR);
                    }
                }
            }
        }
    }
}
