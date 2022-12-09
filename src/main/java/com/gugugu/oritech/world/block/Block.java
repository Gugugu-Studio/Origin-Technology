package com.gugugu.oritech.world.block;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.client.render.Tesselator;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public class Block {
    public boolean isAir() {
        return false;
    }

    public boolean hasSideTransparency() {
        return false;
    }

    public boolean isSolid() {
        return true;
    }

    public AABBox getOutline(int x, int y, int z) {
        return new AABBox(x, y, z,
            x + 1.0f, y + 1.0f, z + 1.0f);
    }

    public AABBox getRayCast(int x, int y, int z) {
        return getOutline(x, y, z);
    }

    public AABBox getCollision(int x, int y, int z) {
        return getOutline(x, y, z);
    }

    public boolean shouldRenderFace(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).hasSideTransparency();
    }

    public boolean canPlaceOn(Block target, World world, int x, int y, int z, Direction face) {
        return !world.getBlock(x + face.getOffsetX(),
            y + face.getOffsetY(),
            z + face.getOffsetZ()).isSolid();
    }

    public String getFaceTexture(Direction face) {
        Identifier id = Registry.BLOCK.getId(this);
        return ResLocation.ofAssets(id.namespace(), "textures/block/" + id.path() + ".png").toString();
    }

    public List<String> getTextures() {
        List<String> list = new ArrayList<>();
        Identifier id = Registry.BLOCK.getId(this);
        list.add(id.path());
        return list;
    }

    public void renderFace(Tesselator t, Direction face, int x, int y, int z) {
        float x0 = (float) x;
        float y0 = (float) y;
        float z0 = (float) z;
        float x1 = x0 + 1.0f;
        float y1 = y0 + 1.0f;
        float z1 = z0 + 1.0f;

        TextureAtlas atlas = OriTechClient.getClient().blockAtlas;
        String texName = getFaceTexture(face);
        float u0 = atlas.getU0n(texName);
        float v0 = atlas.getV0n(texName);
        float u1 = atlas.getU1n(texName);
        float v1 = atlas.getV1n(texName);

        switch (face) {
            case WEST -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(u0, v0).vertex(x0, y1, z0).emit();
                t.texCoord(u0, v1).vertex(x0, y0, z0).emit();
                t.texCoord(u1, v1).vertex(x0, y0, z1).emit();
                t.texCoord(u1, v0).vertex(x0, y1, z1).emit();
            }
            case EAST -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(u0, v0).vertex(x1, y1, z1).emit();
                t.texCoord(u0, v1).vertex(x1, y0, z1).emit();
                t.texCoord(u1, v1).vertex(x1, y0, z0).emit();
                t.texCoord(u1, v0).vertex(x1, y1, z0).emit();
            }
            case DOWN -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(u0, v0).vertex(x0, y0, z1).emit();
                t.texCoord(u0, v1).vertex(x0, y0, z0).emit();
                t.texCoord(u1, v1).vertex(x1, y0, z0).emit();
                t.texCoord(u1, v0).vertex(x1, y0, z1).emit();
            }
            case UP -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(u0, v0).vertex(x0, y1, z0).emit();
                t.texCoord(u0, v1).vertex(x0, y1, z1).emit();
                t.texCoord(u1, v1).vertex(x1, y1, z1).emit();
                t.texCoord(u1, v0).vertex(x1, y1, z0).emit();
            }
            case NORTH -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(u0, v0).vertex(x1, y1, z0).emit();
                t.texCoord(u0, v1).vertex(x1, y0, z0).emit();
                t.texCoord(u1, v1).vertex(x0, y0, z0).emit();
                t.texCoord(u1, v0).vertex(x0, y1, z0).emit();
            }
            case SOUTH -> {
                t.index(0, 1, 2, 2, 3, 0);
                t.texCoord(u0, v0).vertex(x0, y1, z1).emit();
                t.texCoord(u0, v1).vertex(x0, y0, z1).emit();
                t.texCoord(u1, v1).vertex(x1, y0, z1).emit();
                t.texCoord(u1, v0).vertex(x1, y1, z1).emit();
            }
        }
    }

    public boolean render(Tesselator t, World world, int x, int y, int z) {
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
                    t.color(c1, c1, c1);
                } else if (face.isOnAxisY()) {
                    t.color(c2, c2, c2);
                } else if (face.isOnAxisZ()) {
                    t.color(c3, c3, c3);
                }
                renderFace(t, face, x, y, z);
                rendered = true;
            }
        }
        return rendered;
    }
}
