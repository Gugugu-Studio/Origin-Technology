package com.gugugu.oritech.block;

import com.gugugu.oritech.block.properties.IProperty;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public class Block {
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
        return this.hasSideTransparency() || world.getBlock(x, y, z).block.hasSideTransparency();
    }

    public boolean canPlaceOn(BlockState target, World world, int x, int y, int z, Direction face) {
        return !world.getBlock(x + face.getOffsetX(),
            y + face.getOffsetY(),
            z + face.getOffsetZ()).block.isSolid();
    }

    public boolean hasModel() {
        return true;
    }

    public Identifier getRenderer() {
        return new Identifier("common");
    }

    public List<IProperty> getProperties() {
        return new ArrayList<>();
    }
}
