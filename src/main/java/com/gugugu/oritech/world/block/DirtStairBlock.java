package com.gugugu.oritech.world.block;

import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.shape.VoxelShapes;
import com.gugugu.oritech.world.block.properties.IProperty;
import com.gugugu.oritech.world.block.properties.Properties;

import java.util.List;
import java.util.Map;

public class DirtStairBlock extends Block {
    private static final List<AABBox> SHAPE = VoxelShapes.combine(
        createCuboidShape(0, 0, 0, 16, 8, 16),
        createCuboidShape(0, 8, 8, 16, 16, 16)
    );

    @Override
    public boolean hasSideTransparency() {
        return true;
    }

    @Override
    public List<AABBox> getOutline(BlockState state) {
        return SHAPE;
    }

    @Override
    public Map<String, IProperty> getProperties() {
        return Map.of("direction", Properties.DIRECTION);
    }
}
