package net.orekyuu.javatter.core.plugin.loader;

import java.nio.file.Path;

class ZipPluginLoader implements PluginLoader {
    @Override
    public boolean match(Path path) {
        return path.endsWith(".zip");
    }

    @Override
    public PluginContainer load(Path path) {
        return null;
    }
}
