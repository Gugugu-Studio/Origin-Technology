package com.gugugu.oritech.block;

import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.client.model.Model;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.resource.ModelLoader;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.ResType;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public class Block {
    protected Model model;
    private static final Vector3f ZERO = new Vector3f(0, 0, 0);
    private static final Vector3f ONE = new Vector3f(1, 1, 1);

    public Block() {
    }

    public boolean isAir() {
        return false;
    }

    public boolean hasSideTransparency() {
        return false;
    }

    public boolean isSolid() {
        return true;
    }

    public List<AABBox> getOutline() {
        return new ArrayList<>() {
            {
                add(new AABBox(
                    0, 0, 0,
                    1.0f, 1.0f, 1.0f
                ));
            }
        };
    }

    public List<AABBox> getRayCast() {
        return getOutline();
    }

    public List<AABBox> getCollision() {
        return getOutline();
    }

    public boolean shouldRenderFace(World world, int x, int y, int z) {
//        return true;
        return this.hasSideTransparency() || world.getBlock(x, y, z).block.hasSideTransparency();
    }

    public boolean canPlaceOn(BlockState target, World world, int x, int y, int z, Direction face) {
        return !world.getBlock(x + face.getOffsetX(),
            y + face.getOffsetY(),
            z + face.getOffsetZ()).block.isSolid();
    }

    public void renderFace(Batch batch, Direction face) {
        model.renderFace(batch, face);
    }

    public boolean render(Batch batch, World world, int x, int y, int z) {
        boolean rendered = false;
        for (Direction face : Direction.values()) {
            if (shouldRenderFace(world,
                x + face.getOffsetX(),
                y + face.getOffsetY(),
                z + face.getOffsetZ())) {
                final float c1 = 1.0f;
                final float c2 = 0.8f;
                final float c3 = 0.6f;
                if (face.isOnAxisX()) {
                    batch.color(c1, c1, c1);
                } else if (face.isOnAxisY()) {
                    batch.color(c2, c2, c2);
                } else if (face.isOnAxisZ()) {
                    batch.color(c3, c3, c3);
                }
                renderFace(batch, face);
                rendered = true;
            }
        }
        return rendered;
    }

    public Model getModel() {
        Identifier id = Registry.BLOCK.getId(this);
        if (model == null) {
            model = ModelLoader.loadModel(new ResLocation(ResType.ASSETS, "oritech:models/block/" + id.path() + ".json"));
        }
        return model;
    }
}
