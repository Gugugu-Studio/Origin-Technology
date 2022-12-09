package com.gugugu.oritech.client.gui;

import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import org.lwjgl.opengl.GL11C;

import static com.gugugu.oritech.client.gl.GLStateMgr.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
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
        Tesselator t = Tesselator.getInstance();
        t.begin();
        t.index(0, 1, 2, 2, 3, 0);
        t.texCoord(u0, v0).vertex(x, y).emit();
        t.texCoord(u0, v1).vertex(x, y1).emit();
        t.texCoord(u1, v1).vertex(x1, y1).emit();
        t.texCoord(u1, v0).vertex(x1, y).emit();
        t.end();
        t.builtDraw(GL11C.GL_TRIANGLES);
        bindTexture(lastUnit, lastId);
    }
}
