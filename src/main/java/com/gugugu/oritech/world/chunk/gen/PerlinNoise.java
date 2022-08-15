package com.gugugu.oritech.world.chunk.gen;

/**
 * @author theflysong
 * @since 1.0
 */
@Deprecated
public class PerlinNoise {
    private final int seed;
    private static final int MAGIC_A = 874598;
    private static final int MAGIC_B = 4557986;
    private static final int MAGIC_C = 8741232;
    private static final int MAGIC_D = 2134375;
    private static final int MAGIC_E = 9785;
    private static final int MAGIC_F = 3145;
    private static final int MAGIC_G = 2718;
    private static final int UNIT_P = 1000000;
    private static final float UNIT = 1.0f / UNIT_P;

    public PerlinNoise(int seed) {
        this.seed = seed;
    }

    private float noise(int x, int y) {
        int t1 = (x + seed) * (x + MAGIC_A);
        int t2 = (y + seed) * (y + MAGIC_B);
        int t3 = (t1 + seed) * (t1 + MAGIC_C);
        int t4 = (t2 + seed) * (t2 + MAGIC_C);
        int t5 = t3 + t4 + MAGIC_D;
        int t6 = MAGIC_E * t5 * t5 + MAGIC_F * t5 + MAGIC_G;
        long t7 = Integer.toUnsignedLong(t6 * t6) % UNIT_P;
        return t7 * UNIT;
    }

    public float generate(int x, int y) {

        float cornerA = noise(x + 1, y + 1);
        float cornerB = noise(x + 1, y - 1);
        float cornerC = noise(x - 1, y + 1);
        float cornerD = noise(x - 1, y - 1);

        float corners = cornerA + cornerB + cornerC + cornerD;
        corners /= 16;

        float sideA = noise(x, y + 1);
        float sideB = noise(x, y - 1);
        float sideC = noise(x + 1, y);
        float sideD = noise(x - 1, y);
        float sides = sideA + sideB + sideC + sideD;

        sides /= 4;
        float center = noise(x, y) / 4;

        return corners + sides + center;
    }

    public float normalize(float x, float y) {
        int _x = (int) x;
        int _y = (int) y;

        float bx = x - _x;
        float by = y - _y;

        float v1 = generate(_x, _y);
        float v2 = generate(_x + 1, _y);
        float v3 = generate(_x, _y + 1);
        float v4 = generate(_x + 1, _y + 1);

        float i1 = normalizeCos(v1, v2, by);
        float i2 = normalizeCos(v3, v4, bx);

        return normalizeCos(i1, i2, (bx + by) / 2);
    }

    public float normalizeCos(float k1, float k2, float x) {
        float f = (float) (1 - Math.cos(Math.PI * x)) * 0.5f;
        return k1 * (1 - f) + k2 * f;
    }

    private static final float SMOOTH_K = 4.59871f;

    public long randWithin(long x, long y, long maxx) {
        return (long) (normalize(x / SMOOTH_K, y / SMOOTH_K) * maxx);
    }
}
