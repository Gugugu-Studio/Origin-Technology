package com.gugugu.oritech.block.properties;

public abstract class AbstractProperty implements IProperty {
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

    @Override
    public ValueHolder getHolder() {
        return holder;
    }
}
