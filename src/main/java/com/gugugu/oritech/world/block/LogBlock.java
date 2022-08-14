package com.gugugu.oritech.world.block;

import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;

import java.util.List;

public class LogBlock extends Block {
    @Override
    public String getFaceTexture(Direction face) {
        Identifier id = Registry.BLOCK.getId(this);
        if (face == Direction.UP || face == Direction.DOWN) {
            return ResLocation.ofAssets(id.namespace(), "textures/block/log_top.png").toString();
        }
        return super.getFaceTexture(face);
    }

    @Override
    public List<String> getTextures() {
        List<String> array = super.getTextures();
        array.add("log_top");
        return array;
    }
}
