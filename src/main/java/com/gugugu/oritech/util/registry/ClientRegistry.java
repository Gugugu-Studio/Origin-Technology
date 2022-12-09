package com.gugugu.oritech.util.registry;

import com.gugugu.oritech.client.renderer.AbstractBlockStateRenderer;
import com.gugugu.oritech.client.renderer.BlockStateRenderers;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;

import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public abstract class ClientRegistry<T> extends Registry<T> {
    public static final DefaultedRegistry<AbstractBlockStateRenderer> BLOCKSTATE_RENDERER =
        create(() -> BlockStateRenderers.COMMON);

    public static <T, R extends T> R register(ClientRegistry<T> registry,
                                              Identifier id,
                                              R r) {
        return registry.add(id, r);
    }

    public static <T, R extends T> R register(ClientRegistry<T> registry,
                                              String id,
                                              R r) {
        return register(registry, new Identifier(id), r);
    }

    public abstract <R extends T> R add(Identifier id, R r);

    private static <T> DefaultedRegistry<T> create(Supplier<T> defaultEntry) {
        return new DefaultedRegistry<>(defaultEntry);
    }
}
