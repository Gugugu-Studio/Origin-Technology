package com.gugugu.oritech.client.gl;

import com.gugugu.oritech.resource.ResType;
import com.gugugu.oritech.resource.tex.Texture2D;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20C.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class GLStateMgr {
    private static final Map<Identifier, Texture2D> TEXTURE_MAP =
        new HashMap<>();
    private static int textureUnits;
    private static int activeTexture2d = 0;
    private static int[] texture2dId;

    public static Texture2D loadTexture(Identifier id) {
        if (TEXTURE_MAP.containsKey(id)) {
            return getTexture(id);
        }
        Texture2D tex = new Texture2D();
        tex.setParam(target -> {
            glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        });
        tex.setMipmap(null);
        tex.reload(id.toResLocation(ResType.ASSETS));
        TEXTURE_MAP.put(id, tex);
        return tex;
    }

    public static Texture2D getTexture(Identifier id) {
        return TEXTURE_MAP.get(id);
    }

    public static void activeTexture(int unit) {
        if (activeTexture2d != unit) {
            activeTexture2d = unit;
            glActiveTexture(GL_TEXTURE0 + unit);
        }
    }

    public static void bindTexture(int id) {
        if (texture2dId[activeTexture2d] != id) {
            texture2dId[activeTexture2d] = id;
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    public static void bindTexture(Identifier id) {
        loadTexture(id).bind();
    }

    public static void bindTexture(int unit, int id) {
        activeTexture(unit);
        bindTexture(id);
    }

    public static void bindTexture(int unit, Identifier id) {
        activeTexture(unit);
        bindTexture(id);
    }

    public static int getActiveTexture2d() {
        return activeTexture2d;
    }

    public static int getTexture2dId(int unit) {
        return texture2dId[unit];
    }

    public static int getTexture2dId() {
        return getTexture2dId(activeTexture2d);
    }

    public static int getTextureUnits() {
        return textureUnits;
    }

    public static void initialize() {
        textureUnits = glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
        texture2dId = new int[textureUnits];
    }

    public static void close() {
        for (Texture2D texture : TEXTURE_MAP.values()) {
            texture.close();
        }
    }
}
