package com.gugugu.oritech.resource;

import com.gugugu.oritech.util.Identifier;

import java.util.Objects;

/**
 * @author squid233
 * @author theflysong
 * @since 1.0
 */
public class ResLocation {
    private ResType resType;
    private String namespace;
    private String path;

    private ResLocation(ResType type, String[] location) {
        resType = type;
        if (location.length > 1) {
            namespace = location[0];
            path = location[1];
        } else {
            namespace = Identifier.DEFAULT_NAMESPACE;
            path = location[0];
        }
    }

    public ResLocation(ResType type, String namespace, String path) {
        this(type, new String[]{namespace, path});
    }

    public ResLocation(ResType type, String location) {
        this(type, location.split(":", 2));
    }

    public static ResLocation ofAssets(String location) {
        return new ResLocation(ResType.ASSETS, location);
    }

    public static ResLocation ofBehavior(String location) {
        return new ResLocation(ResType.BEHAVIOR, location);
    }

    public static ResLocation ofCore(String location) {
        return new ResLocation(ResType.CORE, location);
    }

    public static ResLocation ofData(String location) {
        return new ResLocation(ResType.DATA, location);
    }

    public static ResLocation ofTexture(String namespace, String path) {
        return new ResLocation(ResType.ASSETS, namespace + ":textures/" + path + ".png");
    }

    public Identifier toIdentifier() {
        return new Identifier(namespace, path);
    }

    public ResType getResType() {
        return resType;
    }

    public void setResType(ResType resType) {
        this.resType = resType;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return resType.pathPrefix + "@" + namespace + ":" + path;
    }

    public String toPath() {
        return resType.pathPrefix + "/" + namespace + "/" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResLocation that = (ResLocation) o;
        return resType == that.resType &&
               Objects.equals(namespace, that.namespace) &&
               Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resType, namespace, path);
    }
}
