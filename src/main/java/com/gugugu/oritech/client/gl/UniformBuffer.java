package com.gugugu.oritech.client.gl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL31C.*;

public class UniformBuffer implements AutoCloseable {
    private final int ubo;
    private final int binding;
    private final String name;
    private final UniformLayout layout;
    private final Map<String, UniformUnit> unitCache;

    public UniformBuffer(UniformLayout layout, String name, int binding) {
        ubo = glGenBuffers();
        this.name = name;
        this.binding = binding;
        this.layout = layout;
        unitCache = new HashMap<>();
        // To avoid long name, we use var
        for (var info : layout.layout.entrySet()) {
            var v = info.getValue();
            unitCache.put(info.getKey(), UniformUnit.of(v.getKey(), v.getValue(), ubo));
        }
        // Alloc UBO
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferData(GL_UNIFORM_BUFFER, layout.getLayoutSize(), GL_STREAM_DRAW);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);

        glBindBufferBase(GL_UNIFORM_BUFFER, binding, ubo);
    }

    public void bind(Shader... shaders) {
        // binding
        for (Shader shader : shaders) {
            int index = glGetUniformBlockIndex(shader.getProgramId(), name);
            glUniformBlockBinding(shader.getProgramId(), index, binding);
        }
    }

    public UniformLayout getLayout() {
        return layout;
    }

    public Optional<UniformUnit> find(String name) {
        if (!unitCache.containsKey(name)) {
            return Optional.empty();
        }
        return Optional.of(unitCache.get(name));
    }

    @Override
    public void close() {
        glDeleteBuffers(ubo);
    }
}
