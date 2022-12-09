package com.gugugu.oritech.world.block;

import com.gugugu.oritech.world.block.properties.DirectionProperty;
import com.gugugu.oritech.world.block.properties.IProperty;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class DirtStairBlock extends Block{
    @Override
    public boolean hasSideTransparency() {
        return true;
    }

    @Override
    public List<AABBox> getOutline() {
        AABBox box1 = new AABBox(
            0.0f, 0.0f, 0.0f,
            1.0f, 0.5f, 1.0f
        );
        AABBox box2 = new AABBox(
            0.0f, 0.5f, 0.5f,
            1.0f, 1.0f, 1.0f
        );
        return new ArrayList<>() {
            {
                add(box1);
                add(box2);
            }
        };
    }

    @Override
    public List<IProperty> getProperties() {
        List<IProperty> properties = super.getProperties();
        properties.add(new DirectionProperty("dir", Direction.EAST));
        return properties;
    }
}
