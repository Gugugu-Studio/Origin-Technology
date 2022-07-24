package com.gugugu.oritech.world;

import com.gugugu.oritech.util.math.ChunkPos;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.chunk.Chunk;
import com.gugugu.oritech.world.chunk.LogicChunk;
import org.joml.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gugugu.oritech.world.chunk.Chunk.CHUNK_SIZE;
import static com.gugugu.oritech.world.chunk.Chunk.getRelativePos;

/**
 * @author squid233
 * @since 1.0
 */
public class ServerWorld extends World {
    private final Map<Long, LogicChunk> chunkMap = new HashMap<>();
    private final List<IWorldListener> listeners = new ArrayList<>();
    public final Random random;
    public final long seed;

    public ServerWorld(long seed,
                       int distance,
                       int spawnX, int spawnY, int spawnZ) {
        this.seed = seed;
        random = new Random(seed);
        forEachChunksDistance(distance, spawnX, spawnY, spawnZ, (chunk, x, y, z) -> {
        });
    }

    public void addListener(IWorldListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IWorldListener listener) {
        listeners.remove(listener);
    }

    private LogicChunk loadChunk(int x, int y, int z) {
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

        return new LogicChunk(
            this,
            x0, y0, z0,
            x1, y1, z1);
    }

    @Override
    public boolean setBlock(Block block, int x, int y, int z) {
        Chunk chunk;
        int rpx, rpy, rpz;
        if (isOutsideWorld(x, y, z)) {
            return false;
        }
        chunk = getChunkByBlockPos(x, y, z);
        rpx = getRelativePos(x);
        rpy = getRelativePos(y);
        rpz = getRelativePos(z);
        if (chunk.getBlock(rpx, rpy, rpz) == block) {
            return false;
        }
        chunk.setBlock(block, rpx, rpy, rpz);
        for (IWorldListener listener : listeners) {
            listener.onBlockChanged(x, y, z);
        }
        return true;
    }

    @Override
    public LogicChunk getChunk(int x, int y, int z) {
        long pos = ChunkPos.toLong(x, y, z);
        LogicChunk chunk = chunkMap.get(pos);
        if (chunk == null) {
            chunk = loadChunk(x, y, z);
            chunkMap.put(pos, chunk);
        }
        return chunk;
    }

    @Override
    public boolean isClient() {
        return false;
    }
}
