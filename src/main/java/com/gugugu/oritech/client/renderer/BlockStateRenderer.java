package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.block.BlockState;
import com.gugugu.oritech.block.Blocks;
import com.gugugu.oritech.client.model.Model;
import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.World;

@SideOnly(Side.CLIENT)
public class BlockStateRenderer extends AbstractBlockStateRenderer {

    public void renderFace(Batch batch, Direction face, BlockState obj) {
        getBlockModel(obj).renderFace(batch, face);
    }

    public boolean render(Batch batch, World world, BlockState obj) {
        boolean rendered = false;
        for (Direction face : Direction.values()) {
            if (obj.shouldRenderFace(world,
                obj.getPos().x() + face.getOffsetX(),
                obj.getPos().y() + face.getOffsetY(),
                obj.getPos().z() + face.getOffsetZ())) {
                final float c1 = 1.0f;
                final float c2 = 0.8f;
                final float c3 = 0.6f;
                if (face.isOnAxisX()) {
                    batch.color(c1, c1, c1);
                } else if (face.isOnAxisY()) {
                    batch.color(c2, c2, c2);
                } else if (face.isOnAxisZ()) {
                    batch.color(c3, c3, c3);
                }
                renderFace(batch, face, obj);
                rendered = true;
            }
        }
        return rendered;
    }
}
