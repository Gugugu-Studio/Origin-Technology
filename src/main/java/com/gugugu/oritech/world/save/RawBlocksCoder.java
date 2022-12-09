package com.gugugu.oritech.world.save;

import com.gugugu.oritech.world.block.BlockState;
import com.gugugu.oritech.util.registry.Registry;

import java.io.*;

public class RawBlocksCoder implements IBlocksCoder {
    @Override
    public BlockState[][][] getBlocks(DataInputStream save) {
        BlockState[][][] blocks;
        try {
            short width = save.readShort();
            short depth = save.readShort();
            short height = save.readShort();
            short reserved = save.readShort();

            blocks = new BlockState[height][width][depth];
            for (int y = 0 ; y < height ; y ++) {
                for (int x = 0 ; x < width ; x ++) {
                    for (int z = 0 ; z < depth ; z ++) {
                        int rawId = save.readInt();
                        blocks[y][x][z] = new BlockState(Registry.BLOCK.get(rawId));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return blocks;
    }

    @Override
    public void saveBlocks(int width, int depth, int height, BlockState[][][] blocks, DataOutputStream save) {
        try {
            save.writeShort(width);
            save.writeShort(depth);
            save.writeShort(height);
            save.writeShort(0);

            for (int y = 0 ; y < height ; y ++) {
                for (int x = 0 ; x < width ; x ++) {
                    for (int z = 0 ; z < depth ; z ++) {
                        save.writeInt(Registry.BLOCK.getRawId(blocks[y][x][z].getBlock()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
