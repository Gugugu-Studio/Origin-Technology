package com.gugugu.oritech.client.render;

import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.IWorldListener;
import com.gugugu.oritech.world.chunk.RenderChunk;
import com.gugugu.oritech.world.entity.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public class WorldRenderer implements IWorldListener {
    public static final int MAX_REBUILD_PER_FRAME = 8;
    private final ClientWorld world;

    public WorldRenderer(ClientWorld world) {
        this.world = world;
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

    @Override
    public void onBlockChanged(int x, int y, int z) {
    }
}
