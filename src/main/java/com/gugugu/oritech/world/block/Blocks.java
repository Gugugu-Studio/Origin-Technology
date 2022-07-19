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

    private static Block register(int rawId, String id, Block block) {
        return Registry.BLOCK.set(rawId, new Identifier(id), block);
    }
}
