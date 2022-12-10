package com.gugugu.oritech.client.render;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.client.gl.Shader;
import com.gugugu.oritech.client.gl.UniformBuffer;
import com.gugugu.oritech.client.gl.UniformLayout;
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
    // Model Use in CPU
    public final Matrix4fStack model = new Matrix4fStack(16);
    private final OriTechClient client;
    public int renderDistance = 5;
    private Shader nolightPosition;
    private Shader nolightPositionColor;
    private Shader nolightPositionColorTex;
    private Shader nolightPositionTex;
    private Shader positionNormal;
    private Shader positionColorNormal;
    private Shader positionColorTexNormal;
    private Shader positionTexNormal;
    private Shader currentShader;
    private UniformBuffer buffer;

    private static Shader loadShader(String name) {
        return ResourceLoader.loadShader(ResLocation.ofCore("shader/" + name + ".vert"),
            ResLocation.ofCore("shader/" + name + ".frag"));
    }

    public GameRenderer(OriTechClient client) {
        this.client = client;
        UniformLayout layout = new UniformLayout();
        layout.addIntUniform("gameTime").addVec4Uniform("ColorModulator");
        buffer = new UniformBuffer(layout, "InfoUniform", 2);
    }

    public void init() {
        nolightPosition = loadShader("nolight/position");
        nolightPositionColor = loadShader("nolight/position_color");
        nolightPositionColorTex = loadShader("nolight/position_color_tex");
        nolightPositionTex = loadShader("nolight/position_tex");
        positionNormal = loadShader("light/position_normal");
        positionColorNormal = loadShader("light/position_color_normal");
        positionColorTexNormal = loadShader("light/position_color_tex_normal");
        positionTexNormal = loadShader("light/position_tex_normal");

        buffer.bind(positionNormal);
        buffer.bind(positionColorNormal);
        buffer.bind(positionColorTexNormal);
        buffer.bind(positionTexNormal);
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
        this.buffer.lookup("gameTime").ifPresent(uniform -> {
            uniform.set(client.world.getGameTime());
        });
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
    }

    public void render() {
        setupCamera((float) client.timer.partialTick, client.width, client.height);
        projection.pushMatrix();
        Frustum.update(projection.mul(view));
        projection.popMatrix();

        client.worldRenderer.pick(client.player, view, camera);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        useShader(positionColorTexNormal, shader -> {
            setupShaderMatrix(shader);
            client.worldRenderer.updateDirtyChunks(client.player);

            client.blockAtlas.bind();
            client.worldRenderer.render(client.player);
            bindTexture(0);
        });

        useShader(positionNormal, shader -> {
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

        useShader(nolightPositionTex, shader -> {
            glEnable(GL_BLEND);
            glBlendFuncSeparate(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR, GL_ONE, GL_ZERO);

            model.pushMatrix().translate(width * 0.5f, height * 0.5f, 0.0f);
            setupShaderMatrix(shader);
            DrawableHelper.drawSprite(model, Screen.WIDGETS_TEXTURES,
                -8.0f, -8.0f,
                16.0f, 16.0f,
                240, 0);
            model.popMatrix();

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
//        this.buffer.lookup("ColorModulator").ifPresent(uniform -> uniform.set(r, g, b, a));
    }

    public Shader nolightPosition() {
        return nolightPosition;
    }

    public Shader nolightPositionColor() {
        return nolightPositionColor;
    }

    public Shader nolightPositionColorTex() {
        return nolightPositionColorTex;
    }

    public Shader nolightPositionTex() {
        return nolightPositionTex;
    }

    public Shader positionNormal() {
        return positionNormal;
    }

    public Shader positionColorNormal() {
        return positionColorNormal;
    }

    public Shader positionColorTexNormal() {
        return positionColorTexNormal;
    }

    public Shader positionTexNormal() {
        return positionTexNormal;
    }

    @Override
    public void close() {
        Tesselator.getInstance().free();
        nolightPosition.close();
        nolightPositionColor.close();
        nolightPositionColorTex.close();
        nolightPositionTex.close();
        positionNormal.close();
        positionColorNormal.close();
        positionColorTexNormal.close();
        positionTexNormal.close();
    }
}
