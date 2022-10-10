package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.phys.RayCastResult;
import com.gugugu.oritech.util.HitResult;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.IWorldListener;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.chunk.RenderChunk;
import com.gugugu.oritech.world.entity.PlayerEntity;
import org.joml.Intersectionf;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.floor;
import static org.lwjgl.opengl.GL11C.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class WorldRenderer implements IWorldListener, AutoCloseable {
    public static final int MAX_REBUILD_PER_FRAME = 4;
    private final OriTechClient client;
    private final ClientWorld world;
    private final List<RenderChunk> dirtyChunks = new ArrayList<>();
    private static final Vector3f pickVec = new Vector3f();
    public HitResult hitResult;

    public WorldRenderer(OriTechClient client,
                         ClientWorld world) {
        this.client = client;
        this.world = world;
        world.addListener(this);
        OriTechClient.getServer().world.addListener(this);
    }

    public List<RenderChunk> getDirtyChunks(PlayerEntity player) {
        dirtyChunks.clear();
        final int distance = client.gameRenderer.renderDistance;
        final Vector3f pos = player.position;
        world.forEachChunksDistance(distance, pos.x(), pos.y(), pos.z(), (chunk, x, y, z) -> {
            if (chunk instanceof RenderChunk rc &&
                rc.isDirty() &&
                rc.testFrustum()) {
                dirtyChunks.add(rc);
            }
        });
        return dirtyChunks;
    }

    public void updateDirtyChunks(PlayerEntity player) {
        List<RenderChunk> list = getDirtyChunks(player);
        if (list.size() > 0) {
            DirtyChunkSorter.reset(player);
            list.sort(DirtyChunkSorter::compare);
            for (int i = 0; i < list.size() && i < MAX_REBUILD_PER_FRAME; i++) {
                RenderChunk chunk = list.get(i);
                chunk.rebuild();
                if (chunk.batch != null) {
                    chunk.batch.initGL();
                    if (chunk.batch.hasBuilt() && !chunk.batch.uploaded()) {
                        chunk.batch.upload();
                    }
                }
            }
        }
    }

    public void render(PlayerEntity player) {
        final int distance = client.gameRenderer.renderDistance;
        final Vector3f pos = player.position;
        world.forEachChunksDistance(distance, pos.x(), pos.y(), pos.z(), (chunk, x, y, z) ->
            ((RenderChunk) chunk).render());
    }

    public void tryRenderHit() {
        if (hitResult != null) {
            GameRenderer gameRenderer = OriTechClient.getClient().gameRenderer;
            gameRenderer.setShaderColor(0.0f, 0.0f, 0.0f, 0.4f);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glLineWidth(2.0f);
            AABBox outline = hitResult.block().getOutline(hitResult.x(), hitResult.y(), hitResult.z());
            final float epsilon = 0.001f;
            Tesselator.getInstance().withBatch(batch -> {
                batch.begin();
                outline.forEachEdge((dir, minX, minY, minZ, maxX, maxY, maxZ) -> {
                    boolean transparency =
                        world.getBlock((int) floor(minX) + dir.getOffsetX(),
                            (int) floor(minY) + dir.getOffsetY(),
                            (int) floor(minZ) + dir.getOffsetZ()).hasSideTransparency();
                    float offset = transparency ? epsilon : 0;
                    float xo = dir.getOffsetX() * offset;
                    float yo = dir.getOffsetY() * offset;
                    float zo = dir.getOffsetZ() * offset;
                    batch.vertex(minX + xo, minY+yo, minZ+zo)
                        .vertex(maxX + xo, maxY + yo, maxZ + zo);
                    return true;
                });
                batch.end().upload().render(GL_LINES);
            });
            glLineWidth(1.0f);
            glDisable(GL_BLEND);
            gameRenderer.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public void pick(PlayerEntity player, Matrix4fc viewMatrix, Camera camera) {
        final float r = 5.0f;
        AABBox box = player.aabb.grow(r, r, r);
        int x0 = (int) floor(box.min.x);
        int x1 = (int) floor(box.max.x + 1.0f);
        int y0 = (int) floor(box.min.y);
        int y1 = (int) floor(box.max.y + 1.0f);
        int z0 = (int) floor(box.min.z);
        int z1 = (int) floor(box.max.z + 1.0f);
        float closestDistance = Float.POSITIVE_INFINITY;
        hitResult = null;
        AABBox rayCast;
        Vector3f dir = viewMatrix.positiveZ(pickVec).negate();
        Block hBlock = null;
        int hX = 0, hY = 0, hZ = 0;
        Direction hFace = null;

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
                            RayCastResult.nearFar)
                            && RayCastResult.nearFar.x < closestDistance) {
                            closestDistance = RayCastResult.nearFar.x;
                            hBlock = block;
                            hX = x;
                            hY = y;
                            hZ = z;
                            hFace = rayCast.rayCastFacing(camera.position, dir);
                        }
                    }
                }
            }
        }
        if (hBlock != null) {
            hitResult = new HitResult(hBlock,
                hX, hY, hZ,
                hFace);
        }
    }

    @Override
    public void onBlockChanged(int x, int y, int z) {
        world.markDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }

    @Override
    public void close() {
    }
}
