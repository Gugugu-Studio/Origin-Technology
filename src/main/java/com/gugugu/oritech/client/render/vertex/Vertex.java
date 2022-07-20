package com.gugugu.oritech.client.render.vertex;

import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import java.util.Objects;

/**
 * @author squid233
 * @since 1.0
 */
public class Vertex {
    public float x, y, z;
    public float r, g, b, a;
    public float s, t;
    public float nx, ny, nz;

    public Vertex() {
    }

    public Vertex(float x, float y, float z,
                  float r, float g, float b, float a,
                  float s, float t,
                  float nx, float ny, float nz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.s = s;
        this.t = t;
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
    }

    public Vertex(Vector3fc position, Vector4fc color, Vector2fc texCoords, Vector3fc normal) {
        this(position.x(), position.y(), position.z(),
            color.x(), color.y(), color.z(), color.w(),
            texCoords.x(), texCoords.y(),
            normal.x(), normal.y(), normal.z());
    }

    public Vertex(Vertex vertex) {
        this(vertex.x, vertex.y, vertex.z,
            vertex.r, vertex.g, vertex.b, vertex.a,
            vertex.s, vertex.t,
            vertex.nx, vertex.ny, vertex.nz);
    }

    public Vertex position(Vector3fc position) {
        return position(position.x(), position.y(), position.z());
    }

    public Vertex position(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vertex color(Vector4fc color) {
        return color(color.x(), color.y(), color.z(), color.w());
    }

    public Vertex color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public Vertex color(float r, float g, float b) {
        return color(r, g, b, 1.0f);
    }

    public Vertex texCoords(Vector2fc texCoords) {
        return texCoords(texCoords.x(), texCoords.y());
    }

    public Vertex texCoords(float s, float t) {
        this.s = s;
        this.t = t;
        return this;
    }

    public Vertex normal(Vector3fc normal) {
        return normal(normal.x(), normal.y(), normal.z());
    }

    public Vertex normal(float nx, float ny, float nz) {
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        return this;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public float r() {
        return r;
    }

    public float g() {
        return g;
    }

    public float b() {
        return b;
    }

    public float a() {
        return a;
    }

    public float s() {
        return s;
    }

    public float t() {
        return t;
    }

    public float nx() {
        return nx;
    }

    public float ny() {
        return ny;
    }

    public float nz() {
        return nz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Float.compare(vertex.x, x) == 0 &&
               Float.compare(vertex.y, y) == 0 &&
               Float.compare(vertex.z, z) == 0 &&
               Float.compare(vertex.r, r) == 0 &&
               Float.compare(vertex.g, g) == 0 &&
               Float.compare(vertex.b, b) == 0 &&
               Float.compare(vertex.a, a) == 0 &&
               Float.compare(vertex.s, s) == 0 &&
               Float.compare(vertex.t, t) == 0 &&
               Float.compare(vertex.nx, nx) == 0 &&
               Float.compare(vertex.ny, ny) == 0 &&
               Float.compare(vertex.nz, nz) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, r, g, b, a, s, t, nx, ny, nz);
    }
}
