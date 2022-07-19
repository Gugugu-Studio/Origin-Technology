package com.gugugu.oritech.util.math;

/**
 * A block pos.
 *
 * @param x pos x
 * @param y pos y
 * @param z pos z
 * @author squid233
 * @since 1.0
 */
public record BlockPos(int x, int y, int z) {
    public BlockPos add(int x, int y, int z) {
        return new BlockPos(x + x(), y + y(), z + z());
    }

    public BlockPos add(BlockPos pos) {
        return add(pos.x(), pos.y(), pos.z());
    }
}
