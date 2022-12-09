package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.registry.ClientRegistry;

@SideOnly(Side.CLIENT)
public class BlockStateRenderers {
    public static final AbstractBlockStateRenderer COMMON = register(0, "common", new BlockStateRenderer());

    private static AbstractBlockStateRenderer register(int rawId, String id, AbstractBlockStateRenderer renderer) {
        return ClientRegistry.BLOCKSTATE_RENDERER.set(rawId, new Identifier(id), renderer);
    }

    public static void register() {
        // Empty initializer
    }
}
