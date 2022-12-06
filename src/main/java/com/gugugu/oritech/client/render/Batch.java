package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.render.vertex.Vertex;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import org.joml.*;

import java.lang.Math;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static com.gugugu.oritech.util.FloatByteUtil.color2byte;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class Batch {
    /**
     * The addend to expand the batch.
     */
    private static final double expandAddend = 0.2;
    public static final int BUFFER_STRIDE = (3 + 1 + 2) * 4;
    public static final int VERTEX_COUNT = 20_000;
    public Matrix4fStack matrix = new Matrix4fStack(64);
    private ByteBuffer buffer;
    private IntBuffer indexBuffer;
    private final Vertex vertex = new Vertex();
    private final int drawFreq;
    private boolean hasColor, hasTexture, hasNormal;
    private int vao, vbo, ebo;
    private int vertexCount, indexCount;
    private boolean bufferGrew = true, indexBufferGrew = true;
    private boolean uploaded = false;
    private boolean built = false;
    private boolean hasInitGL = false;

    public Batch(int initNum, int drawFreq) {
        this.drawFreq = drawFreq;
        buffer = memAlloc(initNum * BUFFER_STRIDE);
    }

    public Batch(int initNum) {
        this(initNum, GL_DYNAMIC_DRAW);
    }

    public Batch() {
        this(VERTEX_COUNT);
    }

    public Batch initGL() {
        if (hasInitGL) {
            return this;
        }
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);
        hasInitGL = true;
        return this;
    }

    public Batch begin() {
        buffer.clear();
        if (indexBuffer != null) {
            indexBuffer.clear();
        }
        vertexCount = 0;
        indexCount = 0;
        hasColor = false;
        hasTexture = false;
        built = false;
        return this;
    }

    public Batch indices(int... indices) {
        if (indices.length > 0) {
            createIB(indices.length);
            for (int i : indices) {
                indexBuffer.put(i + vertexCount);
            }
            indexCount += indices.length;
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

    public Batch normal(float nx, float ny, float nz) {
        Matrix3f normalMatrix = new Matrix3f();
        normalMatrix.identity();
        Matrix4f copy_matrix = new Matrix4f(matrix);
        normalMatrix = copy_matrix.invert().transpose3x3(normalMatrix);
        Vector3f n_pos = new Vector3f(nx, ny, nz);
        n_pos = normalMatrix.transform(n_pos);
        vertex.normal(n_pos.x, n_pos.y, n_pos.z);
//        vertex.normal(nx, ny, nz);
        hasNormal = true;
        return this;
    }

    public Batch vertex(float x, float y, float z) {
        Vector4f position = new Vector4f(x, y, z, 1);
        matrix.transform(position);
        vertex.position(position.x, position.y, position.z);
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
        if (hasNormal) {
            buffer.putFloat(vertex.nx())
                .putFloat(vertex.ny())
                .putFloat(vertex.nz());
        }
        buffer = (ByteBuffer) tryGrowBuffer(buffer, 128);
        ++vertexCount;
        return this;
    }

    private Buffer tryGrowBuffer(Buffer buffer, int len) {
        if (buffer.position() + len >= buffer.capacity()) {
            int increment = Math.max(len, (int) (buffer.capacity() * expandAddend));
            // Grows buffer for (1+expandAddend)x or len
            int sz = buffer.capacity() + increment;
            if (buffer instanceof IntBuffer b) {
                indexBufferGrew = true;
                return memRealloc(b, sz);
            }
            if (buffer instanceof ByteBuffer b) {
                bufferGrew = true;
                return memRealloc(b, sz);
            }
        }
        return buffer;
    }

    private void createIB(int len) {
        if (indexBuffer == null) {
            final int sz = Math.max(len, 2);
            indexBuffer = memAllocInt(sz);
        } else {
            indexBuffer = (IntBuffer) tryGrowBuffer(indexBuffer, len);
        }
    }

    public Batch end() {
        if (buffer.position() > 0) {
            buffer.flip();
        }
        if (indexBuffer != null && indexBuffer.position() > 0) {
            indexBuffer.flip();
        }

        uploaded = false;
        built = true;
        return this;
    }

    public Batch upload() {
        int stride = 12;
        if (hasColor) {
            stride += 4;
        }
        if (hasTexture) {
            stride += 8;
        }
        if (hasNormal) {
            stride += 12;
        }
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        if (bufferGrew) {
            nglBufferData(GL_ARRAY_BUFFER, buffer.capacity(), memAddress(buffer), drawFreq);
            bufferGrew = false;
        } else {
            glBufferSubData(GL_ARRAY_BUFFER, 0L, buffer);
        }
        if (indexCount > 0) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            if (indexBufferGrew) {
                nglBufferData(GL_ELEMENT_ARRAY_BUFFER,
                    Integer.toUnsignedLong(indexBuffer.capacity()) << 2,
                    memAddress(indexBuffer),
                    drawFreq);
                indexBufferGrew = false;
            } else {
                glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0L, indexBuffer);
            }
        } else {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }
        long pointer = 0;
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, pointer);
        pointer += 12;
        if (hasColor) {
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, stride, pointer);
            pointer += 4;
        } else {
            glDisableVertexAttribArray(1);
        }
        if (hasTexture) {
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, pointer);
            pointer += 8;
        } else {
            glDisableVertexAttribArray(2);
        }
        if (hasTexture) {
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(3, 3, GL_FLOAT, false, stride, pointer);
            pointer += 12;
        } else {
            glDisableVertexAttribArray(3);
        }

        glBindVertexArray(0);
        uploaded = true;
        return this;
    }

    public boolean uploaded() {
        return uploaded;
    }

    public boolean hasBuilt() {
        return built;
    }

    public void render(int mode) {
        glBindVertexArray(vao);
        if (indexCount > 0) {
            glDrawElements(mode, indexCount, GL_UNSIGNED_INT, 0L);
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
        memFree(indexBuffer);
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}
