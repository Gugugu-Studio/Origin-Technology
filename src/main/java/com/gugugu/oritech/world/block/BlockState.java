package com.gugugu.oritech.world.block;

import com.gugugu.oritech.world.block.properties.IProperty;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.BlockPos;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.World;
import com.gugugu.oritech.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class BlockState {
    protected Block block;
    protected @Nullable Chunk chunk;
    protected @Nullable World world;
    protected final Map<String, IProperty> properties;

    public BlockState(Block block) {
        this(block, null);
    }

    public BlockState(Block block, @Nullable Chunk chunk) {
        this.block = block;
        this.properties = block.getProperties();
        this.chunk = chunk;
        this.world = chunk != null ? chunk.getWorld() : null;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public @Nullable Chunk getChunk() {
        return chunk;
    }

    public void setChunk(@Nullable Chunk chunk) {
        setWorld(chunk != null ? chunk.getWorld() : null);
        this.chunk = chunk;
    }

    public @Nullable World getWorld() {
        return world;
    }

    public void setWorld(@Nullable World world) {
        this.world = world;
    }

    public boolean isAir() {
        return block.isAir();
    }

    public List<AABBox> getOutline() {
        return block.getOutline(this);
    }

    public List<AABBox> getCollision() {
        return block.getCollision(this);
    }

    public List<AABBox> getRayCast() {
        return block.getRayCast(this);
    }

    public boolean canPlaceOn(BlockState block, World world, int x, int y, int z, Direction face) {
        return this.block.canPlaceOn(block, world, x, y, z, face);
    }

    public boolean shouldRenderFace(BlockPos pos, Direction face) {
        return block.shouldRenderFace(this, pos, face);
    }

    public Identifier getRenderer() {
        return block.getRenderer();
    }

    public Map<String, IProperty> getProperties() {
        return properties;
    }

    public IProperty getProperty(String name) {
        return properties.get(name);
    }
}
