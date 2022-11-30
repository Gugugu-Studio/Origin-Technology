package com.gugugu.oritech.block;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.math.BlockPos;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.World;
import com.gugugu.oritech.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockState {
    protected Block block;
    protected @Nullable BlockPos pos;
    protected @Nullable Chunk chunk;
    protected @Nullable World world;

    public BlockState(Block block) {
        this(block, null, null);
    }

    public BlockState(Block block, @Nullable BlockPos pos, @Nullable Chunk chunk) {
        this.block = block;
        this.pos = pos;
        this.chunk = chunk;
        this.world = chunk != null ? chunk.getWorld() : null;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public @Nullable BlockPos getPos() {
        return pos;
    }

    public void setPos(@Nullable BlockPos pos) {
        this.pos = pos;
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

    public List<AABBox> getCollision() {
        return block.getCollision();
    }

    public boolean canPlaceOn(BlockState block, World world, int x, int y, int z, Direction face) {
        return this.block.canPlaceOn(block, world, x, y, z, face);
    }

    public List<AABBox> getRayCast() {
        return block.getRayCast();
    }
}
