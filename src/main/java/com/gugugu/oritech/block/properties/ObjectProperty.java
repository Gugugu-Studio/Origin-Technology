package com.gugugu.oritech.block.properties;

public class ObjectProperty<T> extends AbstractProperty<T> {
    public ObjectProperty(String name, T defVal) {
        super(name, new ValueHolder(defVal));
    }

    @Override
    public T getValue() {
        return holder.getValueAs();
    }
}
