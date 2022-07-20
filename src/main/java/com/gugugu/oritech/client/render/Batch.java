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
    private static final int BUFFER_STRIDE = (3 + 1 + 2) * 4;
    private static final int VERTEX_COUNT = 20_000;
    private static final int BUFFER_SIZE = BUFFER_STRIDE * VERTEX_COUNT;
    private final ByteBuffer buffer;
    private IntBuffer indexBuffer;
    private final List<Integer> indices = new ArrayList<>();
    private final Vertex vertex = new Vertex();
    private boolean hasColor, hasTexture;
    private final int vao, vbo, ebo;
    private int vertexCount;
    private int bufferSize;

    public Batch() {
        buffer = memAlloc(BUFFER_SIZE);
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public Batch begin() {
        if (buffer.position() > 0) {
            buffer.clear();
        }
        if (indexBuffer != null && indexBuffer.position() > 0) {
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
        ++vertexCount;
        return this;
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
        if (bufferSize < buffer.remaining()) {
            bufferSize=buffer.remaining();
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
        } else {
            glBufferSubData(GL_ARRAY_BUFFER, 0L, buffer);
        }
        boolean reallocated = false;
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
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_DYNAMIC_DRAW);
        } else {
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0L, indexBuffer);
        }
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0L);
        if (hasColor) {
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, stride, 12L);
        }
        if (hasTexture) {
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, hasColor ? 16L : 12L);
        }

        glBindVertexArray(0);
        return this;
    }

    public void render() {
        glBindVertexArray(vao);

        glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0L);

        glBindVertexArray(0);
    }

    public void free() {
        memFree(buffer);
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}
