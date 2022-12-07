package com.gugugu.oritech.block.properties;

public abstract class ObjectProperty<T> extends AbstractProperty {
    public ObjectProperty(String name, T defVal) {
        super(name, new ValueHolder(defVal));
    }

    public T getValue() {
        return holder.getValueAs();
    }
}
