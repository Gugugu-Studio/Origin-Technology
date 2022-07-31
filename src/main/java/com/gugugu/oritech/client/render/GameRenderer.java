package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.client.gl.Shader;
import com.gugugu.oritech.client.gui.DrawableHelper;
import com.gugugu.oritech.client.gui.Screen;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.ResourceLoader;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.math.Numbers;
import org.joml.Matrix4fStack;

import java.util.function.Consumer;

import static com.gugugu.oritech.client.gl.GLStateMgr.bindTexture;
import static org.lwjgl.opengl.GL14C.*;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class GameRenderer implements AutoCloseable {
    public final Camera camera = new Camera();
    public final Matrix4fStack projection = new Matrix4fStack(4);
    public final Matrix4fStack view = new Matrix4fStack(16);
    public final Matrix4fStack model = new Matrix4fStack(16);
    private final OriTechClient client;
    public int renderDistance = 5;
    private Shader position;
    private Shader positionColor;
    private Shader positionColorTex;
    private Shader positionTex;
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
        positionTex = loadShader("position_tex");

        Tesselator.getInstance().init();
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

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        useShader(positionColorTex, shader -> {
            setupShaderMatrix(shader);
            client.worldRenderer.updateDirtyChunks(client.player);

            client.blockAtlas.bind();
            client.worldRenderer.render(client.player);
            bindTexture(0);
        });

        useShader(position, shader -> {
            setupShaderMatrix(shader);
            client.worldRenderer.tryRenderHit();
        });

        drawGui(client.width * 0.5f, client.height * 0.5f);
    }

    private void drawGui(float width, float height) {
        glClear(GL_DEPTH_BUFFER_BIT);

        projection.setOrtho2D(0, width, height, 0);
        view.identity();
        model.identity();

        useShader(positionTex, shader -> {
            glEnable(GL_BLEND);
            glBlendFuncSeparate(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR, GL_ONE, GL_ZERO);

            model.pushMatrix().translate(width * 0.5f, height * 0.5f, 0.0f);
            setupShaderMatrix(shader);
            model.popMatrix();
            DrawableHelper.drawSprite(Screen.WIDGETS_TEXTURES,
                -8.0f, -8.0f,
                16.0f, 16.0f,
                240, 0);

            glDisable(GL_BLEND);
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

    public Shader positionTex() {
        return positionTex;
    }

    @Override
    public void close() {
        Tesselator.getInstance().free();
        position.close();
        positionColor.close();
        positionColorTex.close();
        positionTex.close();
    }
}
