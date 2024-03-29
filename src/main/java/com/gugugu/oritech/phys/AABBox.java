/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gugugu.oritech.phys;

import com.gugugu.oritech.util.math.Direction;
import org.joml.Intersectionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import static com.gugugu.oritech.util.math.Direction.*;

/**
 * The axis-aligned bounding box.
 *
 * @author squid233
 * @since 1.0
 */
public class AABBox {
    public final Vector3f min = new Vector3f();
    public final Vector3f max = new Vector3f();

    public AABBox() {
    }

    public AABBox(Vector3fc min,
                  Vector3fc max) {
        this.min.set(min);
        this.max.set(max);
        fix();
    }

    public AABBox(float minX,
                  float minY,
                  float minZ,
                  float maxX,
                  float maxY,
                  float maxZ) {
        min.set(minX, minY, minZ);
        max.set(maxX, maxY, maxZ);
        fix();
    }

    public AABBox(AABBox box) {
        min.set(box.min);
        max.set(box.max);
        fix();
    }

    /**
     * The box consumer.
     *
     * @author squid233
     * @since 0.1.0
     */
    @FunctionalInterface
    public interface BoxConsumer {
        boolean accept(Direction dir,
                       float minX,
                       float minY,
                       float minZ,
                       float maxX,
                       float maxY,
                       float maxZ);
    }

    public boolean isValid() {
        return min.x < max.x && min.y < max.y && min.z < max.z;
    }

    public void fix() {
        if (min.x > max.x) {
            float mx = min.x;
            min.x = max.x;
            max.x = mx;
        }
        if (min.y > max.y) {
            float my = min.y;
            min.y = max.y;
            max.y = my;
        }
        if (min.z > max.z) {
            float mz = min.z;
            min.z = max.z;
            max.z = mz;
        }
    }

    public Direction rayCastFacing(Vector3fc origin, Vector3fc dir) {
        RayCastResult.reset();
        final float epsilon = 0.0001f;
        forEachFace((dir1, minX, minY, minZ, maxX, maxY, maxZ) -> {
            if (Intersectionf.intersectRayAab(origin.x(), origin.y(), origin.z(),
                dir.x(), dir.y(), dir.z(),
                minX - epsilon, minY - epsilon, minZ - epsilon,
                maxX + epsilon, maxY + epsilon, maxZ + epsilon,
                RayCastResult.nearFar) && RayCastResult.nearFar.x < RayCastResult.distance) {
                RayCastResult.distance = RayCastResult.nearFar.x;
                RayCastResult.direction = dir1;
            }
            return true;
        });
        return RayCastResult.direction;
    }

    public AABBox expand(float x, float y, float z) {
        float fx0 = min.x;
        float fy0 = min.y;
        float fz0 = min.z;
        float fx1 = max.x;
        float fy1 = max.y;
        float fz1 = max.z;
        if (x < 0)
            fx0 += x;
        if (x > 0)
            fx1 += x;
        if (y < 0)
            fy0 += y;
        if (y > 0)
            fy1 += y;
        if (z < 0)
            fz0 += z;
        if (z > 0)
            fz1 += z;
        var aabb = new AABBox(fx0, fy0, fz0, fx1, fy1, fz1);
        aabb.fix();
        return aabb;
    }

    public AABBox grow(float x, float y, float z) {
        float fx0 = min.x - x;
        float fy0 = min.y - y;
        float fz0 = min.z - z;
        float fx1 = max.x + x;
        float fy1 = max.y + y;
        float fz1 = max.z + z;
        var aabb = new AABBox(fx0, fy0, fz0, fx1, fy1, fz1);
        aabb.fix();
        return aabb;
    }

