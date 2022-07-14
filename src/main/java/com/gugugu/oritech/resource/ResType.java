package com.gugugu.oritech.resource;

/**
 * @author theflysong
 * @author squid233
 * @since 1.0
 */
public enum ResType {
    /**
     * Textures, models and so on
     */
    ASSETS("assets"),
    /**
     * Data and values
     */
    DATA("data"),
    /**
     * Core resources. Shouldn't be modified
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
