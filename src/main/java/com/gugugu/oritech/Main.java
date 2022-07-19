package com.gugugu.oritech;

import com.gugugu.oritech.client.OriTechClient;
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

    private OriTechClient client;

    public Main(String title, int width, int height) throws IllegalStateException {
        super(title, width, height);
    }

    @Override
    public void init(int width, int height) {
        glClearColor(0.3f, 0.4f, 1.0f, 1.0f);
        // Initialize resources here
        client = new OriTechClient(width, height);
        client.keyboard = keyboard;
        client.mouse = mouse;
    }

    @Override
    public void update() {
        client.updateTime();
        // Render
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        client.render();
    }

    @Override
    public void close() {
        // Free resources here
        client.close();
        super.close();
    }

    @Override
    public void onKey(long window, int key, int scancode, int action, int mods) {
        client.onKey(window, key, scancode, action, mods);
    }

    @Override
    public void onCursorPos(double x, double y, double xd, double yd) {
        client.onCursorPos(x, y, xd, yd);
    }

    @Override
    public void onResize(long window, int width, int height) {
        glViewport(0, 0, width, height);
        if (client != null) {
            client.onResize(window, width, height);
        }
    }
}
