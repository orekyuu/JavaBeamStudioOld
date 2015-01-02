package net.orekyuu.javatter.core.plugin;

import net.orekyuu.javatter.api.plugin.OnLoad;
import net.orekyuu.javatter.api.plugin.OnPreLoad;
import net.orekyuu.javatter.core.plugin.loader.JavatterPluginLoader;
import net.orekyuu.javatter.core.plugin.loader.PluginContainer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public final class PluginManager {

    private static final PluginManager instance = new PluginManager();

    private List<PluginContainer> pluginContainers = new LinkedList<>();
    private Map<String, Object> pluginInstanceMap = new TreeMap<>();

    public static PluginManager getInstance() {
        return instance;
    }

    public void load() {
        JavatterPluginLoader loader = new JavatterPluginLoader();
        Path path = Paths.get("plugins");
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pluginContainers = loader.readPluginDirectory(path);
        preLoadPlugins();
    }

    private void preLoadPlugins() {
        pluginInstanceMap.clear();
        for (PluginContainer container : pluginContainers) {
            String entryPoint = container.getEntryPoint();
            try {
                Class<?> entryPointClass = Class.forName(entryPoint);
                Object obj = entryPointClass.getConstructor().newInstance();
                pluginInstanceMap.put(container.getPluginId(), obj);
                callAnnotationMethod(OnPreLoad.class, obj);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    public void callOnLoad() {
        for (PluginContainer container : pluginContainers) {
            Object obj = findPluginInstance(container.getPluginId()).get();
            callAnnotationMethod(OnLoad.class, obj);
        }
    }

    private <T extends Annotation> void callAnnotationMethod(Class<T> annotationClass, Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            T load = method.getDeclaredAnnotation(annotationClass);
            if (load == null) {
                continue;
            }
            method.setAccessible(true);
            try {
                method.invoke(obj);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * プラグインIDに対応するエントリポイントのオブジェクトを返します
     * @param pluginId プラグインID
     * @param <T> エントリポイントの型
     * @return IDに対応するインスタンス
     */
    public <T> Optional<T> findPluginInstance(String pluginId) {
        return Optional.ofNullable((T) pluginInstanceMap.get(pluginId));
    }

    /**
     * プラグインコンテナのリストを返す
     * @return 登録されているプラグインコンテナ
     */
    public List<PluginContainer> getPluginContainers() {
        return pluginContainers.stream().map(PluginContainer::new).collect(Collectors.toList());
    }
}
