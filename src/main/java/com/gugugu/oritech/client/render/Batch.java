package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.render.vertex.Vertex;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.gugugu.oritech.util.FloatByteUtil.color2byte;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author squid233
 * @since 1.0
 */
public class Batch {
    /**
     * The addend to expand the batch.
     */
    private static final double expandAddend = 0.6180339887498949; // golden ratio (Math.sqrt(5.0) - 1.0) * 0.5
    private static final int BUFFER_STRIDE = (3 + 1 + 2) * 4;
    private static final int VERTEX_COUNT = 20_000;
    private ByteBuffer buffer;
    private IntBuffer indexBuffer;
    private final List<Integer> indices = new ArrayList<>();
    private final Vertex vertex = new Vertex();
    private final int drawFreq;
    private boolean hasColor, hasTexture;
    private final int vao, vbo, ebo;
    private int vertexCount;
    private boolean bufferGrew = true;

    public Batch(int initNum, int drawFreq) {
        this.drawFreq = drawFreq;
        buffer = memAlloc(initNum * BUFFER_STRIDE);
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public Batch(int initNum) {
        this(initNum, GL_DYNAMIC_DRAW);
    }

    public Batch() {
        this(VERTEX_COUNT);
    }

    public Batch begin() {
        buffer.clear();
        if (indexBuffer != null) {
            indexBuffer.clear();
        }
        indices.clear();
        vertexCount = 0;
        hasColor = false;
        hasTexture = false;
        return this;
    }

    public Batch indices(int... indices) {
        for (int i : indices) {
            this.indices.add(i + vertexCount);
        }
        return this;
    }

    public Batch quadIndices() {
        return indices(0, 1, 2, 2, 3, 0);
    }

    public Batch color(float r, float g, float b, float a) {
        vertex.color(r, g, b, a);
        hasColor = true;
        return this;
    }

    public Batch color(float r, float g, float b) {
        return color(r, g, b, 1.0f);
    }

    public Batch texCoords(float u, float v) {
        vertex.texCoords(u, v);
        hasTexture = true;
        return this;
    }

    public Batch vertex(float x, float y, float z) {
        vertex.position(x, y, z);
        return emit();
    }

    public Batch vertex(float x, float y) {
        return vertex(x, y, 0.0f);
    }

    public Batch emit() {
        buffer.putFloat(vertex.x())
            .putFloat(vertex.y())
            .putFloat(vertex.z());
        if (hasColor) {
            buffer.put(color2byte(vertex.r()))
                .put(color2byte(vertex.g()))
                .put(color2byte(vertex.b()))
                .put(color2byte(vertex.a()));
        }
        if (hasTexture) {
            buffer.putFloat(vertex.s())
                .putFloat(vertex.t());
        }
        buffer = tryGrowBuffer(buffer, 128);
        ++vertexCount;
        return this;
    }

    private ByteBuffer tryGrowBuffer(ByteBuffer buffer, int len) {
        if (buffer.position() + len >= buffer.capacity()) {
            int increment = Math.max(len, (int) (buffer.capacity() * expandAddend));
            // Grows buffer for (1+expandAddend)x or len
            int sz = buffer.capacity() + increment;
            bufferGrew = true;
            return memRealloc(buffer, sz);
        }
        return buffer;
    }

    public Batch end() {
        if (buffer.position() > 0) {
            buffer.flip();
        }

        int stride = 12;
        if (hasColor) {
            stride += 4;
        }
        if (hasTexture) {
            stride += 8;
        }
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        if (bufferGrew) {
            nglBufferData(GL_ARRAY_BUFFER, buffer.capacity(), memAddress(buffer), drawFreq);
            bufferGrew = false;
        } else {
            glBufferSubData(GL_ARRAY_BUFFER, 0L, buffer);
        }
        boolean reallocated = false;
        if (indices.size() > 0) {
            if (indexBuffer == null) {
                indexBuffer = memAllocInt(indices.size());
                reallocated = true;
            } else if (indices.size() > indexBuffer.capacity()) {
                indexBuffer = memRealloc(indexBuffer, indices.size());
                reallocated = true;
            }
            for (int index : indices) {
                indexBuffer.put(index);
            }
            if (indexBuffer.position() > 0) {
                indexBuffer.flip();
            }
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            if (reallocated) {
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, drawFreq);
            } else {
                glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0L, indexBuffer);
            }
        } else {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0L);
        if (hasColor) {
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, stride, 12L);
        } else {
            glDisableVertexAttribArray(1);
        }
        if (hasTexture) {
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, hasColor ? 16L : 12L);
        } else {
            glDisableVertexAttribArray(2);
        }

        glBindVertexArray(0);
        return this;
    }

    public void render(int mode) {
        glBindVertexArray(vao);
        if (indices.size() > 0) {
            glDrawElements(mode, indices.size(), GL_UNSIGNED_INT, 0L);
        } else {
            glDrawArrays(mode, 0, vertexCount);
        }
        glBindVertexArray(0);

    }

    public void render() {
        render(GL_TRIANGLES);
    }

    public void free() {
        memFree(buffer);
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}