    public void forEachEdge(BoxConsumer consumer) {
        boolean c;
        // 12 edges

        // -x
        // [-x..-x], [-y..+y], [-z..-z]
        c = consumer.accept(WEST, min.x, min.y, min.z, min.x, max.y, min.z);
        if (!c) return;
        // [-x..-x], [-y..+y], [+z..+z]
        c = consumer.accept(WEST, min.x, min.y, max.z, min.x, max.y, max.z);
        if (!c) return;
        // [-x..-x], [-y..-y], [-z..+z]
        c = consumer.accept(WEST, min.x, min.y, min.z, min.x, min.y, max.z);
        if (!c) return;
        // [-x..-x], [+y..+y], [-z..+z]
        c = consumer.accept(WEST, min.x, max.y, min.z, min.x, max.y, max.z);
        if (!c) return;

        // +x
        // [+x..+x], [-y..+y], [-z..-z]
        c = consumer.accept(EAST, max.x, min.y, min.z, max.x, max.y, min.z);
        if (!c) return;
        // [+x..+x], [-y..+y], [+z..+z]
        c = consumer.accept(EAST, max.x, min.y, max.z, max.x, max.y, max.z);
        if (!c) return;
        // [+x..+x], [-y..-y], [-z..+z]
        c = consumer.accept(EAST, max.x, min.y, min.z, max.x, min.y, max.z);
        if (!c) return;
        // [+x..+x], [+y..+y], [-z..+z]
        c = consumer.accept(EAST, max.x, max.y, min.z, max.x, max.y, max.z);
        if (!c) return;

        // [-x..+x], [-y..-y], [-z..-z]
        c = consumer.accept(DOWN, min.x, min.y, min.z, max.x, min.y, min.z);
        if (!c) return;
        // [-x..+x], [-y..-y], [+z..+z]
        c = consumer.accept(DOWN, min.x, min.y, max.z, max.x, min.y, max.z);
        if (!c) return;
        // [-x..+x], [+y..+y], [+z..+z]
        c = consumer.accept(UP, min.x, max.y, max.z, max.x, max.y, max.z);
        if (!c) return;
        // [-x..+x], [+y..+y], [-z..-z]
        consumer.accept(UP, min.x, max.y, min.z, max.x, max.y, min.z);
    }

    public void forEachFace(BoxConsumer consumer) {
        // 6 faces

        for (var dir : Direction.values()) {
            boolean c = consumer.accept(dir,
                dir == EAST ? max.x : min.x,
                dir == UP ? max.y : min.y,
                dir == SOUTH ? max.z : min.z,
                dir == WEST ? min.x : max.x,
                dir == DOWN ? min.y : max.y,
                dir == NORTH ? min.z : max.z);
            if (!c) break;
        }
    }

    public float clipXCollide(AABBox other, float dt) {
        // Pass
        if (other.max.y <= min.y || other.min.y >= max.y || other.max.z <= min.z || other.min.z >= max.z)
            return dt;
        float clip;
        if (dt > 0 && other.max.x <= min.x) {
            clip = min.x - other.max.x;
            if (clip < dt)
                dt = clip;
        }
        if (dt < 0 && other.min.x >= max.x) {
            clip = max.x - other.min.x;
            if (clip > dt)
                dt = clip;
        }
        return dt;
    }

    public float clipYCollide(AABBox other, float dt) {
        // Pass
        if (other.max.x <= min.x || other.min.x >= max.x || other.max.z <= min.z || other.min.z >= max.z)
            return dt;
        float clip;
        if (dt > 0 && other.max.y <= min.y) {
            clip = min.y - other.max.y;
            if (clip < dt)
                dt = clip;
        }
        if (dt < 0 && other.min.y >= max.y) {
            clip = max.y - other.min.y;
            if (clip > dt)
                dt = clip;
        }
        return dt;
    }

    public float clipZCollide(AABBox other, float dt) {
        // Pass
        if (other.max.x <= min.x || other.min.x >= max.x || other.max.y <= min.y || other.min.y >= max.y)
            return dt;
        float clip;
        if (dt > 0 && other.max.z <= min.z) {
            clip = min.z - other.max.z;
            if (clip < dt)
                dt = clip;
        }
        if (dt < 0 && other.min.z >= max.z) {
            clip = max.z - other.min.z;
            if (clip > dt)
                dt = clip;
        }
        return dt;
    }

    public AABBox move(float x, float y, float z) {
        min.add(x, y, z);
        max.add(x, y, z);
        fix();
        return this;
    }
}
