package com.gugugu.oritech.client.render.vertex;

/**
 * @author squid233
 * @since 1.0
 */
public final class VertexLayouts {
    public static final VertexLayout POSITION = new VertexLayout(VertexFormat.POSITION);
    public static final VertexLayout POSITION_COLOR = new VertexLayout(VertexFormat.POSITION, VertexFormat.COLOR);
    public static final VertexLayout POSITION_COLOR_TEX = new VertexLayout(VertexFormat.POSITION, VertexFormat.COLOR, VertexFormat.TEX_COORDS);
}
