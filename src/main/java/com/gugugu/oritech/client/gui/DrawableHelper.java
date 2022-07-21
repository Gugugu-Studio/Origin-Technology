package com.gugugu.oritech.client.gui;

import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.util.Identifier;

import static com.gugugu.oritech.client.gl.GLStateMgr.*;

/**
 * @author squid233
 * @since 1.0
 */
public class DrawableHelper {
    public static void drawSprite(Identifier id,
                                  float x, float y,
                                  float w, float h,
                                  int u, int v) {
        drawSprite(id, x, y, w, h, u, v, w, h);
    }

    public static void drawSprite(Identifier id,
                                  float x, float y,
                                  float w, float h,
                                  int u, int v,
                                  float spriteW, float spriteH) {
        drawSprite(id, x, y, w, h, u, v, spriteW, spriteH, 256, 256);
    }

    public static void drawSprite(Identifier id,
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
        Tesselator.getInstance().withBatch(batch ->
            batch.begin()
                .quadIndices()
                .texCoords(u0, v0).vertex(x, y)
                .texCoords(u0, v1).vertex(x, y1)
                .texCoords(u1, v1).vertex(x1, y1)
                .texCoords(u1, v0).vertex(x1, y)
                .end().render());
        bindTexture(lastUnit, lastId);
    }
}
