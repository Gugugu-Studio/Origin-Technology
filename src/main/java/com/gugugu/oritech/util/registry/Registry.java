package com.gugugu.oritech.util.registry;

import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.gen.ChunkGens;
import com.gugugu.oritech.world.chunk.gen.IChunkGen;
import com.gugugu.oritech.world.save.BlocksCoders;
import com.gugugu.oritech.world.save.IBlocksCoder;

import java.util.function.Supplier;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class Registry<T> implements Iterable<T> {
    public static final DefaultedRegistry<Block> BLOCK = create(() -> Blocks.AIR);
    public static final DefaultedRegistry<IBlocksCoder> CODER = create(() -> BlocksCoders.RAW);
    public static final DefaultedRegistry<IChunkGen> CHUNK_GEN = create(() -> ChunkGens.FLAT);

    public static <T, R extends T> R register(Registry<T> registry,
                                              Identifier id,
                                              R r) {
        return registry.add(id, r);
    }

    public static <T, R extends T> R register(Registry<T> registry,
                                              String id,
                                              R r) {
        return register(registry, new Identifier(id), r);
    }

    public abstract <R extends T> R add(Identifier id, R r);

    private static <T> DefaultedRegistry<T> create(Supplier<T> defaultEntry) {
        return new DefaultedRegistry<>(defaultEntry);
    }
}
