package com.gugugu.oritech.world;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.world.block.Block;

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
    public List<AABBox> getCubes(AABBox origin) {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getDepth() {
        return 0;
    }
}
