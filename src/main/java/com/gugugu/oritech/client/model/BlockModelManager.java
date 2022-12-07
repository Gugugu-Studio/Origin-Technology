package com.gugugu.oritech.client.model;

import com.gugugu.oritech.block.Block;
import com.gugugu.oritech.resource.ModelLoader;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.ResType;
import com.gugugu.oritech.resource.StateMappingLoader;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BlockModelManager {
    public static Map<Identifier, BlockStateModels> modelMap = new HashMap<>();

    public static void initialize() {
        for (Block block : Registry.BLOCK) {
            if (! block.hasModel()) {
                continue;
            }

            Identifier id = Registry.BLOCK.getId(block);

            BlockStateModels model = StateMappingLoader.loadBSModel(new ResLocation(
                ResType.ASSETS, "oritech:blockstates/" + id.path() + ".json")
            );

            modelMap.put(id, model);
        }
    }

    public static BlockStateModels getModel(Identifier id) {
        return modelMap.get(id);
    }
}
