package com.gugugu.oritech.block.properties;

public class FloatProperty extends AbstractProperty {
    public FloatProperty(String name, float defVal) {
        super(name, new ValueHolder(defVal));
    }

    public float getValue() {
        return holder.getValueAsFloat();
    }

    @Override
    public String valueToString() {
        return Float.toString(holder.getValueAsFloat());
    }
}
