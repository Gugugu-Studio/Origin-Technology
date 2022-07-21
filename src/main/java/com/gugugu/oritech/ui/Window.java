package com.gugugu.oritech.ui;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author theflysong
 * @author squid233
 * @since 1.0
 */
public abstract class Window
    implements IKeyListener, ISizeListener, Mouse.Callback, AutoCloseable {
    private final long handle;
    protected final Keyboard keyboard;
    protected final Mouse mouse;

    public static void initGLFW() throws IllegalStateException {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    }

    public static void terminateGLFW() {
        glfwTerminate();
        GLFWErrorCallback cb = glfwSetErrorCallback(null);
        if (cb != null) {
            cb.free();
        }
    }

    public Window(String title, int width, int height) throws IllegalStateException {
        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(handle, this::onKey);
        glfwSetFramebufferSizeCallback(handle, this::onResize);
        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            switch (action) {
                case GLFW_PRESS -> onMouseBtnPress(button, mods);
                case GLFW_RELEASE -> onMouseBtnRelease(button, mods);
            }
        });
        keyboard = new Keyboard();
        keyboard.setWindow(handle);
        mouse = new Mouse(this);
        mouse.registerToWindow(handle);

        int[] pWidth = {0}, pHeight = {0};
        glfwGetWindowSize(handle, pWidth, pHeight);

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (videoMode != null) {
            glfwSetWindowPos(
                handle,
                (videoMode.width() - pWidth[0]) / 2,
                (videoMode.height() - pHeight[0]) / 2
            );
        }

        glfwMakeContextCurrent(handle);
        GL.createCapabilities(true);
        glfwSwapInterval(1);

        glfwShowWindow(handle);
    }

    public abstract void init(int width, int height);

    public abstract void update();

    public void onMouseBtnPress(int btn, int mods) {
    }

    public void onMouseBtnRelease(int btn, int mods) {
    }

    public void mainLoop() {
        int[] pWidth = {0}, pHeight = {0};
        glfwGetFramebufferSize(handle, pWidth, pHeight);
        init(pWidth[0], pHeight[0]);
        onResize(handle, pWidth[0], pHeight[0]);

        while (!glfwWindowShouldClose(handle)) {
            update();

            glfwSwapBuffers(handle);
            glfwPollEvents();
        }
    }

    @Override
    public void close() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
    }
}
