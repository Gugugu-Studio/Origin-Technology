package com.gugugu.oritech.world;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.world.block.Block;

import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class World {
    public abstract Block getBlock(int x, int y, int z);

    public abstract List<AABBox> getCubes(AABBox origin);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getDepth();
}
