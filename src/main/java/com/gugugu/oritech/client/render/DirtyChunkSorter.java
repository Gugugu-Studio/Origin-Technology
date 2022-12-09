package com.gugugu.oritech.client.render;

import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.Timer;
import com.gugugu.oritech.world.chunk.RenderChunk;
import com.gugugu.oritech.entity.PlayerEntity;

/**
 * @author squid233
 * @since 1.0
 */
@SideOnly(Side.CLIENT)
public class DirtyChunkSorter {
    private static PlayerEntity playerEntity;
    private static double now = Timer.getTime();

    public static void reset(PlayerEntity player) {
        playerEntity = player;
        now = Timer.getTime();
    }

    public static int compare(RenderChunk c1, RenderChunk c2) {
        boolean visible1 = c1.testFrustum();
        boolean visible2 = c2.testFrustum();
        if (visible1 && !visible2) return -1;
        if (!visible1 && visible2) return 1;
        double t1 = (now - c1.dirtiedTime) * 0.5;
        double t2 = (now - c2.dirtiedTime) * 0.5;
        if (t1 < t2) return -1;
        if (t1 > t2) return 1;
        float d1 = c1.distanceSqr(playerEntity);
        float d2 = c2.distanceSqr(playerEntity);
        return Float.compare(d1, d2);
    }
}
