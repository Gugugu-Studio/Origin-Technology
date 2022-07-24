package com.gugugu.oritech.client.render.vertex;

import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public final class VertexLayouts {
    public static final VertexLayout POSITION = new VertexLayout(VertexFormat.POSITION);
    public static final VertexLayout POSITION_COLOR = new VertexLayout(VertexFormat.POSITION, VertexFormat.COLOR);
    public static final VertexLayout POSITION_COLOR_TEX = new VertexLayout(VertexFormat.POSITION, VertexFormat.COLOR, VertexFormat.TEX_COORDS);
    public static final VertexLayout POSITION_TEX = new VertexLayout(VertexFormat.POSITION, VertexFormat.GENERIC, VertexFormat.TEX_COORDS);
}
