package com.gugugu.oritech.world.block.properties;

public class ValueHolder {
    protected ValueType type;
    protected Object objVal;
    protected int intVal;
    protected float floatVal;

    protected ValueHolder(ValueType type, Object objVal, int intVal, float floatVal) {
        this.objVal = objVal;
        this.intVal = intVal;
        this.floatVal = floatVal;
        this.type = type;
    }

    public ValueHolder(Object val) {
        this(ValueType.OBJECT, val, 0, 0);
    }

    public ValueHolder(int val) {
        this(ValueType.INTEGER, null, val, 0);
    }

    public ValueHolder(float val) {
        this(ValueType.FLOAT, null, 0, val);
    }

    protected Object getValueAsObject() {
        if (type != ValueType.OBJECT) {
            throw new IllegalArgumentException("Type not match!Expect " + ValueType.OBJECT + ", real " + type);
        }
        return objVal;
    }

    public int getValueAsInteger() {
        if (type != ValueType.INTEGER) {
            throw new IllegalArgumentException("Type not match!Expect " + ValueType.INTEGER + ", real " + type);
        }
        return intVal;
    }

    public float getValueAsFloat() {
        if (type != ValueType.FLOAT) {
            throw new IllegalArgumentException("Type not match!Expect " + ValueType.FLOAT + ", real " + type);
        }
        return floatVal;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueAs() {
        return (T) getValueAsObject();
    }
}
