package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.chunk.gen.FlatChunkGen;
import com.gugugu.oritech.world.chunk.gen.IChunkGen;

public class BlockStateRenderers {
    public static final AbstractBlockStateRenderer COMMON = register(0, "common", new BlockStateRenderer());

    private static AbstractBlockStateRenderer register(int rawId, String id, AbstractBlockStateRenderer renderer) {
        return Registry.BLOCKSTATE_RENDERER.set(rawId, new Identifier(id), renderer);
    }

    public static void register() {
        // Empty initializer
    }
}
