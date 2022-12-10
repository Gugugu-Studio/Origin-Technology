package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.client.model.ModelOperators;
import com.gugugu.oritech.client.render.RenderSystem;
import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.BlockPos;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.World;
import com.gugugu.oritech.world.block.BlockState;
import org.joml.Math;
import org.joml.Matrix4fStack;

@SideOnly(Side.CLIENT)
public class BlockStateRenderer extends AbstractBlockStateRenderer {
    @Override
    public void renderFace(Tesselator t, BlockPos pos, Direction face, BlockState state) {
        Matrix4fStack matrixStack = RenderSystem.getMatrixStack();
        matrixStack.pushMatrix();
        ModelOperators model = getBlockModel(state);
        if (model.translate != null) {
            matrixStack.translate(model.translate);
        }
        if (model.rotate != null) {
            matrixStack.rotateXYZ(Math.toRadians(model.rotate.x()),
                Math.toRadians(model.rotate.y()),
                Math.toRadians(model.rotate.z()));
        }
        if (model.scale != null) {
            matrixStack.scale(model.scale);
        }
        model.model.renderFace(t, face);
        matrixStack.popMatrix();
    }

    @Override
    public boolean render(Tesselator t, float x, float y, float z, World world, BlockState state) {
        boolean rendered = false;
        BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
        for (Direction face : Direction.values()) {
            if (state.shouldRenderFace(pos, face)) {
                final float c1 = 1.0f;
                final float c2 = 0.8f;
                final float c3 = 0.6f;
                if (face.isOnAxisX()) {
                    t.color(c1, c1, c1);
                } else if (face.isOnAxisY()) {
                    t.color(c2, c2, c2);
                } else if (face.isOnAxisZ()) {
                    t.color(c3, c3, c3);
                }
                renderFace(t, pos, face, state);
                rendered = true;
            }
        }
        return rendered;
    }
}
