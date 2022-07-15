package com.gugugu.oritech.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Objects;

/**
 * @author squid233
 * @since 1.0
 */
public class Vertex {
    public Vector3f position;
    public Vector4f color;
    public Vector2f texCoords;
    public Vector3f normal;

    public Vertex() {
    }

    public Vertex(Vector3f position, Vector4f color, Vector2f texCoords, Vector3f normal) {
        this.position = position;
        this.color = color;
        this.texCoords = texCoords;
        this.normal = normal;
    }

    public Vertex(Vertex vertex) {
        this(vertex.position, vertex.color, vertex.texCoords, vertex.normal);
    }

    public Vertex position(Vector3f position) {
        this.position = position;
        return this;
    }

    public Vertex position(float x, float y, float z) {
        return position(new Vector3f(x, y, z));
    }

    public Vertex color(Vector4f color) {
        this.color = color;
        return this;
    }

    public Vertex color(float r, float g, float b, float a) {
        return color(new Vector4f(r, g, b, a));
    }

    public Vertex texCoords(Vector2f texCoords) {
        this.texCoords = texCoords;
        return this;
    }

    public Vertex texCoords(float s, float t) {
        return texCoords(new Vector2f(s, t));
    }

    public Vertex normal(Vector3f normal) {
        this.normal = normal;
        return this;
    }

    public Vertex normal(float nx, float ny, float nz) {
        return normal(new Vector3f(nx, ny, nz));
    }

    public float x() {
        return position.x();
    }

    public float y() {
        return position.y();
    }

    public float z() {
        return position.z();
    }

    public float r() {
        return color.x();
    }

    public float g() {
        return color.y();
    }

    public float b() {
        return color.z();
    }

    public float a() {
        return color.w();
    }

    public float s() {
        return texCoords.x();
    }

    public float t() {
        return texCoords.y();
    }

    public float nx() {
        return normal.x();
    }

    public float ny() {
        return normal.y();
    }

    public float nz() {
        return normal.z();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(position, vertex.position) &&
               Objects.equals(color, vertex.color) &&
               Objects.equals(texCoords, vertex.texCoords) &&
               Objects.equals(normal, vertex.normal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, color, texCoords, normal);
    }
}
