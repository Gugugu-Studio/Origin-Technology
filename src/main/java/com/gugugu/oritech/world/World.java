package com.gugugu.oritech.world;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.block.Block;

import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class World {
    public abstract Block getBlock(int x, int y, int z);

    public abstract boolean setBlock(Block block, int x, int y, int z);

    public abstract List<AABBox> getCubes(AABBox origin);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getDepth();

    public boolean canBlockPlaceOn(Block block, int x, int y, int z, Direction face) {
        return block.canPlaceOn(getBlock(x, y, z), this, x, y, z, face);
    }

    public boolean isInsideWorld(int x, int y, int z) {
        return x >= 0 && x < getWidth() &&
               y >= 0 && y < getHeight() &&
               z >= 0 && z < getDepth();
    }

    public boolean isOutsideWorld(int x, int y, int z) {
        return !isInsideWorld(x, y, z);
    }
}
