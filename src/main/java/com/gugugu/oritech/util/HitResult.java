package com.gugugu.oritech.util;

import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.block.BlockState;

/**
 * The hit result.
 *
 * @param state The target block state.
 * @param x     The pos x.
 * @param y     The pos y.
 * @param z     The pos z.
 * @param face  The hit face.
 * @author squid233
 * @since 1.0
 */
public record HitResult(BlockState state, int x, int y, int z, Direction face) {
}
