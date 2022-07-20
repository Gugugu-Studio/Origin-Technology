package com.gugugu.oritech.world;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.RenderChunk;

import java.util.ArrayList;
import java.util.List;

import static com.gugugu.oritech.world.chunk.Chunk.CHUNK_SIZE;

/**
 * @author squid233
 * @since 1.0
 */
public class ClientWorld extends World implements AutoCloseable {
    private final Block[][][] blocks;
    public final RenderChunk[][][] chunks;
    public final int width, height, depth;
    public final int xChunks, yChunks, zChunks;
    private final List<IWorldListener> listeners = new ArrayList<>();

    public ClientWorld(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        xChunks = width / CHUNK_SIZE;
        yChunks = height / CHUNK_SIZE;
        zChunks = depth / CHUNK_SIZE;
        blocks = new Block[height][width][depth];
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < 11; y++) {
                    blocks[y][x][z] = Blocks.STONE;
                }
                for (int y = 11; y < 15; y++) {
                    blocks[y][x][z] = Blocks.DIRT;
                }
                blocks[15][x][z] = Blocks.GRASS_BLOCK;
                for (int y = 16; y < height; y++) {
                    blocks[y][x][z] = Blocks.AIR;
                }
            }
        }
        chunks = new RenderChunk[yChunks][xChunks][zChunks];
        for (int y = 0; y < chunks.length; y++) {
            RenderChunk[][] chunks2 = chunks[y];
            for (int x = 0; x < chunks2.length; x++) {
                RenderChunk[] chunks1 = chunks2[x];
                for (int z = 0; z < chunks1.length; z++) {
                    int x0 = x * CHUNK_SIZE;
                    int y0 = y * CHUNK_SIZE;
                    int z0 = z * CHUNK_SIZE;
                    int x1 = (x + 1) * CHUNK_SIZE;
                    int y1 = (y + 1) * CHUNK_SIZE;
                    int z1 = (z + 1) * CHUNK_SIZE;

                    if (x1 > width) {
                        x1 = width;
                    }
                    if (y1 > height) {
                        y1 = height;
                    }
                    if (z1 > depth) {
                        z1 = depth;
                    }

                    chunks1[z] = new RenderChunk(this,
                        x0, y0, z0,
                        x1, y1, z1);
                }
            }
        }
    }

    public void addListener(IWorldListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IWorldListener listener) {
        listeners.remove(listener);
    }

    public void markDirty(int x0, int y0, int z0,
                          int x1, int y1, int z1) {
        x0 /= CHUNK_SIZE;
        y0 /= CHUNK_SIZE;
        z0 /= CHUNK_SIZE;
        x1 /= CHUNK_SIZE;
        y1 /= CHUNK_SIZE;
        z1 /= CHUNK_SIZE;

        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (z0 < 0) {
            z0 = 0;
        }
        if (x1 >= xChunks) {
            x1 = xChunks - 1;
        }
        if (y1 >= yChunks) {
            y1 = yChunks - 1;
        }
        if (z1 >= zChunks) {
            z1 = zChunks - 1;
        }

        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                for (int z = z0; z <= z1; z++) {
                    chunks[y][x][z].markDirty();
                }
            }
        }
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        if (isInsideWorld(x, y, z))
            return blocks[y][x][z];
        return Blocks.AIR;
    }

    @Override
    public boolean setBlock(Block block, int x, int y, int z) {
        if (isOutsideWorld(x, y, z) ||
            getBlock(x, y, z) == block) {
            return false;
        }
        blocks[y][x][z] = block;
        for (IWorldListener listener : listeners) {
            listener.onBlockChanged(x, y, z);
        }
        return true;
    }

    @Override
    public List<AABBox> getCubes(AABBox origin) {
        List<AABBox> list = new ArrayList<>();
        int x0 = (int) origin.min.x;
        int y0 = (int) origin.min.y;
        int z0 = (int) origin.min.z;
        int x1 = (int) (origin.max.x + 1.0f);
        int y1 = (int) (origin.max.y + 1.0f);
        int z1 = (int) (origin.max.z + 1.0f);

        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (z0 < 0) {
            z0 = 0;
        }

        if (x1 > width) {
            x1 = width;
        }
        if (y1 > height) {
            y1 = height;
        }
        if (z1 > depth) {
            z1 = depth;
        }

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

    public RenderChunk getChunk(int x, int y, int z) {
        return chunks[y][x][z];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void close() {
        for (RenderChunk[][] chunks2 : chunks) {
            for (RenderChunk[] chunks1 : chunks2) {
                for (RenderChunk chunk : chunks1) {
                    chunk.close();
                }
            }
        }
    }
}
