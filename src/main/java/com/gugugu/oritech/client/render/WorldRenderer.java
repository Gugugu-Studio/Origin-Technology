package com.gugugu.oritech.client.render;

import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.entity.PlayerEntity;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.phys.RayCastResult;
import com.gugugu.oritech.util.HitResult;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.IWorldListener;
import com.gugugu.oritech.world.chunk.RenderChunk;
import org.joml.Intersectionf;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.floor;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class WorldRenderer implements IWorldListener, AutoCloseable {
    public static final int MAX_REBUILD_PER_FRAME = 4;
    private final OriTechClient client;
    private final ClientWorld world;
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
        List<RenderChunk> dirtyChunks = new ArrayList<>();
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
        TryHitRenderer.render(hitResult);
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
        Vector3f dir = viewMatrix.positiveZ(pickVec).negate();
        Block hBlock = null;
        int hX = 0, hY = 0, hZ = 0;
        Direction hFace = null;

        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                for (int z = z0; z < z1; z++) {
                    BlockState block = world.getBlock(x, y, z);
                    if (!block.isAir()) {
                        List<AABBox> rayCasts = block.getRayCast();
                        if (rayCasts == null) {
                            continue;
                        }
                        for (AABBox aabb : rayCasts) {
                            aabb.min.add(x, y, z);
                            aabb.max.add(x, y, z);
                        }
                        for (AABBox rayCast : rayCasts) {
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
                                hBlock = block.getBlock();
                                hX = x;
                                hY = y;
                                hZ = z;
                                hFace = rayCast.rayCastFacing(camera.position, dir);
                            }
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
        OriTechClient.getServer().world.markDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }

    @Override
    public void close() {
    }
}
