package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.HitResult;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.IWorldListener;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.chunk.RenderChunk;
import com.gugugu.oritech.world.entity.PlayerEntity;
import org.joml.Intersectionf;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.*;

/**
 * @author squid233
 * @since 1.0
 */
public class WorldRenderer implements IWorldListener {
    public static final int MAX_REBUILD_PER_FRAME = 8;
    private final ClientWorld world;
    public HitResult hitResult;

    public WorldRenderer(ClientWorld world) {
        this.world = world;
        world.addListener(this);
    }

    public List<RenderChunk> getDirtyChunks() {
        List<RenderChunk> list = null;
        for (RenderChunk[][] chunks2 : world.chunks) {
            for (RenderChunk[] chunks1 : chunks2) {
                for (RenderChunk chunk : chunks1) {
                    if (chunk.isDirty()) {
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(chunk);
                    }
                }
            }
        }
        return list;
    }

    public void updateDirtyChunks(PlayerEntity player) {
        List<RenderChunk> list = getDirtyChunks();
        if (list != null) {
            DirtyChunkSorter.reset(player);
            list.sort(DirtyChunkSorter::compare);
            for (int i = 0; i < list.size() && i < MAX_REBUILD_PER_FRAME; i++) {
                list.get(i).rebuild();
            }
        }
    }

    public void render() {
        for (RenderChunk[][] chunks2 : world.chunks) {
            for (RenderChunk[] chunks1 : chunks2) {
                for (RenderChunk chunk : chunks1) {
                    chunk.render();
                }
            }
        }
    }

    public void tryRenderHit(Batch batch) {
        if (hitResult != null) {
            GameRenderer gameRenderer = OriTechClient.getClient().gameRenderer;
            gameRenderer.setShaderColor(0.0f, 0.0f, 0.0f, 0.4f);
            glDisable(GL_DEPTH_TEST);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            batch.begin();
            AABBox outline = hitResult.block().getOutline(hitResult.x(), hitResult.y(), hitResult.z());
            outline.forEachEdge((dir, minX, minY, minZ, maxX, maxY, maxZ) -> {
                batch.vertex(minX, minY, minZ)
                    .vertex(maxX, maxY, maxZ);
                return true;
            });
            batch.end().render(GL_LINES);
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
            gameRenderer.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public void pick(PlayerEntity player, Matrix4fc viewMatrix, Camera camera) {
        final float r = 5.0f;
        AABBox box = player.aabb.grow(r, r, r);
        int x0 = (int) box.min.x;
        int x1 = (int) (box.max.x + 1.0f);
        int y0 = (int) box.min.y;
        int y1 = (int) (box.max.y + 1.0f);
        int z0 = (int) box.min.z;
        int z1 = (int) (box.max.z + 1.0f);
        float closestDistance = Float.POSITIVE_INFINITY;
        Vector2f nearFar = new Vector2f();
        hitResult = null;
        AABBox rayCast;
        Vector3f dir = viewMatrix.positiveZ(new Vector3f()).negate();

        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                for (int z = z0; z < z1; z++) {
                    Block block = world.getBlock(x, y, z);
                    if (!block.isAir()) {
                        rayCast = block.getRayCast(x, y, z);
                        if (!Frustum.test(rayCast)) {
                            continue;
                        }
                        if (Intersectionf.intersectRayAab(camera.position,
                            dir,
                            rayCast.min,
                            rayCast.max,
                            nearFar)
                            && nearFar.x < closestDistance) {
                            closestDistance = nearFar.x;
                            hitResult = new HitResult(block,
                                x, y, z,
                                rayCast.rayCastFacing(camera.position, dir));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBlockChanged(int x, int y, int z) {
        world.markDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }
}
