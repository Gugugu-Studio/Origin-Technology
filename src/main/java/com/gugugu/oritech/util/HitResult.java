package com.gugugu.oritech.util;

import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.block.Block;

/**
 * The hit result.
 *
 * @param block The target block.
 * @param x     The pos x.
 * @param y     The pos y.
 * @param z     The pos z.
 * @param face  The hit face.
 * @author squid233
 * @since 1.0
 */
public record HitResult(Block block, int x, int y, int z, Direction face) {
}
