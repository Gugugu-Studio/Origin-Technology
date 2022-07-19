package com.gugugu.oritech.world.block;

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
}
