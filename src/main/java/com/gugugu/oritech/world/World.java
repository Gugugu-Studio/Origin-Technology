package com.gugugu.oritech.world;

import com.gugugu.oritech.world.block.Block;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class World {
    public abstract Block getBlock(int x, int y, int z);
}
