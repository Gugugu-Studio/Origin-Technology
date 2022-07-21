package com.gugugu.oritech.client.render;

import org.lwjgl.opengl.GL15C;

import java.util.function.Consumer;

/**
 * @author squid233
 * @since 1.0
 */
public class Tesselator {
    private static Tesselator instance;
    private Batch batch;

    public static Tesselator getInstance() {
        if (instance == null) {
            instance = new Tesselator();
        }
        return instance;
    }

    public void init() {
        batch = new Batch(GL15C.GL_STREAM_DRAW);
    }

    public Batch batch() {
        return batch;
    }

    public void withBatch(Consumer<Batch> consumer) {
        consumer.accept(batch());
    }

    public void free() {
        if (batch != null) {
            batch.free();
        }
    }
}
