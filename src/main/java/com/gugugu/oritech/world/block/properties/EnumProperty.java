package com.gugugu.oritech.world.block.properties;

public class EnumProperty<T extends Enum<T>> extends ObjectProperty<T> {
    public EnumProperty(String name, T defVal) {
        super(name, defVal);
    }

    @Override
    public String valueToString() {
        return holder.<T>getValueAs().name();
    }
}
