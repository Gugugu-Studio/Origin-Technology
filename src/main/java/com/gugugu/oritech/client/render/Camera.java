package com.gugugu.oritech.client.render;

import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.joml.Math.*;

/**
 * @author squid233
 * @author theflysong
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class Camera {
    public static final float YAW = -90.0f;
    public static final float PITCH = 0.0f;

    public final Vector3f prevPosition = new Vector3f();
    public final Vector3f position = new Vector3f();
    public final Vector3f lerpPosition = new Vector3f();
    public final Vector3f resultPosition = new Vector3f();
    public final Vector3f front;
    public final Vector3f up = new Vector3f();
    public final Vector3f right = new Vector3f();
    public final Vector3f worldUp;
    /**
     * Euler angles
     */
    public float yaw, pitch;
    public float smoothStep = -1.0f;

    public Camera() {
        front = new Vector3f(0.0f, 0.0f, -1.0f);
        worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        yaw = YAW;
        pitch = PITCH;
        updateCameraVec();
    }

    public void moveRelative(Direction direction, float speed) {
        switch (direction) {
            case NORTH -> {
                float scalar = invsqrt(fma(front.x(), front.x(), front.z() * front.z())) * speed;
                position.add(front.x() * scalar,
                    0.0f,
                    front.z() * scalar);
            }
            case SOUTH -> {
                float scalar = invsqrt(fma(front.x(), front.x(), front.z() * front.z())) * speed;
                position.sub(front.x() * scalar,
                    0.0f,
                    front.z() * scalar);
            }
            case WEST -> {
                float scalar = invsqrt(fma(right.x(), right.x(), right.z() * right.z())) * speed;
                position.sub(right.x() * scalar,
                    0.0f,
                    right.z() * scalar);
            }
            case EAST -> {
                float scalar = invsqrt(fma(right.x(), right.x(), right.z() * right.z())) * speed;
                position.add(right.x() * scalar,
                    0.0f,
                    right.z() * scalar);
            }
            case UP -> position.y += speed;
            case DOWN -> position.y -= speed;
        }
    }

    public void moveRelative(float xa, float ya, float za) {
        moveRelative(Direction.EAST, xa);
        moveRelative(Direction.UP, ya);
        moveRelative(Direction.SOUTH, za);
    }

    public void moveRelative(float xa, float ya, float za, float speed) {
        moveRelative(xa * speed, ya * speed, za * speed);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void setRotation(float xo, float yo, boolean constrainPitch) {
        yaw = xo;
        pitch = yo;
        // make sure that when pitch is out of bounds, screen doesn't get flipped
        if (constrainPitch) {
            if (pitch > 89.5f) {
                pitch = 89.5f;
            }
            if (pitch < -89.5f) {
                pitch = -89.5f;
            }
        }
        // update front, right and up vectors using the updated euler angles
        updateCameraVec();
    }

    public void setRotation(float xo, float yo) {
        setRotation(xo, yo, true);
    }

    public void rotate(float xo, float yo, boolean constrainPitch) {
        setRotation(yaw + xo, pitch + yo, constrainPitch);
    }

    public void rotate(float xo, float yo) {
        rotate(xo, yo, true);
    }

    public void resetYawPitch() {
        yaw = YAW;
        pitch = PITCH;
    }

    public void update() {
        prevPosition.set(position);
    }

    private void updateCameraVec() {
        // calculate the new front vector
        final float yawRad = toRadians(yaw);
        final float pitchRad = toRadians(pitch);
        final float yawSin = sin(yawRad), yawCos = cosFromSin(yawSin, yawRad);
        final float pitchSin = sin(pitchRad), pitchCos = cosFromSin(pitchSin, pitchRad);
        front.x = yawCos * pitchCos;
        front.y = pitchSin;
        front.z = yawSin * pitchCos;
        front.normalize();
        // also re-calculate the right and up vector
        // normalize the vectors, because their length gets closer to 0 the more you look up or down which results in slower movement.
        front.cross(worldUp, right).normalize();
        right.cross(front, up).normalize();
    }

    public Matrix4f apply(Matrix4f matrix) {
        Vector3f pos = smoothStep >= 0.0f ?
            prevPosition.lerp(position, smoothStep, lerpPosition) :
            position;
        return matrix.lookAt(pos,
            pos.add(front, resultPosition),
            up);
    }
}
