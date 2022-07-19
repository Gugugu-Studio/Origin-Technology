package com.gugugu.oritech.util.registry;

import com.gugugu.oritech.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author squid233
 * @since 1.0
 */
public class DefaultedRegistry<T> extends Registry<T> {
    private final Map<Identifier, T> idToEntry = new LinkedHashMap<>();
    private final Map<T, Identifier> entryToId = new LinkedHashMap<>();
    private final Map<Integer, T> rawIdToEntry = new LinkedHashMap<>();
    private final Map<T, Integer> entryToRawId = new LinkedHashMap<>();
    private final Supplier<T> defaultEntrySupplier;
    private T defaultEntry;
    private int nextId = 0;

    public DefaultedRegistry(Supplier<T> defaultEntry) {
        defaultEntrySupplier = defaultEntry;
    }

    public <R extends T> R set(int rawId, Identifier id, R r) {
        idToEntry.put(id, r);
        entryToId.put(r, id);
        rawIdToEntry.put(rawId, r);
        entryToRawId.put(r, rawId);
        if (rawId >= nextId) {
            ++nextId;
        }
        return r;
    }

    @Override
    public <R extends T> R add(Identifier id, R r) {
        return set(nextId, id, r);
    }

    public T get(Identifier id) {
        return idToEntry.getOrDefault(id, defaultEntry());
    }

    public T get(int rawId) {
        return rawIdToEntry.getOrDefault(rawId, defaultEntry());
    }

    public Identifier getId(T t) {
        return entryToId.getOrDefault(t, entryToId.get(defaultEntry()));
    }

    public int getRawId(T t) {
        return entryToRawId.getOrDefault(t, entryToRawId.get(defaultEntry()));
    }

    public T defaultEntry() {
        if (defaultEntry == null) {
            defaultEntry = defaultEntrySupplier.get();
        }
        return defaultEntry;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return idToEntry.values().iterator();
    }
}
