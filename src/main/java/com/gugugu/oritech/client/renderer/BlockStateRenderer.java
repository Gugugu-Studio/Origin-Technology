package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.client.model.ModelOperators;
import com.gugugu.oritech.client.render.RenderSystem;
import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.World;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;

@SideOnly(Side.CLIENT)
public class BlockStateRenderer extends AbstractBlockStateRenderer {
    public void renderFace(Tesselator t, Direction face, BlockState obj) {
        Matrix4fStack matrixStack = RenderSystem.getMatrixStack();
        matrixStack.pushMatrix();
        ModelOperators model = getBlockModel(obj);
        if (model.translate != null) {
            matrixStack.translate(model.translate);
        }
        if (model.rotate != null) {
            Vector3f _rotate = new Vector3f(model.rotate);
            _rotate.mul((float) Math.PI);
            _rotate.div(180);
            matrixStack.rotateXYZ(_rotate);
        }
        if (model.scale != null) {
            matrixStack.scale(model.scale);
        }
        model.model.renderFace(t, face);
        matrixStack.popMatrix();
    }

    public boolean render(Tesselator t, World world, BlockState obj) {
        boolean rendered = false;
        for (Direction face : Direction.values()) {
            if (obj.shouldRenderFace(obj, face)) {
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
                renderFace(t, face, obj);
                rendered = true;
            }
        }
        return rendered;
    }
}
