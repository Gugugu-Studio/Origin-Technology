package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.gl.DataType;
import com.gugugu.oritech.client.render.vertex.Vertex;
import com.gugugu.oritech.client.render.vertex.VertexFormat;
import com.gugugu.oritech.client.render.vertex.VertexLayout;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class Mesh implements AutoCloseable {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Integer> indices = new ArrayList<>();
    private int vao, vbo, ebo;

    private Mesh() {
    }

    /**
     * Generates GL objects.
     */
    private void genGLObj(VertexLayout layout) {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        ByteBuffer buf = memAlloc(layout.getLength() * vertices.size());
        for (Vertex vert : vertices) {
            for (VertexFormat format : layout) {
                switch (format) {
                    case POSITION -> DataType.FLOAT.asWrite(buf, vert.x(), vert.y(), vert.z());
                    case COLOR -> DataType.UNSIGNED_BYTE.asWrite(buf, vert.r(), vert.g(), vert.b(), vert.a());
                    case TEX_COORDS -> DataType.FLOAT.asWrite(buf, vert.s(), vert.t());
                    case NORMAL -> DataType.BYTE.asWrite(buf, vert.nx(), vert.ny(), vert.nz());
                }
            }
        }
        buf.flip();
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
        memFree(buf);

        IntBuffer indexBuf = memAllocInt(indices.size());
        for (int i : indices) {
            indexBuf.put(i);
        }
        indexBuf.flip();
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuf, GL_STATIC_DRAW);
        memFree(indexBuf);

        layout.beginDraw(layout.getLength());

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public static Mesh of(VertexLayout layout,
                          Vertex vertex, Vertex... vertices) {
        Mesh mesh = new Mesh();
        mesh.vertices.add(vertex);
        mesh.indices.add(0);
        for (Vertex vert : vertices) {
            int index = mesh.vertices.indexOf(vert);
            if (index >= 0) {
                mesh.indices.add(index);
            } else {
                mesh.vertices.add(vert);
                mesh.indices.add(mesh.vertices.indexOf(vert));
            }
        }
        mesh.genGLObj(layout);
        return mesh;
    }

    public void render(int mode) {
        glBindVertexArray(vao);
        glDrawElements(mode, indices.size(), GL_UNSIGNED_INT, 0L);
        glBindVertexArray(0);
    }

    public void free() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }

    @Override
    public void close() {
        free();
    }
}
