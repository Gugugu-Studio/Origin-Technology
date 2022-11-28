package com.gugugu.oritech.world.block;

import com.gugugu.oritech.client.OriTechClient;
import com.gugugu.oritech.client.render.Batch;
import com.gugugu.oritech.client.render.RenderBox;
import com.gugugu.oritech.phys.AABBox;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.tex.TextureAtlas;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.World;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author squid233
 * @since 1.0
 */
public class Block {
    protected List<RenderBox> renderBoxes = new ArrayList<>();
    private static final Vector3f ZERO = new Vector3f(0, 0, 0);
    private static final Vector3f ONE = new Vector3f(1, 1, 1);

    public Block() {
        RenderBox.Builder builder = new RenderBox.Builder();
        builder.min(ZERO);
        builder.max(ONE);
        RenderBox.UVGroup group = new RenderBox.UVGroup();
        group.uv_min = new Vector2d(0, 0);
        group.uv_max = new Vector2d(1, 1);
        builder.allGroup(group);

        renderBoxes.add(builder.build());
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

    public void renderFace(Batch batch, Direction face, int x, int y, int z) {
        String texName = getFaceTexture(face);

        TextureAtlas atlas = OriTechClient.getClient().blockAtlas;
        Vector2d uv0 = new Vector2d(atlas.getU0(texName), atlas.getV0(texName));
        Vector2d uv1 = new Vector2d(atlas.getU1(texName), atlas.getV1(texName));

        for (RenderBox renderBox : renderBoxes) {
            renderBox.renderFace(batch, face, x, y, z, uv0, uv1, atlas, 1);
        }
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
                renderFace(batch, face, x, y, z);
                rendered = true;
            }
        }
        return rendered;
    }
}
