/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gugugu.oritech.resource.tex;

import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.ResourceLoader;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL30C;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntConsumer;

import static org.lwjgl.opengl.GL13C.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * A 2D texture.
 *
 * @author Overrun Organization
 * @author squid233
 * @since 1.0
 */
public class Texture2D implements AutoCloseable {
    private int id;
    private boolean failed;
    private int width, height;
    @Nullable
    private IntConsumer param;
    private IntConsumer mipmap = GL30C::glGenerateMipmap;
    public int defaultWidth = 16, defaultHeight = 16;

    /**
     * Create an empty 2D texture.
     */
    public Texture2D() {
    }

    /**
     * Create a 2D texture load from the file.
     *
     * @param loc The resource location.
     */
    public Texture2D(ResLocation loc) {
        reload(loc);
    }

    /**
     * Reloads or loads the asset by the resource location.
     * <p>
     * You shouldn't call {@link #create()} before {@code reload()}.
     * </p>
     * <p>
     * If you do that, and if there are 1000 textures,
     * that means you created 2000 texture ids!
     * </p>
     *
     * @param loc The resource location.
     */
    public void reload(ResLocation loc) {
        ByteBuffer buffer = null;
        try {
            buffer = ResourceLoader.loadToByteBuffer(loc, 8192);
        } catch (IOException e) {
            new RuntimeException("Error reading resource to buffer!", e)
                .printStackTrace();
        }
        buffer = asBuffer(buffer, loc.toPath());
        try {
            build(buffer);
        } finally {
            memFree(buffer);
        }
    }

    /**
     * Create to an empty texture.
     *
     * @param width  the texture width
     * @param height the texture height
     * @since 0.2.0
     */
    public void loadEmpty(int width, int height) {
        this.width = width;
        this.height = height;
        build(null);
    }

    /**
     * Load texture from byte buffer.
     *
     * @param bytes     the byte buffer
     * @param name      the resource debugging name
     * @param freeBytes free bytes after loading buffer
     */
    public void load(ByteBuffer bytes,
                     @Nullable String name,
                     boolean freeBytes) {
        ByteBuffer buffer = asBuffer(bytes,
            Objects.requireNonNullElse(name,
                "byte array"));
        if (freeBytes) {
            memFree(bytes);
        }
        try {
            build(buffer);
        } finally {
            memFree(buffer);
        }
    }

    /**
     * Load texture from byte buffer.
     *
     * @param bytes the byte buffer
     * @param name  the resource debugging name
     */
    public void load(ByteBuffer bytes,
                     @Nullable String name) {
        load(bytes, name, false);
    }

    /**
     * Load texture from byte array.
     *
     * @param bytes the byte array
     * @param name  the resource debugging name
     */
    public void load(byte[] bytes, @Nullable String name) {
        ByteBuffer bb = memAlloc(bytes.length).put(bytes).flip();
        load(bb, name, true);
    }

    public void setParam(@Nullable IntConsumer param) {
        this.param = param;
    }

    public Optional<IntConsumer> getParam() {
        return Optional.ofNullable(param);
    }

    public void setMipmap(IntConsumer mipmap) {
        this.mipmap = mipmap;
    }

    public Optional<IntConsumer> getMipmap() {
        return Optional.ofNullable(mipmap);
    }

    private ByteBuffer fail() {
        failed = true;
        width = (defaultWidth == 0 ? 16 : defaultWidth);
        height = (defaultHeight == 0 ? 16 : defaultHeight);
        int[] missingNo = new int[width * height];
        final int hx = width >> 1;
        final int hy = height >> 1;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = x + y * width;
                if (y < hy)
                    missingNo[index] = x < hx ? 0xf800f8 : 0x000000;
                else
                    missingNo[index] = x < hx ? 0x000000 : 0xf800f8;
            }
        }
        ByteBuffer buffer = memAlloc(missingNo.length * 4);
        buffer.asIntBuffer().put(missingNo).flip();
        return buffer;
    }

    private ByteBuffer asBuffer(ByteBuffer buffer,
                                String name) {
        if (buffer == null)
            return fail();
        int[] xp = {0}, yp = {0}, cp = {0};
        ByteBuffer ret = stbi_load_from_memory(
            buffer,
            xp,
            yp,
            cp,
            STBI_rgb_alpha);
        if (ret == null) {
            System.err.println("Failed to load image '" +
                               name +
                               "'! Reason: " +
                               stbi_failure_reason());
            ret = fail();
        } else {
            width = xp[0];
            height = yp[0];
        }
        return ret;
    }

    private void build(ByteBuffer buffer) {
        int lastUnit = glGetInteger(GL_ACTIVE_TEXTURE);
        int lastId = glGetInteger(GL_TEXTURE_2D);
        if (!glIsTexture(id))
            create();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, id);
        if (mipmap != null) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
        }
        if (param != null)
            param.accept(GL_TEXTURE_2D);
        glTexImage2D(GL_TEXTURE_2D,
            0,
            GL_RGBA,
            width,
            height,
            0,
            failed ? GL_RGB : GL_RGBA,
            GL_UNSIGNED_BYTE,
            buffer);
        if (mipmap != null) {
            mipmap.accept(GL_TEXTURE_2D);
        }
        glActiveTexture(GL_TEXTURE0 + lastUnit);
        glBindTexture(GL_TEXTURE_2D, lastId);
    }

    public void create() {
        id = glGenTextures();
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return id;
    }

    /**
     * If {@code true}, the texture uses the missing texture.
     *
     * @return If the texture loading failed.
     */
    public boolean isFailed() {
        return failed;
    }

    /**
     * Get the texture width.
     *
     * @return the texture width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the texture height.
     *
     * @return the texture height
     */
    public int getHeight() {
        return height;
    }

    @Override
    public void close() {
        glDeleteTextures(id);
    }
}
