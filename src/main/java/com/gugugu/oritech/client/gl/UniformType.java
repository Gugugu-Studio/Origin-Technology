package com.gugugu.oritech.client.gl;

import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public enum UniformType {
    I1(4),
    F4(16),
    MAT_F4(64);

    public final int length;

    UniformType(int length) {
        this.length = length;
    }
}
