package com.gugugu.oritech.client.render.vertex;

import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class VertexLayout implements Iterable<VertexFormat> {
    private final Map<VertexFormat, Integer> offsets = new LinkedHashMap<>();
    private final int length;

    public VertexLayout(VertexFormat... formats) {
        int offset = 0;
        for (var format : formats) {
            offsets.put(format, offset);
            offset += format.length;
        }
        length = offset;
    }

    public void beginDraw(int stride) {
        int index = 0;
        for (VertexFormat format : this) {
            format.beginDraw(index++, stride, getOffset(format));
        }
    }

    public void endDraw() {
        int index = 0;
        for (VertexFormat format : this) {
            format.endDraw(index++);
        }
    }

    @Override
    public Iterator<VertexFormat> iterator() {
        return offsets.keySet().iterator();
    }

    public int getOffset(VertexFormat format) {
        return offsets.get(format);
    }

    public int getLength() {
        return length;
    }
}
