package com.gugugu.oritech.client.gl;

import com.gugugu.oritech.util.FloatByteUtil;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author squid233
 * @since 1.0
 */
public enum DataType {
    BYTE(1) {
        @Override
        public void asWrite(ByteBuffer buffer, float value) {
            buffer.put(FloatByteUtil.normal2byte(value));
        }
    },
    UNSIGNED_BYTE(1) {
        @Override
        public void asWrite(ByteBuffer buffer, float value) {
            buffer.put(FloatByteUtil.color2byte(value));
        }
    },
    FLOAT(4) {
        @Override
        public void asWrite(ByteBuffer buffer, float value) {
            buffer.putFloat(value);
        }
    };

    public final int length;

    DataType(int length) {
        this.length = length;
    }

    public DataType[] repeat(int count) {
        if (count == 1) {
            return new DataType[]{this};
        }
        DataType[] types = new DataType[count];
        Arrays.fill(types, this);
        return types;
    }

    public abstract void asWrite(ByteBuffer buffer, float value);

    public void asWrite(ByteBuffer buffer, float[] values) {
        for (float value : values) {
            asWrite(buffer, value);
        }
    }

    public void asWrite(ByteBuffer buffer, float x, float y) {
        asWrite(buffer, x);
        asWrite(buffer, y);
    }

    public void asWrite(ByteBuffer buffer, Vector2fc values) {
        asWrite(buffer, values.x(), values.y());
    }

    public void asWrite(ByteBuffer buffer, float x, float y, float z) {
        asWrite(buffer, x);
        asWrite(buffer, y);
        asWrite(buffer, z);
    }

    public void asWrite(ByteBuffer buffer, Vector3fc values) {
        asWrite(buffer, values.x(), values.y(), values.z());
    }

    public void asWrite(ByteBuffer buffer, float x, float y, float z, float w) {
        asWrite(buffer, x);
        asWrite(buffer, y);
        asWrite(buffer, z);
        asWrite(buffer, w);
    }

    public void asWrite(ByteBuffer buffer, Vector4fc values) {
        asWrite(buffer, values.x(), values.y(), values.z(), values.w());
    }
}
