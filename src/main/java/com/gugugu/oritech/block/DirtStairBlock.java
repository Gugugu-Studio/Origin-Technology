package com.gugugu.oritech.block;

import com.gugugu.oritech.phys.AABBox;

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
}
