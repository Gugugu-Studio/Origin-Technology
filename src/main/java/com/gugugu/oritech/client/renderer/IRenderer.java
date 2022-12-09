package com.gugugu.oritech.client.renderer;

import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.world.World;

@SideOnly(Side.CLIENT)
public interface IRenderer<T> {
    boolean render(Tesselator t, float x, float y, float z, World world, T obj);
}
