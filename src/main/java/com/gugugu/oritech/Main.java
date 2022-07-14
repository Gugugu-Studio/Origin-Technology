package com.gugugu.oritech;

import com.gugugu.oritech.ui.Window;

import static org.lwjgl.opengl.GL11C.*;

/**
 * @author theflysong
 * @author squid233
 * @since 1.0
 */
public class Main extends Window {
    public static void main(String[] args) {
        Window.initGLFW();
        try (var main = new Main("Origin Technology", 854, 480)) {
            main.mainLoop();
        }
        Window.terminateGLFW();
    }

    public Main(String title, int width, int height) throws IllegalStateException {
        super(title, width, height);
    }

    @Override
    public void init() {
        glClearColor(0.3f, 0.4f, 1.0f, 1.0f);
        // Initialize resources here
    }

    @Override
    public void update() {
        // Render
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void close() {
        // Free resources here
        super.close();
    }

    @Override
    public void onKey(long window, int key, int scancode, int action, int mods) {

    }

    @Override
    public void onResize(long window, int width, int height) {
        glViewport(0, 0, width, height);
    }
}
