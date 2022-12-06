package com.gugugu.oritech.block.properties;

public class EnumProperty<T extends Enum<T>> extends ObjectProperty<T> {
    public EnumProperty(String name, T defVal) {
        super(name, defVal);
    }
}
