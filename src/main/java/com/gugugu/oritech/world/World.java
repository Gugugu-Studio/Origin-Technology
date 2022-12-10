package com.gugugu.oritech.world;

import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.chunk.Chunk;
import org.joml.Math;

import java.util.ArrayList;
import java.util.List;

import static com.gugugu.oritech.world.chunk.Chunk.CHUNK_SIZE;
import static com.gugugu.oritech.world.chunk.Chunk.getChunkPos;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class World {
    public static final int CHUNK_LIMIT = 2097152;
    public static final int LIMIT = CHUNK_LIMIT * Chunk.CHUNK_SIZE;
    public static final int Y_LIMIT = CHUNK_LIMIT * 8;

    public abstract int getGameTime();

    public BlockState getBlock(int x, int y, int z) {
        if (isInsideWorld(x, y, z)) {
            return getChunkByBlockPos(x, y, z)
                .getBlockAbsolute(x, y, z);
        }
        return new BlockState(Blocks.AIR);
    }

    public abstract boolean setBlock(BlockState block, int x, int y, int z);

    public abstract Chunk getChunk(int x, int y, int z);

    public abstract void save(String directory);

    public abstract void load(String directory);

    public abstract boolean isClient();

    public Chunk getChunkByBlockPos(int x, int y, int z) {
        return getChunk(Chunk.getChunkPos(x),
            Chunk.getChunkPos(y),
            Chunk.getChunkPos(z));
    }

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
                        List<AABBox> aabBoxes = block.getCollision();
                        if (aabBoxes == null) {
                            continue;
                        }
                        for (AABBox aabb : aabBoxes) {
                            if (aabb != null) {
                                list.add(new AABBox(aabb).move(x, y, z));
                            }
                        }
                    }
                }
            }
        }

        return list;
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
                    action.accept(getChunk(x, y, z), x, y, z);
                }
            }
        }
    }

    public boolean canBlockPlaceOn(BlockState block, int x, int y, int z, Direction face) {
        return block.canPlaceOn(getBlock(x, y, z), this, x, y, z, face);
    }

    public boolean isInsideWorld(int x, int y, int z) {
        return x >= -LIMIT && x < LIMIT &&
               y >= -Y_LIMIT && y < Y_LIMIT &&
               z >= -LIMIT && z < LIMIT;
    }

    public boolean isOutsideWorld(int x, int y, int z) {
        return !isInsideWorld(x, y, z);
    }

    public void update() {
    }
}
