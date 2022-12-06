package com.gugugu.oritech.block.properties;

public class IntProperty extends AbstractProperty<Integer> {
    public IntProperty(String name, int defVal) {
        super(name, new ValueHolder(defVal));
    }

    @Override
    public Integer getValue() {
        return holder.getValueAsInteger();
    }
}
