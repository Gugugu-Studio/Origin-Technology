package com.gugugu.oritech.world.chunk.gen;

import org.joml.SimplexNoise;

/**
 * @author squid233
 * @since 1.0
 */
public class TerrainNoise {
    public static float sumOcatave(int numIterations,
                                   float x, float y,
                                   float persistence,
                                   float scale,
                                   float low, float high) {
        float maxAmp = 0;
        float amp = 1;
        float freq = scale;
        float noise = 0;

        // add successively smaller, higher-frequency terms
        for (int i = 0; i < numIterations; i++) {
            noise += SimplexNoise.noise(x * freq, y * freq) * amp;
            maxAmp += amp;
            amp *= persistence;
            freq *= 2;
        }

        // take the average value of the iterations
        noise /= maxAmp;

        //normalize the result
        noise = noise * (high - low) * 0.5f + (high + low) * 0.5f;

        return noise;
    }
}
