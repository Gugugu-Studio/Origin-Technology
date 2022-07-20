package com.gugugu.oritech.client;

import com.gugugu.oritech.client.render.Frustum;
import com.gugugu.oritech.client.render.GameRenderer;
import com.gugugu.oritech.client.render.WorldRenderer;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.tex.SpriteInfo;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.ui.IKeyListener;
import com.gugugu.oritech.ui.ISizeListener;
import com.gugugu.oritech.ui.Keyboard;
import com.gugugu.oritech.ui.Mouse;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.entity.PlayerEntity;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL12C.*;

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
    public TextureAtlas blockAtlas;
    public ClientWorld world;
    public WorldRenderer worldRenderer;
    public PlayerEntity player;

    public OriTechClient(int width, int height) {
        this.width = width;
        this.height = height;

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        this.gameRenderer = new GameRenderer();
        gameRenderer.init();

        Blocks.register();

        blockAtlas = new TextureAtlas();
        List<SpriteInfo> list = new ArrayList<>();
        for (Block block : Registry.BLOCK) {
            if (block.isAir()) {
                continue;
            }
            Identifier id = Registry.BLOCK.getId(block);
            list.add(new SpriteInfo(ResLocation.ofTexture(id.namespace(), "block/" + id.path()),
                32,
                32));
        }
        blockAtlas.extraParam(target -> {
            glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
            glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameterf(target, GL_TEXTURE_MIN_LOD, 0);
            glTexParameterf(target, GL_TEXTURE_MAX_LOD, 5);
            glTexParameteri(target, GL_TEXTURE_MAX_LEVEL, 5);
        });
        blockAtlas.load(list);

        instance = this;
    }

    public void lazyInit() {
        world = new ClientWorld(256, 64, 256);
        worldRenderer = new WorldRenderer(world);
        player = new PlayerEntity(world);
        player.keyboard = keyboard;
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
        gameRenderer.camera.update();
        Vector3f pos = player.position;
        gameRenderer.camera.setPosition(pos.x, pos.y + player.getEyeHeight(), pos.z);
        player.tick();
    }

    public void render() {
        gameRenderer.setupCamera((float) timer.partialTick, width, height);
        gameRenderer.useShader(gameRenderer.positionColorTex(),
            shader -> {
                shader.getUniform("Projection").ifPresent(uniform -> uniform.set(gameRenderer.projection));
                shader.getUniform("View").ifPresent(uniform -> uniform.set(gameRenderer.view));
                shader.getUniform("Model").ifPresent(uniform -> uniform.set(gameRenderer.model));
                shader.uploadUniforms();
                Frustum.update(gameRenderer.projection.pushMatrix().mul(gameRenderer.view));
                gameRenderer.projection.popMatrix();
                worldRenderer.updateDirtyChunks(player);

                blockAtlas.bind();
                worldRenderer.render();
                glBindTexture(GL_TEXTURE_2D, 0);
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
            player.rotate((float) xd * SENSITIVITY,
                (float) -yd * SENSITIVITY);
            gameRenderer.camera.setRotation(player.rotation.y - 90.0f, player.rotation.x);
        }
    }

    @Override
    public void close() {
        blockAtlas.close();
        world.close();
        gameRenderer.close();
    }

    public static OriTechClient getClient() {
        return instance;
    }
}
