package com.gugugu.oritech.world.chunk.gen;

import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.block.Block;

/**
 * @author theflysong
 * @since 1.0
 */
public class ChunkGens {
    public static final IChunkGen SMOOTH = register(0, "smooth", new SmoothChunkGen());
    public static final IChunkGen PLAIN = register(1, "plain", new PlainChunkGen());

    private static IChunkGen register(int rawId, String id, IChunkGen gen) {
        return Registry.CHUNK_GEN.set(rawId, new Identifier(id), gen);
    }

    public static void register() {
        // Empty initializer
    }
}
