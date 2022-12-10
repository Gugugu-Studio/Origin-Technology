package com.gugugu.oritech.world.block;

import com.gugugu.oritech.client.renderer.BlockStateRenderers;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.BlockPos;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.ClientRegistry;
import com.gugugu.oritech.util.shape.VoxelShapes;
import com.gugugu.oritech.world.World;
import com.gugugu.oritech.world.block.properties.IProperty;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

/**
 * @author squid233
 * @since 1.0
 */
public class Block {
    private static final Vector3f ZERO = new Vector3f(0, 0, 0);
    private static final Vector3f ONE = new Vector3f(1, 1, 1);

    public Block() {
    }

    public boolean isAir() {
        return false;
    }

    public boolean hasSideTransparency() {
        return false;
    }

    public boolean isSolid() {
        return true;
    }

    public List<AABBox> getOutline(BlockState state) {
        return VoxelShapes.fullCube();
    }

    public List<AABBox> getRayCast(BlockState state) {
        return getOutline(state);
    }

    public List<AABBox> getCollision(BlockState state) {
        return getOutline(state);
    }

    public static AABBox createCuboidShape(float minX, float minY, float minZ,
                                           float maxX, float maxY, float maxZ) {
        return new AABBox(minX / 16.0f, minY / 16.0f, minZ / 16.0f,
            maxX / 16.0f, maxY / 16.0f, maxZ / 16.0f);
    }

    public boolean shouldRenderFace(BlockState state, BlockPos pos, Direction face) {
        int x = pos.x() + face.getOffsetX();
        int y = pos.y() + face.getOffsetY();
        int z = pos.z() + face.getOffsetZ();
        return this.hasSideTransparency() || state.getWorld().getBlock(x, y, z).block.hasSideTransparency();
    }

    public boolean canPlaceOn(BlockState target, World world, int x, int y, int z, Direction face) {
        return !world.getBlock(x + face.getOffsetX(),
            y + face.getOffsetY(),
            z + face.getOffsetZ()).block.isSolid();
    }

    public boolean hasModel() {
        return true;
    }

    public Identifier getRenderer() {
        return ClientRegistry.BLOCKSTATE_RENDERER.getId(BlockStateRenderers.COMMON);
    }

    public Map<String, IProperty> getProperties() {
        return Map.of();
    }
}
