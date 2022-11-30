package com.gugugu.oritech.world;

import com.gugugu.oritech.block.BlockState;
import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.ChunkPos;
import com.gugugu.oritech.block.Block;
import com.gugugu.oritech.world.chunk.RenderChunk;
import org.joml.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class ClientWorld extends World implements AutoCloseable {
    private final Map<Long, RenderChunk> chunkMap = new HashMap<>();
    private final List<IWorldListener> listeners = new ArrayList<>();
    public final Random random;
    public final long seed;

    public ClientWorld(OriTechClient client,
                       long seed,
                       int spawnX, int spawnY, int spawnZ) {
        this.seed = seed;
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

    private RenderChunk loadChunk(int x, int y, int z) {
        return new RenderChunk(
            this,
            OriTechClient.getServer().world.getChunk(x, y, z));
    }

    @Override
    public boolean setBlock(BlockState block, int x, int y, int z) {
        return OriTechClient.getServer().world.setBlock(block, x, y, z);
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
    public void save(String directory) {
        OriTechClient.getServer().world.save(directory);
    }

    @Override
    public void load(String directory) {
        OriTechClient.getServer().world.load(directory);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void close() {
        for (RenderChunk chunk : chunkMap.values()) {
            chunk.close();
        }
    }
}
