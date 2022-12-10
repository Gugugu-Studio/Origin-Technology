package com.gugugu.oritech.world.block.properties;

public class IntProperty extends AbstractProperty {
    public IntProperty(String name, int defVal) {
        super(name, new ValueHolder(defVal));
    }

    public int getValue() {
        return holder.getValueAsInteger();
    }

    @Override
    public String valueToString() {
        return Integer.toString(holder.getValueAsInteger());
    }
}
