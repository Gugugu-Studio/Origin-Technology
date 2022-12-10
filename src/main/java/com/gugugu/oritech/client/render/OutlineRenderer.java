package com.gugugu.oritech.client.render;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.HitResult;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.*;

@SideOnly(Side.CLIENT)
public class OutlineRenderer {
    public static List<Edge> allEdges = new ArrayList<>();
    public static List<Edge> renderEdges = new ArrayList<>();

    private static void cullEdge(Edge e) {
        List<Edge> _renderEdges = new ArrayList<>();
        for (Edge re : renderEdges) {
            _renderEdges.addAll(re.subtract(e));
        }
        renderEdges = _renderEdges;
    }

    private static void addEdge(Edge e) {
        renderEdges.add(e);
        for (Edge ae : allEdges) {
            Edge ie = ae.intersection(e);
            if (ie != Edge.EMPTY) {
                cullEdge(ie);
            }
        }
        allEdges.add(e);
    }

    private static void collectEdges(List<AABBox> outlines, int x, int y, int z) {
        allEdges.clear();
        renderEdges.clear();
        if (outlines == null) {
            return;
        }
        for (AABBox outline : outlines) {
            new AABBox(outline)
                    .move(x, y, z)
                    .forEachEdge((dir, minX, minY, minZ, maxX, maxY, maxZ) -> {
                        addEdge(new Edge(
                                new Vector3f(minX, minY, minZ),
                                new Vector3f(maxX, maxY, maxZ)
                        ));
                        return true;
                    });
        }
    }

    public static void render(HitResult hitResult) {
        if (hitResult != null) {
            List<AABBox> outlines = hitResult.state().getOutline();
            collectEdges(outlines, hitResult.x(), hitResult.y(), hitResult.z());

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glLineWidth(2.0f);
            Tesselator t = Tesselator.getInstance();
            t.begin();
            t.color(0.0f, 0.0f, 0.0f);
            for (Edge re : renderEdges) {
                Edge.Line l = re.toLine();
                t.index(0, 1);
                t.vertex(l.min.x, l.min.y, l.min.z).emit();
                t.vertex(l.max.x, l.max.y, l.max.z).emit();
            }
            t.end();
            t.builtDraw(GL_LINES);
            glLineWidth(1.0f);
            glDisable(GL_BLEND);
        }
    }
}
