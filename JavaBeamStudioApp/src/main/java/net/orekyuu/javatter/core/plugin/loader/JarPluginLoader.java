package net.orekyuu.javatter.core.plugin.loader;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class JarPluginLoader implements PluginLoader {
    @Override
    public boolean match(Path path) {
        return path.toString().endsWith(".jar");
    }

    @Override
    public PluginContainer load(Path path) {
        InputStream inputStream = null;
        PluginInfoFile info = null;
        try {
            JarFile jarFile = new JarFile(path.toFile());
            JarEntry pluginInfoEntry = jarFile.getJarEntry("plugin.info");
            if (pluginInfoEntry == null) {
                return null;
            }
            inputStream = jarFile.getInputStream(pluginInfoEntry);
            Gson gson = new Gson();
            info = gson.fromJson(new InputStreamReader(inputStream), PluginInfoFile.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new PluginContainer(info, path.toString());
    }
}
