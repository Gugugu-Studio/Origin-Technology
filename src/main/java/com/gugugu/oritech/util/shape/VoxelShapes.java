package com.gugugu.oritech.util.shape;

import com.gugugu.oritech.phys.AABBox;

import java.util.List;

/**
 * The voxel shapes.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class VoxelShapes {
    private static final List<AABBox> FULL_CUBE = List.of(new AABBox(
        0.0f, 0.0f, 0.0f,
        1.0f, 1.0f, 1.0f
    ));

    public static List<AABBox> empty() {
        return null;
    }

    public static List<AABBox> fullCube() {
        return FULL_CUBE;
    }

    public static List<AABBox> combine(AABBox... boxes) {
        return List.of(boxes);
    }
}
