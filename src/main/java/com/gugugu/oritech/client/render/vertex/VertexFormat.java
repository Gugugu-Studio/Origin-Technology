package com.gugugu.oritech.client.render.vertex;

import com.gugugu.oritech.client.gl.DataType;

import static org.lwjgl.opengl.GL20C.*;

/**
 * @author squid233
 * @since 1.0
 */
public enum VertexFormat {
    POSITION(3, GL_FLOAT, false, DataType.FLOAT.repeat(3)),
    COLOR(4, GL_UNSIGNED_BYTE, true, DataType.UNSIGNED_BYTE.repeat(4)),
    TEX_COORDS(2, GL_FLOAT, false, DataType.FLOAT.repeat(2)),
    OVERLAY(2, GL_FLOAT, false, DataType.FLOAT.repeat(2)),
    LIGHTMAP(2, GL_FLOAT, false, DataType.FLOAT.repeat(2)),
    NORMAL(3, GL_BYTE, true, DataType.BYTE.repeat(3)),
    GENERIC(1, GL_BYTE, false, DataType.BYTE);

    private final int size, type;
    private final boolean normalized;
    public final DataType[] types;
    public final int length;

    VertexFormat(int size,
                 int type,
                 boolean normalized,
                 DataType... types) {
        this.size = size;
        this.type = type;
        this.normalized = normalized;
        this.types = types;
        int len = 0;
        for (DataType dataType : types) {
            len += dataType.length;
        }
        length = len;
    }

    public void beginDraw(int index, int stride, long pointer) {
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, type, normalized, stride, pointer);
    }

    public void endDraw(int index) {
        glDisableVertexAttribArray(index);
    }
}
