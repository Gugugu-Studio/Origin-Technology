package com.gugugu.oritech.server;

import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

/**
 * @author squid233
 * @since 1.0
 */
public abstract class Server implements Runnable {
    @Nullable
    public ServerWorld world;

    public abstract boolean isIntegrated();

    public abstract int getTicks();
}
