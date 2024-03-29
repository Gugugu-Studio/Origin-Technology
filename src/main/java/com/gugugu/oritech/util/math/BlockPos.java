package com.gugugu.oritech.util.math;

import com.gugugu.oritech.world.World;

/**
 * A block pos. Relative range [0..16)
 *
 * @param x pos x
 * @param y pos y
 * @param z pos z
 * @author squid233
 * @since 1.0
 */
public record BlockPos(int x, int y, int z) {
    private static final int SIZE_BITS_X = 1 + Numbers.floorLog2(Numbers.toPoT(World.LIMIT));
    private static final int SIZE_BITS_Z = SIZE_BITS_X;
    public static final int SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z;
    private static final long BITS_X = (1L << SIZE_BITS_X) - 1L;
    private static final long BITS_Y = (1L << SIZE_BITS_Y) - 1L;
    private static final long BITS_Z = (1L << SIZE_BITS_Z) - 1L;
    private static final int BIT_SHIFT_Z = SIZE_BITS_Y;
    private static final int BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z;

    public static long toLong(int x, int y, int z) {
        long l = 0L;
        l |= ((long) x & BITS_X) << BIT_SHIFT_X;
        l |= ((long) y & BITS_Y);
        l |= ((long) z & BITS_Z) << BIT_SHIFT_Z;
        return l;
    }

    public static int unpackLongX(long packedPos) {
        return (int) (packedPos << 64 - BIT_SHIFT_X - SIZE_BITS_X >> 64 - SIZE_BITS_X);
    }

    public static int unpackLongY(long packedPos) {
        return (int) (packedPos << 64 - SIZE_BITS_Y >> 64 - SIZE_BITS_Y);
    }

    public static int unpackLongZ(long packedPos) {
        return (int) (packedPos << 64 - BIT_SHIFT_Z - SIZE_BITS_Z >> 64 - SIZE_BITS_Z);
    }

    public static BlockPos fromLong(long packedPos) {
        return new BlockPos(unpackLongX(packedPos),
            unpackLongY(packedPos),
            unpackLongZ(packedPos));
    }

    public BlockPos add(int x, int y, int z) {
        return new BlockPos(x + x(), y + y(), z + z());
    }

    public BlockPos add(BlockPos pos) {
        return add(pos.x(), pos.y(), pos.z());
    }

    public long toLong() {
        return toLong(x, y, z);
    }
}
