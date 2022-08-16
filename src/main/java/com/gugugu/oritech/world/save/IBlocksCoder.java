package com.gugugu.oritech.world.save;

import com.gugugu.oritech.world.block.Block;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IBlocksCoder {
    Block[][][] getBlocks(DataInputStream save);
    void saveBlocks(int width, int depth, int height, Block[][][] blocks, DataOutputStream save);
}
