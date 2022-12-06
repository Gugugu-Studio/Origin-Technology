package com.gugugu.oritech.block.properties;

public abstract class AbstractProperty<T> implements IProperty<T> {
    protected String name;
    protected ValueHolder holder;

    public AbstractProperty(String name, ValueHolder holder) {
        this.name = name;
        this.holder = holder;
    }

    @Override
    public String getName() {
        return name;
    }
}
