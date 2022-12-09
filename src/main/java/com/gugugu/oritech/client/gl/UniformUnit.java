package com.gugugu.oritech.client.gl;

import org.joml.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL31C.*;
import static org.lwjgl.system.MemoryUtil.*;

public record UniformUnit(int offset, int size, int ubo) {
    public void set(int offset, int size, ByteBuffer buffer) {
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferSubData(GL_UNIFORM_BUFFER, offset, buffer);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    private ByteBuffer malloc(int size) {
        return memAlloc(size).order(ByteOrder.LITTLE_ENDIAN);
    }

    private void free(ByteBuffer buf) {
        memFree(buf);
    }

    public void set(int offset, int size, int value) {
        ByteBuffer buf = malloc(size);
        set(offset, size, buf.putInt(value).flip());
        free(buf);
    }

    public void set(int offset, int size, float value) {
        ByteBuffer buf = malloc(size);
        set(offset, size, buf.putFloat(value).flip());
        free(buf);
    }

    public void set(int offset, int size, boolean value) {
        ByteBuffer buf = malloc(size);
        set(offset, size, buf.putInt(value ? 1 : 0).flip());
        free(buf);
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
        ByteBuffer buf = malloc(eleSize * 4);
        set(offset, eleSize * 4, value.get(buf).flip());
        free(buf);
    }

    public void set(int offset, int eleSize, Vector3fc value) {
        ByteBuffer buf = malloc(eleSize * 4);
        set(offset, eleSize * 4, value.get(ByteBuffer.allocateDirect(eleSize * 4)).flip());
        free(buf);
    }

    public void set(int offset, int eleSize, Vector2fc value) {
        ByteBuffer buf = malloc(eleSize * 2);
        set(offset, eleSize * 2, value.get(ByteBuffer.allocateDirect(eleSize * 2)).flip());
        free(buf);
    }

    public void set(int offset, int eleSize, Vector4ic value) {
        ByteBuffer buf = malloc(eleSize * 4);
        set(offset, eleSize * 4, value.get(ByteBuffer.allocateDirect(eleSize * 4)).flip());
        free(buf);
    }

    public void set(int offset, int eleSize, Vector3ic value) {
        ByteBuffer buf = malloc(eleSize * 4);
        set(offset, eleSize * 4, value.get(ByteBuffer.allocateDirect(eleSize * 4)).flip());
        free(buf);
    }

    public void set(int offset, int eleSize, Vector2ic value) {
        ByteBuffer buf = malloc(eleSize * 2);
        set(offset, eleSize * 2, value.get(ByteBuffer.allocateDirect(eleSize * 2)).flip());
        free(buf);
    }

    public void set(int offset, Matrix2fc value) {
        Vector2f col = new Vector2f();
        int off = offset;
        for (int i = 0 ; i < 2 ; i  ++) {
            set(off, 16, value.getColumn(i, col));
            off += 16;
        }
    }

    public void set(int offset, Matrix3fc value) {
        Vector3f col = new Vector3f();
        int off = offset;
        for (int i = 0 ; i < 3 ; i  ++) {
            set(off, 16, value.getColumn(i, col));
            off += 16;
        }
    }

    public void set(int offset, Matrix4fc value) {
        Vector4f col = new Vector4f();
        int off = offset;
        for (int i = 0 ; i < 4 ; i  ++) {
            set(off, 16, value.getColumn(i, col));
            off += 16;
        }
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
}
