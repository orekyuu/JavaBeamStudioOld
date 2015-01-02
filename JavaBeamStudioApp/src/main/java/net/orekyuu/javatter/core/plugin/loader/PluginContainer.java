package net.orekyuu.javatter.core.plugin.loader;

public final class PluginContainer {

    private final String name;
    private final String pluginId;
    private final String version;
    private final String entryPoint;
    private final String requireVersion;
    private final String filePath;

    PluginContainer(PluginInfoFile info, String filePath) {
        name = info.getName();
        pluginId = info.getId();
        version = info.getVersion();
        entryPoint = info.getEntryPoint();
        requireVersion = info.getRequireVersion();
        this.filePath = filePath;
    }

    public PluginContainer(PluginContainer container) {
        name = container.name;
        pluginId = container.pluginId;
        version = container.version;
        entryPoint = container.entryPoint;
        requireVersion = container.requireVersion;
        filePath = container.filePath;
    }

    public String getName() {
        return name;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getVersion() {
        return version;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public String getRequireVersion() {
        return requireVersion;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PluginContainer{");
        sb.append("name='").append(name).append('\'');
        sb.append(", pluginId='").append(pluginId).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", entryPoint='").append(entryPoint).append('\'');
        sb.append(", requireVersion='").append(requireVersion).append('\'');
        sb.append(", filePath='").append(filePath).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean isEqualPlugin(PluginContainer container) {
        return pluginId.equals(container.pluginId);
    }
}
