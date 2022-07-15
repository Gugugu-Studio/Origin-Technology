package com.gugugu.oritech.renderer;

/**
 * @author squid233
 * @since 1.0
 */
public final class VertexLayouts {
    public static final VertexLayout POSITION = new VertexLayout(VertexFormat.POSITION);
    public static final VertexLayout POSITION_COLOR = new VertexLayout(VertexFormat.POSITION, VertexFormat.COLOR);
}
