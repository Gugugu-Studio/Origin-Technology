package com.gugugu.oritech.world;

import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.RenderChunk;

import static com.gugugu.oritech.world.chunk.Chunk.CHUNK_SIZE;

/**
 * @author squid233
 * @since 1.0
 */
public class ClientWorld extends World implements AutoCloseable {
    private final Block[][][] blocks;
    public final RenderChunk chunk;
    public final int width, height, depth;

    public ClientWorld(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        blocks = new Block[height][width][depth];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int y = 0; y < 16; y++) {
                    blocks[y][x][z] = Blocks.STONE;
                }
                for (int y = 16; y < CHUNK_SIZE; y++) {
                    blocks[y][x][z] = Blocks.AIR;
                }
            }
        }
        chunk = new RenderChunk(this);
    }

    public boolean isInBounds(int x, int y, int z) {
        return x >= 0 && x < width &&
               y >= 0 && y < height &&
               z >= 0 && z < depth;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        if (isInBounds(x, y, z))
            return blocks[y][x][z];
        return Blocks.AIR;
    }

    public RenderChunk getChunk(int x, int y, int z) {
        return chunk;
    }

    @Override
    public void close() {
        chunk.close();
    }
}
