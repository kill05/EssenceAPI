package com.github.kill05.essenceapi.core.modules;

import java.util.HashMap;
import java.util.Map;

public class ModuleMap {

    private final Map<Class<?>, Object> moduleMap;

    public ModuleMap() {
        this.moduleMap = new HashMap<>();
    }


    public <T> void put(Class<? extends T> clazz, T module) {

    }

    public Map<Class<?>, Object> getMap() {
        return moduleMap;
    }
}
