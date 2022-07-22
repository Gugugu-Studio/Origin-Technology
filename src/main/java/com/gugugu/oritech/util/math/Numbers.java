package com.gugugu.oritech.util.math;

import org.joml.Quaternionf;
import org.joml.Vector3fc;

import static org.joml.Math.*;

/**
 * @author Overrun Organization
 * @author squid233
 * @since 1.0
 */
public class Numbers {
    /**
     * The angles in radians for 90°.
     */
    public static final float RAD90F = toRadians(90.0f);
    /**
     * The angles in radians for 180°.
     */
    public static final float RAD180F = toRadians(180.0f);
    /**
     * The angles in radians for 360°.
     */
    public static final float RAD360F = toRadians(360.0f);
    /**
     * The epsilon of single float point.
     * <p>
     * Writing in plain format: {@code 0.000001}
     * </p>
     */
    public static final float EPSILON_SINGLE = 1.0E-06f;
    /**
     * The epsilon of double float point.
     * <p>
     * Writing in plain format: {@code 0.000000000000001}
     * </p>
     */
    public static final double EPSILON_DOUBLE = 1.0E-15;
    /**
     * <code>log<sub>e</sub>2</code>
     *
     * @since 0.2.0
     */
    public static final double LN2 = Math.log(2);
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = {
        0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8,
        31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
    };

    /**
     * Check if two single float point numbers are equal.
     * <p>
     * Checking method: {@code |a-b| &lt; 10<sup>-6</sup>}
     * </p>
     *
     * @param a The first number.
     * @param b The second number.
     * @return The result.
     */
    public static boolean isEqual(float a, float b) {
        return Math.abs(a - b) < EPSILON_SINGLE;
    }

    /**
     * Check if two single float point numbers are non-equal.
     * <p>
     * Checking method: {@code |a-b| &gt;= 10<sup>-6</sup>}
     * </p>
     *
     * @param a The first number.
     * @param b The second number.
     * @return The result.
     */
    public static boolean isNonEqual(float a, float b) {
        return Math.abs(a - b) >= EPSILON_SINGLE;
    }

    /**
     * Check if two double float point numbers are equal.
     * <p>
     * Checking method: {@code |a-b| &lt; 10<sup>-15</sup>}
     * </p>
     *
     * @param a The first number.
     * @param b The second number.
     * @return The result.
     */
    public static boolean isEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON_DOUBLE;
    }

    /**
     * Check if two single double point numbers are non-equal.
     * <p>
     * Checking method: {@code |a-b| &gt;= 10<sup>-15</sup>}
     * </p>
     *
     * @param a The first number.
     * @param b The second number.
     * @return The result.
     */
    public static boolean isNonEqual(double a, double b) {
        return Math.abs(a - b) >= EPSILON_DOUBLE;
    }

    /**
     * Check if the number is 0.
     *
     * @param a The number.
     * @return Is zero
     */
    public static boolean isZero(float a) {
        return isEqual(a, 0.0f);
    }

    /**
     * Check if the number is not 0.
     *
     * @param a The number.
     * @return Is non-zero
     */
    public static boolean isNonZero(float a) {
        return isNonEqual(a, 0.0f);
    }

    /**
     * Check if the number is an even number.
     *
     * @param a The number.
     * @return Is an even number
     */
    public static boolean isEven(int a) {
        return (a & 1) == 0;
    }

    /**
     * Check if the number is an even number.
     *
     * @param a The number
     * @throws IllegalArgumentException If {@code a} is an odd number
     */
    public static void checkEven(int a) throws IllegalArgumentException {
        // Check if even
        if (Numbers.isOdd(a))
            throw new IllegalArgumentException("The kvs length must be an even number! Got: " + a + ".");
    }

    /**
     * Check if the number is an odd number.
     *
     * @param a The number.
     * @return Is an odd number
     */
    public static boolean isOdd(int a) {
        return (a & 1) != 0;
    }

    /**
     * Check if the number is power of 2.
     *
     * @param a The number.
     * @return Is the number power of 2
     */
    public static boolean isPoT(int a) {
        return a > 0 && (a & (a - 1)) == 0;
    }

    /**
     * Convert {@code a} to a number that is power of 2.
     *
     * @param a a value
     * @return the result
     */
    public static int toPoT(int a) {
        --a;
        a |= a >> 1;
        a |= a >> 2;
        a |= a >> 4;
        a |= a >> 8;
        a |= a >> 16;
        return a + 1;
    }

    /**
     * <code>log<sub>a</sub>n</code>
     *
     * @param a a
     * @param n n
     * @return <code>ln(n) / ln(a)</code>
     */
    public static double log(double a, double n) {
        return Math.log(n) / Math.log(a);
    }

    /**
     * <code>log<sub>2</sub>n</code>
     *
     * @param a a
     * @return <code>ln(a) / ln2</code>
     */
    public static double log2(double a) {
        return Math.log(a) / LN2;
    }

    public static int ceilLog2(int value) {
        value = isPoT(value) ? value : toPoT(value);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) value * 125613361L >> 27) & 31];
    }

    public static int floorLog2(int value) {
        return ceilLog2(value) - (isPoT(value) ? 0 : 1);
    }

    public static Quaternionf quatFromEulerAngle(Vector3fc eulerAngle,
                                                 Quaternionf dst) {
        float sx = sin(eulerAngle.x() * 0.5f),
            sy = sin(eulerAngle.y() * 0.5f),
            sz = sin(eulerAngle.z() * 0.5f),
            cx = cosFromSin(sx, eulerAngle.x() * 0.5f),
            cy = cosFromSin(sy, eulerAngle.y() * 0.5f),
            cz = cosFromSin(sz, eulerAngle.z() * 0.5f);
        dst.x = sx * cy * cz - cx * sy * sz;
        dst.y = cx * sy * cz + sx * cy * sz;
        dst.z = cx * cy * sz - sx * sy * cz;
        dst.w = cx * cy * cz + sx * sy * sz;
        return dst;
    }
}
