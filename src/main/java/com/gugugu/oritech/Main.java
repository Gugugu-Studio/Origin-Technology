package com.gugugu.oritech;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.ui.Window;
import org.lwjgl.opengl.GL11C;

import java.io.File;

import static org.lwjgl.opengl.GL11C.glClearColor;

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
        glClearColor(0.53f, 0.68f, 0.98f, 1.0f);
        // Initialize resources here
        client = new OriTechClient(width, height);
        client.keyboard = keyboard;
        client.mouse = mouse;
        client.lazyInit();

        client.world.load("./save/");
    }

    @Override
    public void update() {
        client.run();
    }

    @Override
    public void close() {
        File directory = new File("./save/");
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                System.err.println("Couldn't create save directory!");
            }
        }

        client.world.save("./save/");

        // Free resources here
        client.close();
        super.close();
    }

    @Override
    public void onKey(long window, int key, int scancode, int action, int mods) {
        client.onKey(window, key, scancode, action, mods);
    }

    @Override
    public void onScroll(long window, double xoffset, double yoffset) {
        client.onScroll(xoffset, yoffset);
    }

    @Override
    public void onCursorPos(double x, double y, double xd, double yd) {
        client.onCursorPos(x, y, xd, yd);
    }

    @Override
    public void onResize(long window, int width, int height) {
        GL11C.glViewport(0, 0, width, height);
        if (client != null) {
            client.onResize(window, width, height);
        }
    }

    @Override
    public void onMouseBtnPress(int btn, int mods) {
        client.onMouseBtnPress(btn, mods);
    }

    @Override
    public void onMouseBtnRelease(int btn, int mods) {
        client.onMouseBtnRelease(btn, mods);
    }
}
