package com.gugugu.oritech.world;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.chunk.Chunk;

import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public class ServerWorld extends World {
    @Override
    public Block getBlock(int x, int y, int z) {
        return null;
    }

    @Override
    public boolean setBlock(Block block, int x, int y, int z) {
        return false;
    }

    @Override
    public Chunk getChunk(int x, int y, int z) {
        return null;
    }

    @Override
    public List<AABBox> getCubes(AABBox origin) {
        return null;
    }
}
