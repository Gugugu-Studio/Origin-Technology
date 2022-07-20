package com.gugugu.oritech.util;

import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.ResType;

import java.util.Objects;

/**
 * @author squid233
 * @since 1.0
 */
public class Identifier {
    public static final String DEFAULT_NAMESPACE = "origin_technology";

    private final String namespace, path;

    private Identifier(String[] id) {
        if (id.length > 1) {
            namespace = id[0];
            path = id[1];
        } else {
            namespace = DEFAULT_NAMESPACE;
            path = id[0];
        }
    }

    public Identifier(String id) {
        this(id.split(":", 2));
    }

    public Identifier(String namespace, String path) {
        this(new String[]{namespace, path});
    }

    public ResLocation toResLocation(ResType type) {
        return new ResLocation(type, namespace, path);
    }

    public String namespace() {
        return namespace;
    }

    public String path() {
        return path;
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(namespace, that.namespace) &&
               Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }
}
