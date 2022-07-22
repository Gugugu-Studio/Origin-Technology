package com.gugugu.oritech.client;

import com.gugugu.oritech.client.gl.GLStateMgr;
import com.gugugu.oritech.client.render.GameRenderer;
import com.gugugu.oritech.client.render.WorldRenderer;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.tex.SpriteInfo;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.ui.IKeyListener;
import com.gugugu.oritech.ui.ISizeListener;
import com.gugugu.oritech.ui.Keyboard;
import com.gugugu.oritech.ui.Mouse;
import com.gugugu.oritech.util.HitResult;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.ClientWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.entity.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Random;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
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
    @Nullable
    public ClientWorld world;
    @Nullable
    public WorldRenderer worldRenderer;
    @Nullable
    public PlayerEntity player;
    public int passedTick = 0;
    private int buildTick = 0;
    private Block handBlock = Blocks.STONE;

    public OriTechClient(int width, int height) {
        this.width = width;
        this.height = height;

        GLStateMgr.initialize();
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        this.gameRenderer = new GameRenderer(this);
        gameRenderer.init();

        Blocks.register();

        blockAtlas = new TextureAtlas();
        List<SpriteInfo> list = new ArrayList<>();
        for (Block block : Registry.BLOCK) {
            if (block.isAir()) {
                continue;
            }
            Identifier id = Registry.BLOCK.getId(block);
            list.add(new SpriteInfo(ResLocation.ofAssets(id.namespace(), "textures/block/" + id.path() + ".png"),
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
        world = new ClientWorld(this, Random.newSeed(), 0, 5, 0);
        worldRenderer = new WorldRenderer(this, world);
        player = new PlayerEntity(world);
        player.keyboard = keyboard;
    }

    public void updateTime() {
        timer.update();
        for (int i = 0; i < timer.ticks; i++) {
            tick();
            ++passedTick;
        }

        update((float) timer.deltaFrameTime);
    }

    public void update(float delta) {
        if (passedTick - buildTick >= 4) {
            if (mouse.isBtnDown(GLFW_MOUSE_BUTTON_LEFT)) {
                destroyBlock();
                buildTick = passedTick;
            }
            if (mouse.isBtnDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                placeBlock();
                buildTick = passedTick;
            }
        }
    }

    private void destroyBlock() {
        final HitResult hitResult = worldRenderer.hitResult;
        if (hitResult != null) {
            world.setBlock(Blocks.AIR, hitResult.x(), hitResult.y(), hitResult.z());
        }
    }

    private void placeBlock() {
        final HitResult hitResult = worldRenderer.hitResult;
        if (hitResult != null) {
            final Direction face = hitResult.face();
            if (face == null) {
                return;
            }
            final int hrx = hitResult.x();
            final int hry = hitResult.y();
            final int hrz = hitResult.z();
            if (world.canBlockPlaceOn(handBlock,
                hrx,
                hry,
                hrz,
                face)) {
                final int x = hrx + face.getOffsetX();
                final int y = hry + face.getOffsetY();
                final int z = hrz + face.getOffsetZ();
                world.setBlock(handBlock, x, y, z);
            }
        }
    }

    public void tick() {
        gameRenderer.camera.update();
        Vector3f pos = player.position;
        gameRenderer.camera.setPosition(pos.x, pos.y + player.getEyeHeight(), pos.z);
        player.tick();
    }

    public void render() {
        gameRenderer.render();
    }

    @Override
    public void onKey(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            switch (key) {
                case GLFW_KEY_ESCAPE -> mouse.setGrabbed(!mouse.isGrabbed());
                case GLFW_KEY_1 -> handBlock = Blocks.STONE;
                case GLFW_KEY_2 -> handBlock = Blocks.GRASS_BLOCK;
                case GLFW_KEY_3 -> handBlock = Blocks.DIRT;
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

    public void onMouseBtnPress(int btn, int mods) {
    }

    public void onMouseBtnRelease(int btn, int mods) {
    }

    @Override
    public void close() {
        blockAtlas.close();
        world.close();
        gameRenderer.close();
        GLStateMgr.close();
    }

    public static OriTechClient getClient() {
        return instance;
    }
}
