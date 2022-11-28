package com.gugugu.oritech.world.block;

import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.math.Direction;
import com.gugugu.oritech.util.registry.Registry;

import java.util.List;

public class LogBlock extends Block {
    @Override
    public List<String> getTextures() {
        List<String> array = super.getTextures();
        array.add("log_top");
        return array;
    }
}
