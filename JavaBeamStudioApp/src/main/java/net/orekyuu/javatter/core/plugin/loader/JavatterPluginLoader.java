package net.orekyuu.javatter.core.plugin.loader;

import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.util.VersionComparator;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * プラグインを読み込むローダー
 */
public class JavatterPluginLoader {

    private PluginLoader[] pluginLoaders = {
            new JarPluginLoader(),
//            new ZipPluginLoader(),
//            new TarPluginLoader(),
    };

    /**
     * プラグインディレクトリを読み込みます
     * @param path ディレクトリのパス
     * @return ロードしたプラグインの情報
     */
    public List<PluginContainer> readPluginDirectory(Path path) {
        LinkedList<PluginContainer> containers = new LinkedList<>();
        try {
            Files.list(path).forEach(p -> loadPath(p, containers));
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkPlugins(containers);
        loadPluginContainer(containers);
        return containers;
    }

    private void checkPlugins(LinkedList<PluginContainer> containers) {
        for (int i = 0; i < containers.size() - 1; i++) {
            PluginContainer container1 = containers.get(i);
            for (int k = i + 1; k < containers.size(); k++) {
                PluginContainer container2 = containers.get(k);
                if (container1.isEqualPlugin(container2)) {
                    loadError("プラグインIDが重複しています。：" + container1.getPluginId());
                }
            }
        }

        String version = API.getInstance().getApplication().getVersion();
        VersionComparator comparator = new VersionComparator();
        containers.stream().filter(container -> comparator.compare(version, container.getRequireVersion()) < 0).forEach(container -> {
            loadError(container.getName() + "(" + container.getVersion() + ")は" + container.getRequireVersion() + "以上のJavaビーム工房でしか使えません。");
        });
    }

    private void loadError(String message) {
        ExceptionDialogBuilder.create(new PluginLoadException(message));
        //プラグインの読み込みに失敗したらアプリケーション終了
        API.getInstance().getApplication().getPrimaryStage().close();
    }

    private void loadPath(Path path, LinkedList<PluginContainer> containers) {
        Optional<PluginLoader> optional = Arrays.stream(pluginLoaders).filter(loader -> loader.match(path)).findFirst();
        optional.ifPresent(o -> {
            PluginContainer container = o.load(path);
            if (container != null)
                containers.add(container);
        });
    }

    private void loadPluginContainer(LinkedList<PluginContainer> containers) {
        try {
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);
            for (PluginContainer container : containers) {
                File file = new File(container.getFilePath());
                addURL.invoke(ClassLoader.getSystemClassLoader(), file.toURI().toURL());
            }
        } catch (ReflectiveOperationException | MalformedURLException e) {
            e.printStackTrace();
        }
    }


}
