package com.gugugu.oritech.world.block;

import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.registry.Registry;

/**
 * @author squid233
 * @since 1.0
 */
public class Blocks {
    public static final Block AIR = register(0, "air", new AirBlock());
    public static final Block STONE = register(1, "stone", new Block());
    public static final Block GRASS_BLOCK = register(2, "grass_block", new Block());
    public static final Block DIRT = register(3, "dirt", new Block());
    public static final Block LOG = register(4, "log", new LogBlock());
    public static final Block LEAVES = register(5, "leaves", new LeavesBlock());
    public static final Block DIRT_STAIR = register(6, "dirt_stair", new DirtStairBlock());

    private static Block register(int rawId, String id, Block block) {
        return Registry.BLOCK.set(rawId, new Identifier(id), block);
    }

    public static void register() {
        // Empty initializer
    }
}
