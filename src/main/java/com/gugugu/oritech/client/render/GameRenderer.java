package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.client.gl.Shader;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.ResourceLoader;
import com.gugugu.oritech.util.math.Numbers;
import org.joml.Matrix4fStack;

import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

/**
 * @author squid233
 * @since 1.0
 */
public class GameRenderer implements AutoCloseable {
    public final Camera camera = new Camera();
    public final Matrix4fStack projection = new Matrix4fStack(4);
    public final Matrix4fStack view = new Matrix4fStack(16);
    public final Matrix4fStack model = new Matrix4fStack(16);
    private final OriTechClient client;
    public Batch batch;
    public int renderInstance = 5;
    private Shader position;
    private Shader positionColor;
    private Shader positionColorTex;
    private Shader currentShader;

    private static Shader loadShader(String name) {
        return ResourceLoader.loadShader(ResLocation.ofCore("shader/" + name + ".vert"),
            ResLocation.ofCore("shader/" + name + ".frag"));
    }

    public GameRenderer(OriTechClient client) {
        this.client = client;
    }

    public void init() {
        position = loadShader("position");
        positionColor = loadShader("position_color");
        positionColorTex = loadShader("position_color_tex");
        batch = new Batch();
    }

    public void useShader(Shader shader,
                          Consumer<Shader> supplier) {
        bindShader(shader);
        supplier.accept(shader);
        bindShader(null);
    }

    public void setupShaderMatrix(Shader shader) {
        shader.getUniform("Projection").ifPresent(uniform -> uniform.set(projection));
        shader.getUniform("View").ifPresent(uniform -> uniform.set(view));
        shader.getUniform("Model").ifPresent(uniform -> uniform.set(model));
        shader.uploadUniforms();
    }

    private void moveCameraToPlayer(float delta) {
        camera.smoothStep = delta;
        camera.apply(view.translate(0.0f, 0.0f, -0.3f));
    }

    public void setupCamera(float delta, float width, float height) {
        projection.setPerspective(Numbers.RAD90F,
            width / height,
            0.05f,
            1000.0f);
        view.identity();
        moveCameraToPlayer(delta);
        model.identity();
    }

    public void render() {
        setupCamera((float) client.timer.partialTick, client.width, client.height);
        Frustum.update(projection.pushMatrix().mul(view));
        projection.popMatrix();
        client.worldRenderer.pick(client.player, view, camera);
        useShader(positionColorTex, shader -> {
            setupShaderMatrix(shader);
            client.worldRenderer.updateDirtyChunks(client.player);

            client.blockAtlas.bind();
            client.worldRenderer.render();
            glBindTexture(GL_TEXTURE_2D, 0);
        });
        useShader(position, shader -> {
            setupShaderMatrix(shader);
            client.worldRenderer.tryRenderHit(batch);
        });
    }

    public void bindShader(Shader shader) {
        if (shader != null) {
            shader.bind();
        } else if (currentShader != null) {
            currentShader.unbind();
        }
        currentShader = shader;
    }

    public Shader getCurrentShader() {
        return currentShader;
    }

    public void setShaderColor(float r, float g, float b, float a) {
        currentShader.getUniform("ColorModulator").ifPresent(uniform -> uniform.set(r, g, b, a));
        currentShader.uploadUniforms();
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
        batch.free();
        position.close();
        positionColor.close();
        positionColorTex.close();
    }
}
