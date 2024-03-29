package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.util.math.BlockPos;
import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.client.model.BlockModelManager;
import com.gugugu.oritech.client.model.ModelOperators;
import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;

@SideOnly(Side.CLIENT)
public abstract class AbstractBlockStateRenderer implements IRenderer<BlockState> {
    public ModelOperators getBlockModel(BlockState state) {
        Identifier id = Registry.BLOCK.getId(state.getBlock());
        return BlockModelManager.getModel(id).getModel(state);
    }

    public abstract void renderFace(Tesselator t, BlockPos pos, Direction face, BlockState state);
}
