package com.gugugu.oritech.client.model;

import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.world.block.properties.IProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StateMapping {
    protected final String name;
    public final Map<String, StateMapping> nextLevel = new HashMap<>();
    public final Map<String, Integer> mapping = new HashMap<>();

    public StateMapping(String name) {
        this.name = name;
    }

    public int match(BlockState state) {
        if (Objects.equals(name, "__root__")) {
            if (mapping.containsKey("default")) {
                return mapping.get("default");
            }
            else {
                return nextLevel.get("default").match(state);
            }
        }
        IProperty property = state.getProperty(name);
        if (property == null) {
            throw new IllegalArgumentException("State " + name + " not found");
        }

        String val = property.valueToString();
        if (mapping.containsKey(val)) {
            return mapping.get(val);
        }
        else if (nextLevel.containsKey(val)) {
            return nextLevel.get(val).match(state);
        }

        throw new IllegalArgumentException("Unknown state " + name + ":" + val);
    }
}
