package com.gugugu.oritech.client.render;

import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Edge {
    public Vector2f xy;
    public float min_z;
    public float max_z;

    public enum EdgeDir {
        X,
        Y,
        Z
    }

    public EdgeDir dir;
    public static final Edge EMPTY = new Edge(new Vector2f(0, 0), 0, 0, EdgeDir.X);

    public Edge(Vector3f min, Vector3f max) {
        if (min.x != max.x) {
            if (min.y == max.y && min.z == max.z) {
                set(new Vector2f(min.y, min.z), min.x, max.x, EdgeDir.X);
                return;
            }
            set(EMPTY);
            return;
        }
        if (min.y != max.y) {
            if (min.x == max.x && min.z == max.z) {
                set(new Vector2f(min.x, min.z), min.y, max.y, EdgeDir.Y);
                return;
            }
            set(EMPTY);
            return;
        }
        if (min.z != max.z) {
            if (min.x == max.x && min.y == max.y) {
                set(new Vector2f(min.x, min.y), min.z, max.z, EdgeDir.Z);
                return;
            }
            set(EMPTY);
        }
    }

    private void set(Vector2f xy, float min_z, float max_z, EdgeDir dir) {
        this.xy = xy;
        this.min_z = min_z;
        this.max_z = max_z;
        this.dir = dir;
    }

    private void set(Edge e) {
        set(e.xy, e.min_z, e.max_z, e.dir);
    }

    private Edge(Vector2f xy, float min_z, float max_z, EdgeDir dir) {
        set(xy, min_z, max_z, dir);
    }

    private Edge(Edge e) {
        set(e.xy, e.min_z, e.max_z, e.dir);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) {
            return false;
        }
        if (!xy.equals(((Edge) obj).xy)) {
            return false;
        }
        if (min_z != ((Edge) obj).min_z) {
            return false;
        }
        if (max_z != ((Edge) obj).max_z) {
            return false;
        }
        return dir == ((Edge) obj).dir;
    }

    public boolean inline(Edge e) {
        return e.xy.equals(xy) && e.dir == dir;
    }

    public Edge intersection(Edge e) {
        if (inline(e)) {
            float min_z = Math.max(this.min_z, e.min_z);
            float max_z = Math.min(this.max_z, e.max_z);
            if (min_z >= max_z) {
                return EMPTY;
            }
            return new Edge(xy, min_z, max_z, dir);
        }
        return EMPTY;
    }

    public List<Edge> subtract(Edge e) {
        Edge ie = intersection(e);
        if (ie == EMPTY) {
            return List.of(this);
        }
        float min0 = this.min_z;
        float max0 = ie.min_z; // [min0, max0)
        float min1 = ie.max_z;
        float max1 = this.max_z; // (min1, max1]
        List<Edge> edges = new ArrayList<>();
        if (min0 < max0) {
            edges.add(new Edge(xy, min0, max0, dir));
        }
        if (min1 < max1) {
            edges.add(new Edge(xy, min1, max1, dir));
        }
        return edges;
    }

    public static class Line {
        public Vector3f min;
        public Vector3f max;

        public Line(Vector3f min, Vector3f max) {
            this.min = min;
            this.max = max;
        }
    }

    public Line toLine() {
        return switch (dir) {
            case X -> new Line(
                new Vector3f(min_z, xy.x, xy.y),
                new Vector3f(max_z, xy.x, xy.y)
            );
            case Y -> new Line(
                new Vector3f(xy.x, min_z, xy.y),
                new Vector3f(xy.x, max_z, xy.y)
            );
            case Z -> new Line(
                new Vector3f(xy.x, xy.y, min_z),
                new Vector3f(xy.x, xy.y, max_z)
            );
        };
    }
}
