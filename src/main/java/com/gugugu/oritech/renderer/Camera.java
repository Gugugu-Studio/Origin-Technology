package com.gugugu.oritech.renderer;

import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.math.Numbers;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.joml.Math.*;

/**
 * @author squid233
 * @author theflysong
 * @since 1.0
 */
public class Camera {
    private final Vector3f position = new Vector3f();
    private final Vector3f front = new Vector3f(0, 0, -1);
    private final Vector3f up = new Vector3f(0, 1, 0);
    private final Vector3f eulerAngle = new Vector3f();
    private final Vector3f resultPosition = new Vector3f();
    private final Quaternionf orientation = new Quaternionf();


    public Camera() {
    }

    public void move(float dt, Direction direction) {
        switch (direction) {
            case WEST -> position.x -= dt;
            case EAST -> position.x += dt;
            case DOWN -> position.y -= dt;
            case UP -> position.y += dt;
            case NORTH -> position.z -= dt;
            case SOUTH -> position.z += dt;
        }
    }

    public void moveRelative(float dt, Direction direction) {
        switch (direction) {
            case WEST -> {
                // Cross product
                float cx = front.y * up.z - front.z * up.y;
                float cz = front.x * up.y - front.y * up.x;
                // Normalize
                float length = invsqrt(fma(cx, cx, cz * cz));
                cx *= length;
                cz *= length;
                position.x -= cx * dt;
                position.z -= cz * dt;
            }
            case EAST -> {
                // Cross product
                float cx = front.y * up.z - front.z * up.y;
                float cz = front.x * up.y - front.y * up.x;
                // Normalize
                float length = invsqrt(fma(cx, cx, cz * cz));
                cx *= length;
                cz *= length;
                position.x += cx * dt;
                position.z += cz * dt;
            }
            case DOWN -> position.y -= dt;
            case UP -> position.y += dt;
            case NORTH -> {
                // Normalize
                float length = invsqrt(fma(front.x, front.x, front.z * front.z));
                float fx = front.x * length;
                float fz = front.z * length;
                position.x += dt * fx;
                position.z += dt * fz;
            }
            case SOUTH -> {
                // Normalize
                float length = invsqrt(fma(front.x, front.x, front.z * front.z));
                float fx = front.x * length;
                float fz = front.z * length;
                position.x -= dt * fx;
                position.z -= dt * fz;
            }
        }
    }

    public void moveRelative(float dx, float dy, float dz) {
        if (Numbers.isNonZero(dz)) {
            // Normalize
            float length = invsqrt(fma(front.x, front.x, front.z * front.z));
            float fx = front.x * length;
            float fz = front.z * length;
            position.x -= dz * fx;
            position.z -= dz * fz;
        }

        if (Numbers.isNonZero(dx)) {
            // Cross product
            float cx = front.y * up.z - front.z * up.y;
            float cz = front.x * up.y - front.y * up.x;
            // Normalize
            float length = invsqrt(fma(cx, cx, cz * cz));
            cx *= length;
            cz *= length;
            position.x += cx * dx;
            position.z += cz * dx;
        }

        position.y += dy;
    }

    public void moveRelative(float dx, float dy, float dz, float speed) {
        moveRelative(dx * speed, dy * speed, dz * speed);
    }

    public Vector3f getFrontVec() {
        orientation.getEulerAnglesXYZ(eulerAngle);
        float pitch = eulerAngle.x;
        float sinPitch = sin(pitch);
        float cosPitch = cosFromSin(sinPitch, pitch);
        float yaw = eulerAngle.y;
        float sinYaw = sin(yaw);
        float cosYaw = cosFromSin(sinYaw, yaw);
        front.x = cosPitch * cosYaw;
        front.y = sinPitch;
        front.z = cosPitch * sinYaw;
        return front.normalize();
    }

    public Matrix4f apply(Matrix4f matrix) {
        return matrix.lookAt(position,
            position.add(getFrontVec(), resultPosition),
            up);
    }
}
