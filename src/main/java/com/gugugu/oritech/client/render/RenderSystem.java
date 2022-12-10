package com.gugugu.oritech.client.render;

import org.joml.Matrix4fStack;

/**
 * @author squid233
 * @since 1.0.0
 */
public final class RenderSystem {
    private static final Matrix4fStack matrixStack = new Matrix4fStack(64);

    public static Matrix4fStack getMatrixStack() {
        return matrixStack;
    }
}
