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

import java.nio.ByteBuffer;
import java.util.StringJoiner;

/**
 * The sprite info.
 *
 * @author Overrun Organization
 * @author squid233
 * @since 1.0
 */
public class SpriteInfo {
    private final ResLocation loc;
    private final int defaultWidth, defaultHeight;
    private int width, height;
    private int x, y;
    private NativeImage image;

    public SpriteInfo(ResLocation loc,
                      int defaultWidth,
                      int defaultHeight) {
        this.loc = loc;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
    }

    public SpriteInfo(ResLocation loc) {
        this(loc, 0, 0);
    }

    public void load() {
        image = new NativeImage();
        image.defaultWidth(defaultWidth);
        image.defaultHeight(defaultHeight);
        image.load(loc);
        width = image.width();
        height = image.height();
    }

    public ResLocation loc() {
        return loc;
    }

    public int defaultWidth() {
        return defaultWidth;
    }

    public int defaultHeight() {
        return defaultHeight;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int x() {
        return x;
    }

    public void x(int x) {
        this.x = x;
    }

    public int y() {
        return y;
    }

    public void y(int y) {
        this.y = y;
    }

    public ByteBuffer buffer() {
        return image.buffer();
    }

    public void free() {
        image.close();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SpriteInfo.class.getSimpleName() + "[", "]")
            .add("loc='" + loc + "'")
            .add("defaultWidth=" + defaultWidth)
            .add("defaultHeight=" + defaultHeight)
            .add("width=" + width)
            .add("height=" + height)
            .add("x=" + x)
            .add("y=" + y)
            .toString();
    }
}
