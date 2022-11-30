package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.HitResult;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.floor;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL11C.GL_BLEND;

@SideOnly(Side.CLIENT)
public class TryHitRenderer {
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

    private static void collectEdges(List<AABBox> outlines) {
        allEdges.clear();
        renderEdges.clear();
        if (outlines == null) {
            return;
        }
        for (AABBox outline : outlines) {
            outline.forEachEdge((dir, minX, minY, minZ, maxX, maxY, maxZ) -> {
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
            List<AABBox> outlines = hitResult.block().getOutline();
            for (AABBox aabb : outlines) {
                aabb.min.add(hitResult.x(), hitResult.y(), hitResult.z());
                aabb.max.add(hitResult.x(), hitResult.y(), hitResult.z());
            }
            collectEdges(outlines);

            GameRenderer gameRenderer = OriTechClient.getClient().gameRenderer;
            gameRenderer.setShaderColor(0.0f, 0.0f, 0.0f, 0.4f);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glLineWidth(2.0f);
            final float epsilon = 0.001f;
            Tesselator.getInstance().withBatch(batch -> {
                batch.begin();
                for (Edge re : renderEdges) {
                    Edge.Line l = re.toLine();
                    batch.vertex(l.min.x, l.min.y, l.min.z)
                        .vertex(l.max.x, l.max.y, l.max.z);
                }
                batch.end().upload().render(GL_LINES);
            });
            glLineWidth(1.0f);
            glDisable(GL_BLEND);
            gameRenderer.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
