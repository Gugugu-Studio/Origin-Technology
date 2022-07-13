package com.gugugu.oritech.resource;

public class ResLocation {
    public static final String DEFAULT_NAMESPACE = "origin_technology";

    public enum ResType {
        ASSETS("assets"),
        DATA("data"),
        CORE("core"),
        BEHAVIOR("behavior");

        public String pathPrefix;

        ResType(String prefix) {
            this.pathPrefix = prefix;
        }
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

    private ResType resType;
    private String namespace;
    private String path;

    public ResLocation(ResType resType, String namespace, String path) {
        this.resType = resType;
        this.namespace = namespace;
        this.path = path;
    }

    public ResLocation(ResType resType, String path) {
        this(resType, DEFAULT_NAMESPACE, path);
    }

    @Override
    public String toString() {
        return resType.pathPrefix + "@" + namespace + ":" + path;
    }

    public String toPath() {
        return resType.pathPrefix + "/" + namespace + "/" + path;
    }
}
