package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.world.World;

public interface IRenderer<T> {
    boolean render(Batch batch, World world, T obj);
}
