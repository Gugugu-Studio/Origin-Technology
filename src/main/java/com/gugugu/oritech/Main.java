package com.gugugu.oritech;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.server.IntegratedServer;
import com.gugugu.oritech.ui.Window;
import com.gugugu.oritech.world.ServerWorld;
import com.gugugu.oritech.world.block.Block;
import com.gugugu.oritech.world.block.Blocks;
import com.gugugu.oritech.world.chunk.LogicChunk;
import com.gugugu.oritech.world.save.BlocksCoders;
import org.lwjgl.opengl.GL11C;

import java.io.*;

import static org.lwjgl.opengl.GL11C.*;

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

        //test
        try {
            DataInputStream input = new DataInputStream(new FileInputStream("save.dat"));
            Block[][][] blocks = BlocksCoders.RAW.getBlocks(input);
            for (int y = 0 ; y < 32 ; y ++) {
                for (int x = 0 ; x < 32 ; x ++) {
                    for (int z = 0 ; z < 32 ; z ++) {
                        LogicChunk chunk = OriTechClient.getServer().world.getChunk(0, 0, 0);
                        chunk.setBlock(blocks[y][x][z], x, y, z);
                    }
                }
            }
        } catch (FileNotFoundException e) {
        }
    }

    @Override
    public void update() {
        client.run();
    }

    @Override
    public void close() {
        //test
        LogicChunk chunk = OriTechClient.getServer().world.getChunk(0, 0, 0);
        Block[][][] blocks = chunk.getBlocks();

        try {
            DataOutputStream output = new DataOutputStream(new FileOutputStream("save.dat"));
            BlocksCoders.RAW.saveBlocks(32, 32, 32, blocks, output);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

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
