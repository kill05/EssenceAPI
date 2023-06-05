package com.github.kill05.essenceapi.core.config.parsers;

import org.bukkit.Material;

public class MaterialParser implements IConfigParser<Material> {

    @Override
    public Material parse(Object value) {
        return Material.matchMaterial(value.toString());
    }

}
