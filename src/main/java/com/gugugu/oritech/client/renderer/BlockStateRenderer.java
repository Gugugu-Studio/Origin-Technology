package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.block.BlockState;
import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.world.World;

public class BlockStateRenderer implements IRenderer<BlockState> {
    @Override
    public boolean render(Batch batch, World world, BlockState obj) {
        return false;
    }
}
