package com.gugugu.oritech.renderer;

import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.ResourceLoader;
import org.joml.Matrix4fStack;

import java.util.function.Consumer;

/**
 * @author squid233
 * @since 1.0
 */
public class GameRenderer implements AutoCloseable {
    public final Camera camera = new Camera();
    public final Matrix4fStack projection = new Matrix4fStack(4);
    public final Matrix4fStack view = new Matrix4fStack(16);
    public final Matrix4fStack model = new Matrix4fStack(16);
    public int renderInstance = 5;
    private Shader position;
    private Shader positionColor;
    private Shader positionColorTex;

    private static Shader loadShader(String name) {
        return ResourceLoader.loadShader(ResLocation.ofCore("shader/" + name + ".vert"),
            ResLocation.ofCore("shader/" + name + ".frag"));
    }

    public void init() {
        position = loadShader("position");
        positionColor = loadShader("position_color");
        positionColorTex = loadShader("position_color_tex");
    }

    public void useShader(Shader shader,
                          Consumer<Shader> supplier) {
        shader.bind();
        supplier.accept(shader);
        shader.unbind();
    }

    public Shader position() {
        return position;
    }

    public Shader positionColor() {
        return positionColor;
    }

    public Shader positionColorTex() {
        return positionColorTex;
    }

    @Override
    public void close() {
        position.close();
        positionColor.close();
        positionColorTex.close();
    }
}
