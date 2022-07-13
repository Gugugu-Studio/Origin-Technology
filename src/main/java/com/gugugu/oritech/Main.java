package com.gugugu.oritech;

import com.gugugu.oritech.ui.Window;
import static org.lwjgl.opengl.GL11.*;

public class Main extends Window {
    public static void main(String[] args) {
    }

    public Main(String title, int width, int height) throws IllegalStateException {
        super(title, width, height);
    }

    @Override
    public void init() {
        glClearColor(0.3f, 0.4f, 1.0f, 1.0f);
    }

    @Override
    public void update() {
    }

    @Override
    public void onKey(long window, int key, int scancode, int action, int mods) {

    }
}
