package com.gugugu.oritech.client.gl;

import java.util.*;

public class UniformLayout {
    protected Map<String, Map.Entry<Integer, Integer>> layout = new HashMap<>();
    protected int offset = 0;

    public UniformLayout() {
    }

    protected static <K, V> Map.Entry<K, V> make(K k, V v) {
        return new AbstractMap.SimpleEntry<>(k, v);
    }

    protected UniformLayout _addUniform(String name, int size) {
        layout.put(name, make(offset, size));
        offset += size;
        return this;
    }

    public UniformLayout addUniform(String name, int size) {
        return _addUniform(name, size);
    }

    public UniformLayout addArrayUniform(String name, int size, int elements) {
        return _addUniform(name, size * elements);
    }

    public UniformLayout addMatrixUniform(String name, int size) {
        return addArrayUniform(name, 16, size);
    }

    public UniformLayout addMatrix3fUniform(String name) {
        return addMatrixUniform(name, 3);
    }

    public UniformLayout addMatrix4fUniform(String name) {
        return addMatrixUniform(name, 4);
    }

    public UniformLayout addIntUniform(String name) {
        return addUniform(name, 4);
    }

    public UniformLayout addBoolUniform(String name) {
        return addUniform(name, 4);
    }

    public UniformLayout addFloatUniform(String name) {
        return addUniform(name, 4);
    }

    public UniformLayout addScalarArrayUniform(String name, int size) {
        return addArrayUniform(name, 16, size);
    }

    public UniformLayout addVecArrayUniform(String name, int size) {
        return addArrayUniform(name, 16, size);
    }

    public UniformLayout addVec2Uniform(String name) {
        return addUniform(name, 8);
    }

    public UniformLayout addVec3Uniform(String name) {
        return addUniform(name, 16);
    }

    public UniformLayout addVec4Uniform(String name) {
        return addUniform(name, 16);
    }

    protected UniformLayout addAlignedStructUniform(String name, int aligned_size) {
        return addUniform(name, aligned_size);
    }

    protected UniformLayout addAlignedStructArrayUniform(String name, int aligned_size, int elements) {
        return addArrayUniform(name, aligned_size, elements);
    }

    public UniformLayout addStructUniform(String name, int size) {
        return addAlignedStructUniform(name, (size + 0xF) & ~0xF);
    }

    public UniformLayout addStructArrayUniform(String name, int size, int elements) {
        return addAlignedStructArrayUniform(name, (size + 0xF) & ~0xF, elements);
    }

    public int getLayoutSize() {
        return offset;
    }

    public int getOffset(String name) {
        return layout.get(name).getKey();
    }

    public int getSize(String name) {
        return layout.get(name).getValue();
    }
}
