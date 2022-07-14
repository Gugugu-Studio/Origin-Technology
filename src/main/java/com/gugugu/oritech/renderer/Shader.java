package com.gugugu.oritech.renderer;

import static org.lwjgl.opengl.GL20C.*;

/**
 * @author theflysong
 * @author squid233
 * @since 1.0
 */
public class Shader implements AutoCloseable {
    private final int programId;

    public Shader(CharSequence vertexShader, CharSequence fragmentShader) {
        programId = glCreateProgram();
        int vsh = compileShader(GL_VERTEX_SHADER, "vertex", vertexShader);
        int fsh = compileShader(GL_FRAGMENT_SHADER, "fragment", fragmentShader);
        glAttachShader(programId, vsh);
        glAttachShader(programId, fsh);
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Failed to link program " +
                                            programId +
                                            ". Log: " +
                                            glGetProgramInfoLog(programId));
        }
        glDetachShader(programId, vsh);
        glDetachShader(programId, fsh);
        glDeleteShader(vsh);
        glDeleteShader(fsh);
    }

    private static int compileShader(int type, String typeStr, CharSequence src) {
        int shader = glCreateShader(type);
        glShaderSource(shader, src);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Failed to compile " +
                                            typeStr +
                                            " shader. Log: " +
                                            glGetShaderInfoLog(shader));
        }
        return shader;
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void delete() {
        glDeleteProgram(programId);
    }

    @Override
    public void close() {
        delete();
    }
}
