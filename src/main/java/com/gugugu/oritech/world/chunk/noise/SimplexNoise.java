package com.gugugu.oritech.world.chunk.noise;

public class SimplexNoise {
    private final long seed;

    public SimplexNoise(long seed) {
        this.seed = seed;
    }
    private static final float SMOOTH_K = 64;

    public long randWithin(long x, long y, long maxx) {
        float t1 = seed / 1048576.0f / 1048576.0f;
        long _t2 = (seed / 1048576) % 1048576;
        float t2 = _t2 * 1.0f;
        float t3 = seed % 1048576;
        float _ret = org.joml.SimplexNoise.noise(x / SMOOTH_K + t1, y / SMOOTH_K + t1, t2, t3);
        long result = (long)((_ret + 1) / 2 * maxx);
        return Math.max(result, 0);
    }
}
