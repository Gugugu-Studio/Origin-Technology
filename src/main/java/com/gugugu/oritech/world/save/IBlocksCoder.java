package com.gugugu.oritech.world.save;

import com.gugugu.oritech.world.block.BlockState;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IBlocksCoder {
    BlockState[][][] getBlocks(DataInputStream save);
    void saveBlocks(int width, int depth, int height, BlockState[][][] blocks, DataOutputStream save);
}
