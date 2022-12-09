package com.gugugu.oritech.client.gl;

import org.joml.*;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL31C.*;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class UniformBuffer {
    private final int ubo;
    private final int binding;
    private final String name;
    private final UniformLayout layout;

    public UniformBuffer(UniformLayout layout, String name, int binding) {
        ubo = glGenBuffers();
        this.name = name;
        this.binding = binding;
        this.layout = layout;
        // Alloc UBO
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferData(GL_UNIFORM_BUFFER, layout.getLayoutSize(), GL_STATIC_DRAW);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);

        glBindBufferBase(GL_UNIFORM_BUFFER, binding, ubo);
    }

    public void bind(Shader shader) {
        // binding
        int index = glGetUniformBlockIndex(shader.getProgramId(), name);
        glUniformBlockBinding(shader.getProgramId(), index, binding);
    }

    public Optional<UniformUnit> lookup(String name) {
        if (! layout.layout.containsKey(name)) {
            return Optional.empty();
        }
        Map.Entry<Integer, Integer> info = layout.layout.get(name);
        return Optional.of(new UniformUnit(info.getKey(), info.getValue(), ubo));
    }
}
