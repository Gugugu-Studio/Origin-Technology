package com.gugugu.oritech.world.block;

import com.gugugu.oritech.phys.AABBox;

import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public class AirBlock extends Block {
    @Override
    public boolean isAir() {
        return true;
    }

    @Override
    public boolean hasSideTransparency() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public List<AABBox> getOutline() {
        return null;
    }

    @Override
    public boolean hasModel() {
        return false;
    }
}
