package com.gugugu.oritech.util.math;

import org.joml.*;

/**
 * @author squid233
 * @since 1.0
 */
public class Transformation {
    private final Vector3f position;
    private final Quaternionf orientation;
    private final Vector3f translation;
    private final Vector3f scaling;

    public Transformation(Vector3f position,
                          Quaternionf orientation,
                          Vector3f translation,
                          Vector3f scaling) {
        this.position = position;
        this.orientation = orientation;
        this.translation = translation;
        this.scaling = scaling;
    }

    public Transformation() {
        this(new Vector3f(), new Quaternionf(), new Vector3f(), new Vector3f(1.0f));
    }

    public Vector3f position() {
        return position;
    }

    public Transformation setPosition(Vector3fc pos) {
        position.set(pos);
        return this;
    }

    public Transformation setPosition(float x, float y, float z) {
        position.set(x, y, z);
        return this;
    }

    public Transformation addPosition(Vector3fc pos) {
        position.add(pos);
        return this;
    }

    public Transformation addPosition(float x, float y, float z) {
        position.add(x, y, z);
        return this;
    }

    public Transformation subPosition(Vector3fc pos) {
        position.sub(pos);
        return this;
    }

    public Transformation subPosition(float x, float y, float z) {
        position.sub(x, y, z);
        return this;
    }

    public Quaternionf orientation() {
        return orientation;
    }

    public Transformation setOrientation(Quaternionfc ori) {
        orientation.set(ori);
        return this;
    }

    public Transformation setOrientation(float x, float y, float z, float w) {
        orientation.set(x, y, z, w);
        return this;
    }

    public Transformation fromAxisAngleRad(Vector3fc axis, float angle) {
        orientation.fromAxisAngleRad(axis, angle);
        return this;
    }

    public Transformation fromAxisAngleRad(float axisX, float axisY, float axisZ, float angle) {
        orientation.fromAxisAngleRad(axisX, axisY, axisZ, angle);
        return this;
    }

    public Transformation fromAxisAngleDeg(Vector3fc axis, float angle) {
        orientation.fromAxisAngleDeg(axis, angle);
        return this;
    }

    public Transformation fromAxisAngleDeg(float axisX, float axisY, float axisZ, float angle) {
        orientation.fromAxisAngleDeg(axisX, axisY, axisZ, angle);
        return this;
    }

    public Transformation rotateAxis(float angle, Vector3fc axis) {
        orientation.rotateAxis(angle, axis);
        return this;
    }

    public Transformation rotateAxis(float angle, float axisX, float axisY, float axisZ) {
        orientation.rotateAxis(angle, axisX, axisY, axisZ);
        return this;
    }

    public Transformation rotateX(float angle) {
        orientation.rotateX(angle);
        return this;
    }

    public Transformation rotateY(float angle) {
        orientation.rotateY(angle);
        return this;
    }

    public Transformation rotateZ(float angle) {
        orientation.rotateZ(angle);
        return this;
    }

    public Transformation rotateXYZ(float angleX, float angleY, float angleZ) {
        orientation.rotateXYZ(angleX, angleY, angleZ);
        return this;
    }

    public Transformation rotateYXZ(float angleY, float angleX, float angleZ) {
        orientation.rotateYXZ(angleY, angleX, angleZ);
        return this;
    }

    public Transformation rotateZYX(float angleZ, float angleY, float angleX) {
        orientation.rotateZYX(angleZ, angleY, angleX);
        return this;
    }

    public Transformation rotationAxis(AxisAngle4f axisAngle) {
        orientation.rotationAxis(axisAngle);
        return this;
    }

    public Transformation rotationAxis(float angle, Vector3fc axis) {
        orientation.rotationAxis(angle, axis);
        return this;
    }

    public Transformation rotationAxis(float angle, float axisX, float axisY, float axisZ) {
        orientation.rotationAxis(angle, axisX, axisY, axisZ);
        return this;
    }

    public Transformation rotationX(float angle) {
        orientation.rotationX(angle);
        return this;
    }

    public Transformation rotationY(float angle) {
        orientation.rotationY(angle);
        return this;
    }

    public Transformation rotationZ(float angle) {
        orientation.rotationZ(angle);
        return this;
    }

    public Transformation rotationXYZ(float angleX, float angleY, float angleZ) {
        orientation.rotationXYZ(angleX, angleY, angleZ);
        return this;
    }

    public Transformation rotationYXZ(float angleY, float angleX, float angleZ) {
        orientation.rotationYXZ(angleY, angleX, angleZ);
        return this;
    }

    public Transformation rotationZYX(float angleZ, float angleY, float angleX) {
        orientation.rotationZYX(angleZ, angleY, angleX);
        return this;
    }

    public Vector3f translation() {
        return translation;
    }

    public Transformation setTranslation(Vector3fc tra) {
        translation.set(tra);
        return this;
    }

    public Transformation setTranslation(float x, float y, float z) {
        translation.set(x, y, z);
        return this;
    }

    public Transformation addTranslation(Vector3fc tra) {
        translation.add(tra);
        return this;
    }

    public Transformation addTranslation(float x, float y, float z) {
        translation.add(x, y, z);
        return this;
    }

    public Transformation subTranslation(Vector3fc tra) {
        translation.sub(tra);
        return this;
    }

    public Transformation subTranslation(float x, float y, float z) {
        translation.sub(x, y, z);
        return this;
    }

    public Vector3f scaling() {
        return scaling;
    }

    public Transformation setScaling(Vector3fc sca) {
        scaling.set(sca);
        return this;
    }

    public Transformation setScaling(float x, float y, float z) {
        scaling.set(x, y, z);
        return this;
    }

    public <T extends Matrix4f> T apply(T matrix) {
        matrix.translate(position())
            .rotate(orientation())
            .translate(translation())
            .scale(scaling());
        return matrix;
    }
}
