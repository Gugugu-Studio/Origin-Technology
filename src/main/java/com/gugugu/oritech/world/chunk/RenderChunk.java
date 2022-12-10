package com.gugugu.oritech.world.chunk;

import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.client.render.Frustum;
import com.gugugu.oritech.client.render.RenderSystem;
import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.client.renderer.AbstractBlockStateRenderer;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.util.registry.ClientRegistry;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.entity.PlayerEntity;
import com.gugugu.oritech.world.World;
import org.joml.Matrix4fStack;

import static org.lwjgl.opengl.GL30C.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class RenderChunk extends Chunk implements AutoCloseable {
    private final ClientWorld world;
    private final int x0, y0, z0;
    private final int x1, y1, z1;
    private final float x, y, z;
    public final int chunkX, chunkY, chunkZ;
    private final int width, height, depth;
    private final int vao, vbo, ebo;
    private final Tesselator.EndProperty property = new Tesselator.EndProperty();
    private boolean isDirty = true;
    private boolean hasRendered = false;
    public double dirtiedTime = 0.0;

    public RenderChunk(ClientWorld world,
                       LogicChunk logicChunk) {
        this.world = world;
        x0 = logicChunk.x0;
        y0 = logicChunk.y0;
        z0 = logicChunk.z0;
        x1 = logicChunk.x1;
        y1 = logicChunk.y1;
        z1 = logicChunk.z1;
        x = (x0 + x1) * 0.5f;
        y = (y0 + y1) * 0.5f;
        z = (z0 + z1) * 0.5f;
        chunkX = logicChunk.chunkX;
        chunkY = logicChunk.chunkY;
        chunkZ = logicChunk.chunkZ;
        width = logicChunk.width;
        height = logicChunk.height;
        depth = logicChunk.depth;
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return OriTechClient.getServer().world
            .getChunk(chunkX, chunkY, chunkZ)
            .getBlock(x, y, z);
    }

    @Override
    public void setBlock(BlockState block, int x, int y, int z) {
        OriTechClient.getServer().world
            .getChunk(chunkX, chunkY, chunkZ)
            .setBlock(block, x, y, z);
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void markDirty() {
        if (!isDirty) {
            dirtiedTime = Timer.getTime();
        }
        isDirty = true;
    }

    public void rebuild() {
        //int blocks = 0;
        boolean rendered = false;
        Tesselator t = Tesselator.getInstance();
        t.begin();
        Matrix4fStack matrix = RenderSystem.getMatrixStack();
        matrix.pushMatrix()
            .identity();
        for (int y = 0; y < height; y++) {
            int absY = getAbsolutePos(chunkY, y);
            for (int x = 0; x < width; x++) {
                int absX = getAbsolutePos(chunkX, x);
                for (int z = 0; z < depth; z++) {
                    BlockState block = getBlock(x, y, z);
                    int absZ = getAbsolutePos(chunkZ, z);
                    if (!block.isAir()) {
                        matrix.pushMatrix()
                            .scale(0.5f)
                            .translate(
                                absX * 2 + 1,
                                absY * 2 + 1,
                                absZ * 2 + 1
                            );
                        AbstractBlockStateRenderer renderer = ClientRegistry.BLOCKSTATE_RENDERER.get(block.getRenderer());
                        boolean b = renderer.render(t,
                            absX,
                            absY,
                            absZ,
                            world, block);
                        if (b) {
                            rendered = true;
                        }
                        matrix.popMatrix();
                        //++blocks;
                    }
                }
            }
        }
        matrix.popMatrix();
        hasRendered = rendered;
        //if (blocks > 0) {
        //    hasBlock = true;
        //}
        t.end(vao, vbo, ebo, property);
        isDirty = false;
    }

    public boolean testFrustum() {
        return Frustum.test(x0, y0, z0, x1, y1, z1);
    }

    public void render() {
        if (hasRendered && testFrustum()) {
            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, property.indicesSize, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
        }
    }

    @Override
    public float distanceSqr(PlayerEntity player) {
        return player.position.distanceSquared(x, y, z);
    }

    @Override
    public void close() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }

    @Override
    public World getWorld() {
        return world;
    }
}
