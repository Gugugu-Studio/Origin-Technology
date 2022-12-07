package com.gugugu.oritech.client.model;

import com.gugugu.oritech.block.BlockState;
import org.joml.Vector3f;

import java.util.List;

public class BlockStateModels {
    public List<ModelOperators> models;
    public StateMapping mapping;

    public BlockStateModels(List<ModelOperators> models, StateMapping mapping) {
        this.models = models;
        this.mapping = mapping;
    }

    public ModelOperators getModel(BlockState state) {
        return models.get(mapping.match(state));
    }
}
