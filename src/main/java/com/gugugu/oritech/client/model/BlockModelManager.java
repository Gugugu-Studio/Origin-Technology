package com.gugugu.oritech.client.model;

import com.gugugu.oritech.block.Block;
import com.gugugu.oritech.resource.ModelLoader;
import com.gugugu.oritech.resource.ResLocation;
import com.gugugu.oritech.resource.ResType;
import com.gugugu.oritech.util.Identifier;
import com.gugugu.oritech.util.Side;
import com.gugugu.oritech.util.SideOnly;
import com.gugugu.oritech.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BlockModelManager {
    public static Map<Identifier, Model> modelMap = new HashMap<>();

    public static void initialize() {
        for (Block block : Registry.BLOCK) {
            if (! block.hasModel()) {
                continue;
            }

            Identifier id = Registry.BLOCK.getId(block);

            Model model = ModelLoader.loadModel(new ResLocation(
                ResType.ASSETS, "oritech:models/block/" + id.path() + ".json")
            );

            modelMap.put(id, model);
        }
    }

    public static Model getModel(Identifier id) {
        return modelMap.get(id);
    }
}
