package com.gugugu.oritech.renderer;

/**
 * @author squid233
 * @since 1.0
 */
public enum UniformType {
    F4(16),
    MAT_F4(64);

    public final int length;

    UniformType(int length) {
        this.length = length;
    }
}
