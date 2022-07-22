package com.gugugu.oritech.world;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.math.ChunkPos;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.Chunk;
import com.gugugu.oritech.world.chunk.RenderChunk;
import org.joml.Math;
import org.joml.Random;

import java.util.*;

import static com.gugugu.oritech.world.chunk.Chunk.*;

/**
 * @author squid233
 * @since 1.0
 */
public class ClientWorld extends World implements AutoCloseable {
    private final Map<Long, RenderChunk> chunkMap = new HashMap<>();
    private final List<IWorldListener> listeners = new ArrayList<>();
    private final Random random;

    public ClientWorld(OriTechClient client,
                       long seed,
                       int spawnX, int spawnY, int spawnZ) {
        random = new Random(seed);
        int distance = client.gameRenderer.renderDistance;
        forEachChunksDistance(distance, spawnX, spawnY, spawnZ, (chunk, x, y, z) -> {
        });
    }

    public void addListener(IWorldListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IWorldListener listener) {
        listeners.remove(listener);
    }

    public void markDirty(int x0, int y0, int z0,
                          int x1, int y1, int z1) {
        x0 = getChunkPos(x0);
        y0 = getChunkPos(y0);
        z0 = getChunkPos(z0);
        x1 = getChunkPos(x1);
        y1 = getChunkPos(y1);
        z1 = getChunkPos(z1);

        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                for (int z = z0; z <= z1; z++) {
                    getChunk(x, y, z).markDirty();
                }
            }
        }
    }

    private RenderChunk loadChunk(int x, int y, int z) {
        int x0 = x * CHUNK_SIZE;
        int y0 = y * CHUNK_SIZE;
        int z0 = z * CHUNK_SIZE;
        int x1 = (x + 1) * CHUNK_SIZE;
        int y1 = (y + 1) * CHUNK_SIZE;
        int z1 = (z + 1) * CHUNK_SIZE;

        if (x0 < -LIMIT) {
            x0 = -LIMIT;
        }
        if (y0 < -Y_LIMIT) {
            y0 = -Y_LIMIT;
        }
        if (z0 < -LIMIT) {
            z0 = -LIMIT;
        }

        if (x1 > LIMIT) {
            x1 = LIMIT;
        }
        if (y1 > Y_LIMIT) {
            y1 = Y_LIMIT;
        }
        if (z1 > LIMIT) {
            z1 = LIMIT;
        }

        return new RenderChunk(
            this,
            x0, y0, z0,
            x1, y1, z1);
    }

    public void forEachChunksDistance(int distance,
                                      float xo, float yo, float zo,
                                      IChunkConsumer action) {
        final float dc = distance * CHUNK_SIZE * 0.5f;
        int cx0 = getChunkPos((int) (xo - dc));
        int cy0 = getChunkPos((int) (yo - dc));
        int cz0 = getChunkPos((int) (zo - dc));
        int cx1 = getChunkPos((int) (xo + dc));
        int cy1 = getChunkPos((int) (yo + dc));
        int cz1 = getChunkPos((int) (zo + dc));
        for (int y = cy0; y <= cy1; y++) {
            for (int x = cx0; x <= cx1; x++) {
                for (int z = cz0; z <= cz1; z++) {
                    long pos = ChunkPos.toLong(x, y, z);
                    RenderChunk chunk = chunkMap.get(pos);
                    if (chunk == null) {
                        chunk = loadChunk(x, y, z);
                        chunkMap.put(pos, chunk);
                    }
                    action.accept(chunk, x, y, z);
                }
            }
        }
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        if (isInsideWorld(x, y, z)) {
            return getChunkByBlockPos(x, y, z)
                .getBlock(getRelativePos(x), getRelativePos(y), getRelativePos(z));
        }
        return Blocks.AIR;
    }

    @Override
    public boolean setBlock(Block block, int x, int y, int z) {
        Chunk chunk;
        int rpx, rpy, rpz;
        if (isOutsideWorld(x, y, z)) {
            return false;
        } else {
            chunk = getChunkByBlockPos(x, y, z);
            rpx = getRelativePos(x);
            rpy = getRelativePos(y);
            rpz = getRelativePos(z);
            if (chunk.getBlock(rpx, rpy, rpz) == block) {
                return false;
            }
        }
        chunk.setBlock(block, rpx, rpy, rpz);
        for (IWorldListener listener : listeners) {
            listener.onBlockChanged(x, y, z);
        }
        return true;
    }

    @Override
    public List<AABBox> getCubes(AABBox origin) {
        List<AABBox> list = new ArrayList<>();
        int x0 = (int) Math.floor(origin.min.x);
        int y0 = (int) Math.floor(origin.min.y);
        int z0 = (int) Math.floor(origin.min.z);
        int x1 = (int) Math.floor(origin.max.x + 1.0f);
        int y1 = (int) Math.floor(origin.max.y + 1.0f);
        int z1 = (int) Math.floor(origin.max.z + 1.0f);

        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                for (int z = z0; z < z1; z++) {
                    var block = getBlock(x, y, z);
                    if (!block.isAir()) {
                        AABBox aabb = block.getCollision(x, y, z);
                        if (aabb != null) {
                            list.add(aabb);
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public RenderChunk getChunk(int x, int y, int z) {
        long pos = ChunkPos.toLong(x, y, z);
        RenderChunk chunk = chunkMap.get(pos);
        if (chunk == null) {
            chunk = loadChunk(x, y, z);
            chunkMap.put(pos, chunk);
        }
        return chunk;
    }

    @Override
    public void close() {
        for (RenderChunk chunk : chunkMap.values()) {
            chunk.close();
        }
    }
}
