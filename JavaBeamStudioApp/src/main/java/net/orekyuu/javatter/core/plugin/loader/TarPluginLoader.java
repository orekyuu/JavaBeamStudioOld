package net.orekyuu.javatter.core.plugin.loader;

import java.nio.file.Path;

class TarPluginLoader implements PluginLoader {
    @Override
    public boolean match(Path path) {
        return path.endsWith(".tar.gz");
    }

    @Override
    public PluginContainer load(Path path) {
        return null;
    }
}
