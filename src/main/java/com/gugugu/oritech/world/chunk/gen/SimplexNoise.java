package com.gugugu.oritech.world.chunk.gen;

public class SimplexNoise {
    private final int seed;

    public SimplexNoise(int seed) {
        this.seed = seed;
    }
    private static final float SMOOTH_K = 64;

    public long randWithin(long x, long y, long maxx) {
        return (long)(org.joml.SimplexNoise.noise(x / SMOOTH_K, y / SMOOTH_K, seed) * maxx);
    }
}
