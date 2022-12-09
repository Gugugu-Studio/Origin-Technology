package com.gugugu.oritech.world;

import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.util.math.ChunkPos;
import com.gugugu.oritech.world.chunk.Chunk;
import com.gugugu.oritech.world.chunk.LogicChunk;
import com.gugugu.oritech.world.save.BlocksCoders;
import org.joml.Random;

import java.io.*;
import java.util.*;

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
    public List<LogicChunk> dirtyChunks = new ArrayList<>();

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
    public boolean setBlock(BlockState block, int x, int y, int z) {
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

    private boolean saveChunk(String directory, LogicChunk chunk) {
        int x = chunk.chunkX;
        int y = chunk.chunkY;
        int z = chunk.chunkZ;
        String filename = directory + "/" + "chunk_" + x + "_" + y + "_" + z + ".dat";
        DataOutputStream output;

        try {
            File chunk_save = new File(filename);
            if (!chunk_save.exists()) {
                if (!chunk_save.createNewFile()) {
                    System.err.println("Couldn't create save file");
                    return false;
                }
            }
            output = new DataOutputStream(new FileOutputStream(chunk_save));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BlocksCoders.RAW.saveBlocks(chunk.width, chunk.depth, chunk.height, chunk.getBlocks(), output);
        return true;
    }

    @Override
    public void save(String directory) {
        dirtyChunks.removeIf(chunk -> saveChunk(directory, chunk));
    }

    private void loadChunk(LogicChunk chunk, DataInputStream input) {
        BlockState[][][] blocks = BlocksCoders.RAW.getBlocks(input);
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                for (int z = 0; z < 32; z++) {
                    chunk.setBlock(blocks[y][x][z], x, y, z);
                }
            }
        }
    }

    @Override
    public void load(String directory) {
        File dir = new File(directory);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                for (File chunk_dat : dir.listFiles()) {
                    String name = chunk_dat.getName();
                    String[] name_ext = name.split("\\.");
                    String[] names = name_ext[0].split("_");
                    int x = Integer.parseInt(names[1]);
                    int y = Integer.parseInt(names[2]);
                    int z = Integer.parseInt(names[3]);

                    LogicChunk chunk = getChunk(x, y, z);
                    try {
                        loadChunk(chunk, new DataInputStream(new FileInputStream(chunk_dat)));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public boolean isClient() {
        return false;
    }
}
