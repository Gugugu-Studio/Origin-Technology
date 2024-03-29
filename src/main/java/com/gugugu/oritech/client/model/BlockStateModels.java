package com.gugugu.oritech.client.model;

import com.gugugu.oritech.world.block.BlockState;

import java.util.List;

public class BlockStateModels {
    public final List<ModelOperators> models;
    public final StateMapping mapping;

    public BlockStateModels(List<ModelOperators> models, StateMapping mapping) {
        this.models = models;
        this.mapping = mapping;
    }

    public ModelOperators getModel(BlockState state) {
        return models.get(mapping.match(state));
    }
}
