package com.gugugu.oritech.client.render;

import com.gugugu.oritech.util.FloatByteUtil;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class Tesselator {
    private static final int MEMORY_USE = 4 * 1024 * 1024;
    private static Tesselator instance;
    private final ByteBuffer buffer = memAlloc(MEMORY_USE);
    private IntBuffer indexBuffer;
    private int stride = 12, vertexCount;
    private boolean hasColor, hasTexCoord;
    private float x, y, z, r, g, b, u, v;
    private int builtVao, builtVbo, builtEbo;
    private EndProperty builtProperty;

    public static Tesselator getInstance() {
        if (instance == null) {
            instance = new Tesselator();
        }
        return instance;
    }

    private Tesselator() {
    }

    public int builtVao() {
        if (builtVao == 0) {
            builtVao = glGenVertexArrays();
        }
        return builtVao;
    }

    public int builtVbo() {
        if (builtVbo == 0) {
            builtVbo = glGenBuffers();
        }
        return builtVbo;
    }

    public int builtEbo() {
        if (builtEbo == 0) {
            builtEbo = glGenBuffers();
        }
        return builtEbo;
    }

    public EndProperty builtProperty() {
        if (builtProperty == null) {
            builtProperty = new EndProperty();
        }
        return builtProperty;
    }

    /**
     * @author squid233
     * @since 1.0
     */
    public static final class EndProperty {
        public boolean vaVertex, vaColor, vaTexCoord;
        public int bufferSize, indicesSize;
    }

    public void begin() {
        vertexCount = 0;
        buffer.clear();
        if (indexBuffer != null) {
            indexBuffer.clear();
        }
    }

    public Tesselator vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Tesselator vertex(float x, float y) {
        return vertex(x, y, 0.0f);
    }

    public Tesselator color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        if (!hasColor) {
            hasColor = true;
            stride += 3;
        }
        return this;
    }

    public Tesselator texCoord(float u, float v) {
        this.u = u;
        this.v = v;
        if (!hasTexCoord) {
            hasTexCoord = true;
            stride += 8;
        }
        return this;
    }

    private void growIndexBuf(int len) {
        if (indexBuffer == null) {
            indexBuffer = memAllocInt(len);
        } else if (indexBuffer.remaining() < len) {
            indexBuffer = memRealloc(indexBuffer, Math.max(indexBuffer.capacity() * 3 / 2, indexBuffer.capacity() + len));
        }
    }

    public Tesselator index(int... indices) {
        growIndexBuf(indices.length);
        for (int index : indices) {
            indexBuffer.put(vertexCount + index);
        }
        return this;
    }

    public void emit() {
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
        if (hasColor) {
            buffer.put(FloatByteUtil.color2byte(r));
            buffer.put(FloatByteUtil.color2byte(g));
            buffer.put(FloatByteUtil.color2byte(b));
        }
        if (hasTexCoord) {
            buffer.putFloat(u);
            buffer.putFloat(v);
        }
        ++vertexCount;
    }

    public void end(int vao, int vbo, int ebo, EndProperty property) {
        boolean noVbo = !glIsBuffer(vbo);
        boolean noEbo = !glIsBuffer(ebo);
        boolean vaChanged = !property.vaVertex ||
                            (hasColor && !property.vaColor) ||
                            (hasTexCoord && !property.vaTexCoord);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        if (noVbo || property.bufferSize < buffer.position()) {
            property.bufferSize = buffer.position();
            glBufferData(GL_ARRAY_BUFFER, buffer.flip(), GL_DYNAMIC_DRAW);
        } else {
            glBufferSubData(GL_ARRAY_BUFFER, 0, buffer.flip());
        }

        if (vaChanged) {
            glBindVertexArray(vao);
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        if (indexBuffer != null) {
            indexBuffer.flip();
        }
        if (indexBuffer != null) {
            if (noEbo || property.indicesSize < indexBuffer.limit()) {
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_DYNAMIC_DRAW);
            } else {
                glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indexBuffer);
            }
            property.indicesSize = indexBuffer.limit();
        }

        if (!property.vaVertex) {
            property.vaVertex = true;
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        }
        if (hasColor && !property.vaColor) {
            property.vaColor = true;
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_UNSIGNED_BYTE, true, stride, 12);
        }
        if (hasTexCoord && !property.vaTexCoord) {
            property.vaTexCoord = true;
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, hasColor ? 15 : 12);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (vaChanged) {
            glBindVertexArray(0);
        }
    }

    public void end() {
        end(builtVao(), builtVbo(), builtEbo(), builtProperty());
    }

    public void builtDraw(int mode) {
        glBindVertexArray(builtVao);
        glDrawElements(mode, builtProperty.indicesSize, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void free() {
        memFree(buffer);
        memFree(indexBuffer);
        glDeleteVertexArrays(builtVao);
        glDeleteBuffers(builtVbo);
        glDeleteBuffers(builtEbo);
    }
}
