package com.gugugu.oritech.client.gl;

/**
 * @author squid233
 * @since 1.0
 */
public enum UniformType {
    I1(4),
    F4(16),
    MAT_F4(64);

    public final int length;

    UniformType(int length) {
        this.length = length;
    }
}
