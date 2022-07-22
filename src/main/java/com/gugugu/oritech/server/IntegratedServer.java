package com.gugugu.oritech.server;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.world.ServerWorld;
import org.joml.Random;

/**
 * @author squid233
 * @since 1.0
 */
public class IntegratedServer extends Server implements AutoCloseable {
    private final OriTechClient client;
    public final Timer timer = new Timer();
    public int passedTick = 0;

    public IntegratedServer(OriTechClient client) {
        this.client = client;
    }

    public void start() {
        world = new ServerWorld(Random.newSeed() ^ System.nanoTime(),
            client.gameRenderer.renderDistance,
            0, 5, 0);
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
    }

    public void tick() {
    }

    @Override
    public void run() {
        updateTime();
    }

    @Override
    public boolean isIntegrated() {
        return true;
    }

    @Override
    public void close() {
    }
}
