package com.gugugu.oritech.client.gl;

import org.joml.*;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL31C.GL_UNIFORM_BUFFER;
import static org.lwjgl.system.MemoryUtil.*;

public record UniformUnit(int offset, int size, int ubo, ByteBuffer buffer) implements AutoCloseable {
    public static UniformUnit of(int offset, int size, int ubo) {
        return new UniformUnit(offset, size, ubo, memAlloc(size));
    }

    public void set(int offset, int size, ByteBuffer buffer) {
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        nglBufferSubData(GL_UNIFORM_BUFFER, offset, size, memAddress(buffer));
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    public void set(int offset, int size, int value) {
        set(offset, size, buffer.position(0).putInt(value));
    }

    public void set(int offset, int size, float value) {
        set(offset, size, buffer.position(0).putFloat(value));
    }

    public void set(int offset, int size, boolean value) {
        set(offset, size, buffer.position(0).putInt(value ? 1 : 0));
    }

    public void set(int offset, int eleSize, float[] values) {
        int off = offset;
        for (float val : values) {
            set(off, eleSize, val);
            off += 16;
        }
    }

    public void set(int offset, int eleSize, int[] values) {
        int off = offset;
        for (int val : values) {
            set(off, eleSize, val);
            off += 16;
        }
    }

    public void set(int offset, int eleSize, boolean[] values) {
        int off = offset;
        for (boolean val : values) {
            set(off, eleSize, val);
            off += 16;
        }
    }

    public void set(int offset, int eleSize, Vector4fc value) {
        set(offset, eleSize * 4, value.get(buffer.position(0)));
    }

    public void set(int offset, int eleSize, Vector3fc value) {
        set(offset, eleSize * 4, value.get(buffer.position(0)));
    }

    public void set(int offset, int eleSize, Vector2fc value) {
        set(offset, eleSize * 2, value.get(buffer.position(0)));
    }

    public void set(int offset, int eleSize, Vector4ic value) {
        set(offset, eleSize * 4, value.get(buffer.position(0)));
    }

    public void set(int offset, int eleSize, Vector3ic value) {
        set(offset, eleSize * 4, value.get(buffer.position(0)));
    }

    public void set(int offset, int eleSize, Vector2ic value) {
        set(offset, eleSize * 2, value.get(buffer.position(0)));
    }

    public void set(int offset, Matrix2fc value) {
        set(offset, 16, value.get(buffer.position(0)));
    }

    public void set(int offset, Matrix3fc value) {
        set(offset, 36, value.get(buffer.position(0)));
    }

    public void set(int offset, Matrix4fc value) {
        set(offset, 64, value.get(buffer.position(0)));
    }

    public void set(int offset, int eleSize, float x, float y, float z, float w) {
        set(offset, eleSize, new Vector4f(x, y, z, w));
    }

    public void set(int offset, int eleSize, float x, float y, float z) {
        set(offset, eleSize, new Vector3f(x, y, z));
    }

    public void set(int offset, int eleSize, float x, float y) {
        set(offset, eleSize, new Vector2f(x, y));
    }

    public void set(int offset, int eleSize, int u, int v) {
        set(offset, eleSize, new Vector2i(u, v));
    }

    public void set(ByteBuffer buffer) {
        set(offset, size, buffer);
    }

    public void set(int value) {
        set(offset, size, value);
    }

    public void set(float value) {
        set(offset, 4, value);
    }

    public void set(boolean value) {
        set(offset, 4, value);
    }

    public void set(float[] values) {
        set(offset, 4, values);
    }

    public void set(int[] values) {
        set(offset, 4, values);
    }

    public void set(boolean[] values) {
        set(offset, 4, values);
    }

    public void set(Vector4fc value) {
        set(offset, 16, value);
    }

    public void set(Vector3fc value) {
        set(offset, 16, value);
    }

    public void set(Vector2fc value) {
        set(offset, 16, value);
    }

    public void set(Vector4ic value) {
        set(offset, 16, value);
    }

    public void set(Vector3ic value) {
        set(offset, 16, value);
    }

    public void set(Vector2ic value) {
        set(offset, 16, value);
    }

    public void set(Matrix2fc value) {
        set(offset, value);
    }

    public void set(Matrix3fc value) {
        set(offset, value);
    }

    public void set(Matrix4fc value) {
        set(offset, value);
    }

    public void set(float x, float y, float z, float w) {
        set(offset, 4, x, y, z, w);
    }

    public void set(float x, float y, float z) {
        set(offset, 4, x, y, z);
    }

    public void set(float x, float y) {
        set(offset, 4, x, y);
    }

    public void set(int u, int v) {
        set(offset, 4, u, v);
    }

    @Override
    public void close() {
        memFree(buffer);
    }
}
