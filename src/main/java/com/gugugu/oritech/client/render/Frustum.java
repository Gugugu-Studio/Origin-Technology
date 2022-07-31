package com.gugugu.oritech.client.render;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import org.joml.FrustumIntersection;
import org.joml.Matrix4fc;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class Frustum {
    private static final FrustumIntersection frustum = new FrustumIntersection();

    public static void update(Matrix4fc m) {
        frustum.set(m);
    }

    public static boolean test(float minX, float minY, float minZ,
                               float maxX, float maxY, float maxZ) {
        return frustum.testAab(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static boolean test(AABBox box) {
        return frustum.testAab(box.min, box.max);
    }
}
