package com.gugugu.oritech.util.registry;

import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.world.block.Block;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class Registry<T> {
    public static DefaultedRegistry<Block> BLOCK;

    public static <T, R extends T> R register(Registry<T> registry,
                                              Identifier id,
                                              R r) {
        return r;
    }

    static {
        BLOCK = create();
    }

    private static <T> DefaultedRegistry<T> create() {
        return new DefaultedRegistry<>();
    }
}
