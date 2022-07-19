package com.gugugu.oritech.client;

import com.gugugu.oritech.renderer.GameRenderer;
import com.gugugu.oritech.ui.IKeyListener;
import com.gugugu.oritech.ui.ISizeListener;
import com.gugugu.oritech.ui.Keyboard;
import com.gugugu.oritech.ui.Mouse;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.util.math.Numbers;
import com.gugugu.oritech.world.ClientWorld;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Client.
 *
 * @author squid233
 * @since 1.0
 */
public class OriTechClient
    implements IKeyListener, ISizeListener, Mouse.Callback, AutoCloseable {
    public static final float SENSITIVITY = 0.15f;
    private static OriTechClient instance;
    public final GameRenderer gameRenderer;
    public final Timer timer = new Timer();
    public Keyboard keyboard;
    public Mouse mouse;
    public int width, height;
    private ClientWorld world;

    public OriTechClient(int width, int height) {
        this.width = width;
        this.height = height;
        this.gameRenderer = new GameRenderer();
        gameRenderer.init();
        world = new ClientWorld(32, 32, 32);

        instance = this;
    }

    public void updateTime() {
        timer.update();
        for (int i = 0; i < timer.ticks; i++) {
            tick();
        }

        update((float) timer.deltaFrameTime);
    }

    public void update(float delta) {
    }

    public void tick() {
        float xa = 0.0f, ya = 0.0f, za = 0.0f;
        if (keyboard.isKeyDown(GLFW_KEY_W)) {
            --za;
        }
        if (keyboard.isKeyDown(GLFW_KEY_S)) {
            ++za;
        }
        if (keyboard.isKeyDown(GLFW_KEY_A)) {
            --xa;
        }
        if (keyboard.isKeyDown(GLFW_KEY_D)) {
            ++xa;
        }
        if (keyboard.isKeyDown(GLFW_KEY_SPACE)) {
            ++ya;
        }
        if (keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            --ya;
        }
        gameRenderer.camera.update();
        gameRenderer.camera.moveRelative(xa, ya, za, 0.1f);
    }

    public void render() {
        gameRenderer.useShader(gameRenderer.positionColor(),
            shader -> {
                shader.getUniform("Projection").ifPresent(uniform -> uniform.set(gameRenderer.projection
                    .setPerspective(Numbers.RAD90F,
                        (float) width / (float) height,
                        0.01f,
                        1000.0f)));
                gameRenderer.camera.smoothStep = (float) timer.partialTick;
                shader.getUniform("View").ifPresent(uniform -> uniform.set(gameRenderer.camera.apply(
                    gameRenderer.view.pushMatrix()
                )));
                gameRenderer.view.popMatrix();
                shader.uploadUniforms();
                world.chunk.rebuild();
                world.chunk.render();
//                tesselator.begin()
//                    .color(1.0f, 0.0f, 0.0f).vertex(0.0f, 1.0f, 0.0f)
//                    .color(0.0f, 1.0f, 0.0f).vertex(0.0f, 0.0f, 0.0f)
//                    .color(0.0f, 0.0f, 1.0f).vertex(1.0f, 0.0f, 0.0f)
//                    .emit()
//                    .color(1.0f, 1.0f, 1.0f).vertex(1.0f, 1.0f, 0.0f)
//                    .color(1.0f, 0.0f, 0.0f).vertex(0.0f, 1.0f, 0.0f)
//                    .end();
            });
    }

    @Override
    public void onKey(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_ESCAPE) {
                mouse.setGrabbed(!mouse.isGrabbed());
            }
        }
    }

    @Override
    public void onResize(long window, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onCursorPos(double x, double y, double xd, double yd) {
        if (mouse.isGrabbed()) {
            gameRenderer.camera.rotate((float) xd * SENSITIVITY,
                (float) -yd * SENSITIVITY);
        }
    }

    @Override
    public void close() {
        world.close();
        gameRenderer.close();
    }

    public static OriTechClient getClient() {
        return instance;
    }
}
