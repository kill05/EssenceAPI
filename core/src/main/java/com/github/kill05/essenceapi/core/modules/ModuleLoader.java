package com.github.kill05.essenceapi.core.modules;

import com.github.kill05.essenceapi.core.EssenceAPI;
import com.github.kill05.essenceapi.core.nms.NMSHelper;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;

public final class ModuleLoader {

    private static final String PACKAGE = "com.github.kill05.essenceapi";
    private static final ClassToInstanceMap<Object> hookMap = MutableClassToInstanceMap.create();

    @SuppressWarnings("unchecked")
    public static <T> void load(Class<T> clazz) {
        String version = NMSHelper.getVersion();
        String path = PACKAGE + ".craftbukkit_" + version;
        Reflections reflection = new Reflections(path);
        Set<Class<? extends T>> foundClasses = reflection.getSubTypesOf(clazz);

        if(foundClasses == null || foundClasses.isEmpty()) {
            EssenceAPI.log(Level.INFO, String.format("Failed to find an implementation of %s for NMS version %s.", clazz.getName(), version));
            return;
        }

        if(foundClasses.size() != 1) {
            EssenceAPI.log(Level.SEVERE, String.format("Found more than an implementation of %s! This is a bug!", clazz.getName()));
        }

        Class<? extends T> implClass = foundClasses.iterator().next();
        try {
            EssenceAPI.log(Level.INFO, String.format("Found an implementation of %s for NMS version %s.", clazz.getName(), version));
            hookMap.putInstance(clazz, (T) ConstructorUtils.invokeConstructor(implClass, null));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            EssenceAPI.log(Level.SEVERE, String.format("Failed to initialize an implementation of %s for NMS version %s. This is a bug!", clazz.getName(), version), e);
        }
    }

    public static <T> T getModule(Class<T> module) {
        return hookMap.getInstance(module);
    }

    public static boolean isLoaded(Class<?> clazz) {
        return hookMap.containsKey(clazz);
    }

    public static void checkLoaded(Class<?> clazz) {
        if(!isLoaded(clazz)) throw new IllegalStateException(String.format("There is no implementation of %s for NMS version %s. This is a bug!", clazz.getName(), NMSHelper.getVersion()));
    }

}
