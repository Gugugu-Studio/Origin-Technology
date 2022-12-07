package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.block.BlockState;
import com.gugugu.oritech.block.Blocks;
import com.gugugu.oritech.client.model.BlockModelManager;
import com.gugugu.oritech.client.model.Model;
import com.gugugu.oritech.client.model.ModelOperators;
import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;

@SideOnly(Side.CLIENT)
public abstract class AbstractBlockStateRenderer implements IRenderer<BlockState> {
    public ModelOperators getBlockModel(BlockState obj) {
        Identifier id = Registry.BLOCK.getId(obj.getBlock());
        return BlockModelManager.getModel(id).getModel(obj);
    }

    public abstract void renderFace(Batch batch, Direction face, BlockState obj);
}
