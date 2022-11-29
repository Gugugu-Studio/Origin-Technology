package com.gugugu.oritech.world.block;

import com.gugugu.oritech.phys.AABBox;

import java.util.ArrayList;
import java.util.List;

public class DirtStairBlock extends Block{
    @Override
    public boolean hasSideTransparency() {
        return true;
    }

    @Override
    public List<AABBox> getOutline(int x, int y, int z) {
        AABBox box1 = new AABBox(
            x + 0.0f, y + 0.0f, z + 0.0f,
            x + 1.0f, y + 0.5f, z + 1.0f
        );
        AABBox box2 = new AABBox(
            x + 0.0f, y + 0.5f, z + 0.5f,
            x + 1.0f, y + 1.0f, z + 1.0f
        );
        return new ArrayList<>() {
            {
                add(box1);
                add(box2);
            }
        };
    }
}
