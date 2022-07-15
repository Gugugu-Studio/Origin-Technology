package com.gugugu.oritech.util;

/**
 * @author squid233
 * @since 1.0
 */
public final class FloatByteUtil {
    public static byte color2byte(float c) {
        return (byte) (c * 255.0f);
    }

    /**
     * Convert color byte to normalized float.
     *
     * @param c the color byte
     * @return the normalized float in range {@code [0..1]}
     */
    public static float byte2color(byte c) {
        return Byte.toUnsignedInt(c) / 255.0f;
    }

    public static byte normal2byte(float n) {
        return (byte) ((255.0f * n - 1.0f) * 0.5f);
    }
}
