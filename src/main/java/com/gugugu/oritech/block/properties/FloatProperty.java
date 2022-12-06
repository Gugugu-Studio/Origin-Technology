package com.gugugu.oritech.block.properties;

public class FloatProperty extends AbstractProperty<Float> {
    public FloatProperty(String name, float defVal) {
        super(name, new ValueHolder(defVal));
    }

    @Override
    public Float getValue() {
        return holder.getValueAsFloat();
    }
}
