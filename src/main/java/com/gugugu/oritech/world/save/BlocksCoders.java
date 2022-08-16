package com.gugugu.oritech.world.save;

import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.registry.Registry;

public class BlocksCoders {
    public static final IBlocksCoder RAW = register(0, "raw", new RawBlocksCoder());

    private static IBlocksCoder register(int rawId, String id, IBlocksCoder gen) {
        return Registry.CODER.set(rawId, new Identifier(id), gen);
    }

    public static void register() {
        // Empty initializer
    }
}
