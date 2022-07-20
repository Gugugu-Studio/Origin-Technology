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

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memCalloc;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * A native image.<br>
 * <b>NOTE:</b> The buffer is little-endian.
 *
 * @author Overrun Organization
 * @author squid233
 * @since 1.0
 */
public class NativeImage implements AutoCloseable {
    private int width, height;
    private int defaultWidth, defaultHeight;
    private ByteBuffer buffer = null;

    public void load(ResLocation loc) {
        try {
            buffer = ResourceLoader.loadToByteBuffer(loc, 8192);
        } catch (IOException e) {
            throw new RuntimeException("Error reading resource to buffer!", e);
        }
        buffer = asBuffer(buffer, loc.toPath());
    }

    private ByteBuffer fail() {
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
        var buffer = memCalloc(missingNo.length * 4);
        buffer.asIntBuffer().put(missingNo).flip();
        return buffer;
    }

    private ByteBuffer asBuffer(ByteBuffer buffer,
                                String name) {
        if (buffer == null)
            return fail();
        int[] xp = {0}, yp = {0}, cp = {0};
        var ret = stbi_load_from_memory(
            buffer,
            xp,
            yp,
            cp,
            STBI_rgb_alpha);
        if (ret == null) {
            // TODO: 2022/7/20 Replace with logger
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

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int defaultWidth() {
        return defaultWidth;
    }

    public void defaultWidth(int defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    public int defaultHeight() {
        return defaultHeight;
    }

    public void defaultHeight(int defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

    public ByteBuffer buffer() {
        return buffer;
    }

    @Override
    public void close() {
        memFree(buffer);
    }
}
