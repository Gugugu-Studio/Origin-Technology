package com.gugugu.oritech.world.save;

import com.gugugu.oritech.util.registry.Registry;
import com.gugugu.oritech.world.block.Block;

import java.io.*;

public class RawBlocksCoder implements IBlocksCoder {
    @Override
    public Block[][][] getBlocks(DataInputStream save) {
        Block[][][] blocks;
        try {
            short width = save.readShort();
            short depth = save.readShort();
            short height = save.readShort();
            short reserved = save.readShort();

            blocks = new Block[height][width][depth];
            for (int y = 0 ; y < height ; y ++) {
                for (int x = 0 ; x < width ; x ++) {
                    for (int z = 0 ; z < depth ; z ++) {
                        int rawId = save.readInt();
                        blocks[y][x][z] = Registry.BLOCK.get(rawId);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return blocks;
    }

    @Override
    public void saveBlocks(int width, int depth, int height, Block[][][] blocks, DataOutputStream save) {
        try {
            save.writeShort(width);
            save.writeShort(depth);
            save.writeShort(height);
            save.writeShort(0);

            for (int y = 0 ; y < height ; y ++) {
                for (int x = 0 ; x < width ; x ++) {
                    for (int z = 0 ; z < depth ; z ++) {
                        save.writeInt(Registry.BLOCK.getRawId(blocks[y][x][z]));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
