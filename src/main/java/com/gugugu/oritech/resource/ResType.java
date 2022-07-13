package com.gugugu.oritech.resource;

/**
 * @author theflysong
 * @author squid233
 * @since 1.0
 */
public enum ResType {
    ASSETS("assets"),
    /**
     * Data and values
     */
    DATA("data"),
    /**
     * Resources that can't be modified
     */
    CORE("core"),
    /**
     * Behavior scripts
     */
    BEHAVIOR("behavior");

    public final String pathPrefix;

    ResType(String prefix) {
        this.pathPrefix = prefix;
    }
}
