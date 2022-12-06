package com.gugugu.oritech.client.gui;

import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static com.gugugu.oritech.client.gl.GLStateMgr.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class DrawableHelper {
    public static void drawSprite(Matrix4fStack model, Identifier id,
                                  float x, float y,
                                  float w, float h,
                                  int u, int v) {
        drawSprite(model, id, x, y, w, h, u, v, w, h);
    }

    public static void drawSprite(Matrix4fStack model, Identifier id,
                                  float x, float y,
                                  float w, float h,
                                  int u, int v,
                                  float spriteW, float spriteH) {
        drawSprite(model, id, x, y, w, h, u, v, spriteW, spriteH, 256, 256);
    }

    public static void drawSprite(Matrix4fStack model, Identifier id,
                                  float x, float y,
                                  float w, float h,
                                  int u, int v,
                                  float spriteW, float spriteH,
                                  float texW, float texH) {
        final float u0 = u / texW;
        final float v0 = v / texH;
        final float u1 = (u + spriteW) / texW;
        final float v1 = (v + spriteH) / texH;
        final float x1 = x + w;
        final float y1 = y + h;
        int lastUnit = getActiveTexture2d();
        int lastId = getTexture2dId();
        bindTexture(0, id);
        final Vector4f xy0 = model.transform(new Vector4f(x, y, 1, 1));
        final Vector4f xy1 = model.transform(new Vector4f(x1, y1, 1, 1));
        Tesselator.getInstance().withBatch(batch ->
            batch.begin()
                .quadIndices()
                .texCoords(u0, v0).vertex(xy0.x, xy0.y)
                .texCoords(u0, v1).vertex(xy0.x, xy1.y)
                .texCoords(u1, v1).vertex(xy1.x, xy1.y)
                .texCoords(u1, v0).vertex(xy1.x, xy0.y)
                .end().upload().render());
        bindTexture(lastUnit, lastId);
    }
}
